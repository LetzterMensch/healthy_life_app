/*  Copyright (C) 2022-2024 José Rebelo, Reiner Herrmann

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
package com.example.gr.controller.device.huami.zeppos;

import com.example.gr.controller.device.model.ActivitySummaryEntries;
import com.example.gr.utils.constant.ActivityKind;
import com.example.gr.model.database.entities.BaseActivitySummary;
import com.example.gr.controller.device.huami.AbstractHuamiActivityDetailsParser;
import com.example.gr.controller.device.huami.HuamiActivitySummaryParser;
import com.example.gr.controller.device.huami.HuamiProtos;
import com.google.protobuf.InvalidProtocolBufferException;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class ZeppOsActivitySummaryParser extends HuamiActivitySummaryParser {
    private static final Logger LOG = LoggerFactory.getLogger(ZeppOsActivitySummaryParser.class);

    @Override
    public AbstractHuamiActivityDetailsParser getDetailsParser(final BaseActivitySummary summary) {
        return new ZeppOsActivityDetailsParser(summary);
    }

    @Override
    protected void parseBinaryData(final BaseActivitySummary summary, final Date startTime) {
        final byte[] rawData = summary.getRawSummaryData();
        final int version = (rawData[0] & 0xff) | ((rawData[1] & 0xff) << 8);
        if (version != 0x8000) {
            LOG.warn("Unexpected binary data version {}, parsing might fail", version);
        }

        final byte[] protobufData = ArrayUtils.subarray(rawData, 2, rawData.length);
        final HuamiProtos.WorkoutSummary summaryProto;
        try {
            summaryProto = HuamiProtos.WorkoutSummary.parseFrom(protobufData);
        } catch (final InvalidProtocolBufferException e) {
            LOG.error("Failed to parse summary protobuf data", e);
            return;
        }

        if (summaryProto.hasType()) {
            final ZeppOsActivityType activityType = ZeppOsActivityType
                    .fromCode((byte) summaryProto.getType().getType());

            final int activityKind;
            if (activityType != null) {
                activityKind = activityType.toActivityKind();
            } else {
                LOG.warn("Unknown workout activity type code {}", String.format("0x%X", summaryProto.getType().getType()));
                activityKind = ActivityKind.TYPE_UNKNOWN;
            }
            summary.setActivityKind(activityKind);
        }

        if (summaryProto.hasTime()) {
            int totalDuration = summaryProto.getTime().getTotalDuration();
            summary.setEndTime(new Date(startTime.getTime() + totalDuration * 1000L));
            addSummaryData(ActivitySummaryEntries.ACTIVE_SECONDS, summaryProto.getTime().getWorkoutDuration(), ActivitySummaryEntries.UNIT_SECONDS);
            // TODO pause durations
        }

        if (summaryProto.hasLocation()) {
            summary.setBaseLongitude(summaryProto.getLocation().getBaseLongitude());
            summary.setBaseLatitude(summaryProto.getLocation().getBaseLatitude());
            summary.setBaseAltitude(summaryProto.getLocation().getBaseAltitude() / 2);
            // TODO: Min/Max Latitude/Longitude
            addSummaryData(ActivitySummaryEntries.ALTITUDE_BASE, summaryProto.getLocation().getBaseAltitude() / 2, ActivitySummaryEntries.UNIT_METERS);
        }

        if (summaryProto.hasHeartRate()) {
            addSummaryData(ActivitySummaryEntries.HR_AVG, summaryProto.getHeartRate().getAvg(), ActivitySummaryEntries.UNIT_BPM);
            addSummaryData(ActivitySummaryEntries.HR_MAX, summaryProto.getHeartRate().getMax(), ActivitySummaryEntries.UNIT_BPM);
            addSummaryData(ActivitySummaryEntries.HR_MIN, summaryProto.getHeartRate().getMin(), ActivitySummaryEntries.UNIT_BPM);
        }

        if (summaryProto.hasSteps()) {
            addSummaryData(ActivitySummaryEntries.CADENCE_MAX, summaryProto.getSteps().getMaxCadence() * 60, ActivitySummaryEntries.UNIT_SPM);
            addSummaryData(ActivitySummaryEntries.CADENCE_AVG, summaryProto.getSteps().getAvgCadence() * 60, ActivitySummaryEntries.UNIT_SPM);
            addSummaryData(ActivitySummaryEntries.STRIDE_AVG, summaryProto.getSteps().getAvgStride(), ActivitySummaryEntries.UNIT_CM);
            addSummaryData(ActivitySummaryEntries.STEPS, summaryProto.getSteps().getSteps(), ActivitySummaryEntries.UNIT_STEPS);
        }

        if (summaryProto.hasDistance()) {
            addSummaryData(ActivitySummaryEntries.DISTANCE_METERS, summaryProto.getDistance().getDistance(), ActivitySummaryEntries.UNIT_METERS);
        }

        if (summaryProto.hasPace()) {
            addSummaryData(ActivitySummaryEntries.PACE_MAX, summaryProto.getPace().getBest(), ActivitySummaryEntries.UNIT_SECONDS_PER_M);
            addSummaryData(ActivitySummaryEntries.PACE_AVG_SECONDS_KM, summaryProto.getPace().getAvg() * 1000, ActivitySummaryEntries.UNIT_SECONDS_PER_KM);
        }

        if (summaryProto.hasCalories()) {
            addSummaryData(ActivitySummaryEntries.CALORIES_BURNT, summaryProto.getCalories().getCalories(), ActivitySummaryEntries.UNIT_KCAL);
        }

        if (summaryProto.hasHeartRateZones()) {
            // TODO hr zones bpm?
            if (summaryProto.getHeartRateZones().getZoneTimeCount() == 6) {
                addSummaryData(ActivitySummaryEntries.HR_ZONE_NA, summaryProto.getHeartRateZones().getZoneTime(0), ActivitySummaryEntries.UNIT_SECONDS);
                addSummaryData(ActivitySummaryEntries.HR_ZONE_WARM_UP, summaryProto.getHeartRateZones().getZoneTime(1), ActivitySummaryEntries.UNIT_SECONDS);
                addSummaryData(ActivitySummaryEntries.HR_ZONE_FAT_BURN, summaryProto.getHeartRateZones().getZoneTime(2), ActivitySummaryEntries.UNIT_SECONDS);
                addSummaryData(ActivitySummaryEntries.HR_ZONE_AEROBIC, summaryProto.getHeartRateZones().getZoneTime(3), ActivitySummaryEntries.UNIT_SECONDS);
                addSummaryData(ActivitySummaryEntries.HR_ZONE_ANAEROBIC, summaryProto.getHeartRateZones().getZoneTime(4), ActivitySummaryEntries.UNIT_SECONDS);
                addSummaryData(ActivitySummaryEntries.HR_ZONE_EXTREME, summaryProto.getHeartRateZones().getZoneTime(5), ActivitySummaryEntries.UNIT_SECONDS);
            } else {
                LOG.warn("Unexpected number of HR zones {}", summaryProto.getHeartRateZones().getZoneTimeCount());
            }
        }

        if (summaryProto.hasTrainingEffect()) {
            addSummaryData(ActivitySummaryEntries.TRAINING_EFFECT_AEROBIC, summaryProto.getTrainingEffect().getAerobicTrainingEffect(), ActivitySummaryEntries.UNIT_NONE);
            addSummaryData(ActivitySummaryEntries.TRAINING_EFFECT_ANAEROBIC, summaryProto.getTrainingEffect().getAnaerobicTrainingEffect(), ActivitySummaryEntries.UNIT_NONE);
            addSummaryData(ActivitySummaryEntries.WORKOUT_LOAD, summaryProto.getTrainingEffect().getCurrentWorkoutLoad(), ActivitySummaryEntries.UNIT_NONE);
            addSummaryData(ActivitySummaryEntries.MAXIMUM_OXYGEN_UPTAKE, summaryProto.getTrainingEffect().getMaximumOxygenUptake(), ActivitySummaryEntries.UNIT_ML_KG_MIN);
        }

        if (summaryProto.hasAltitude()) {
            addSummaryData(ActivitySummaryEntries.ALTITUDE_MAX, summaryProto.getAltitude().getMaxAltitude() / 200, ActivitySummaryEntries.UNIT_METERS);
            addSummaryData(ActivitySummaryEntries.ALTITUDE_MIN, summaryProto.getAltitude().getMinAltitude() / 200, ActivitySummaryEntries.UNIT_METERS);
            addSummaryData(ActivitySummaryEntries.ALTITUDE_AVG, summaryProto.getAltitude().getAvgAltitude() / 200, ActivitySummaryEntries.UNIT_METERS);
            // TODO totalClimbing
            addSummaryData(ActivitySummaryEntries.ELEVATION_GAIN, summaryProto.getAltitude().getElevationGain() / 100, ActivitySummaryEntries.UNIT_METERS);
            addSummaryData(ActivitySummaryEntries.ELEVATION_LOSS, summaryProto.getAltitude().getElevationLoss() / 100, ActivitySummaryEntries.UNIT_METERS);
        }

        if (summaryProto.hasElevation()) {
            addSummaryData(ActivitySummaryEntries.ASCENT_SECONDS, summaryProto.getElevation().getUphillTime(), ActivitySummaryEntries.UNIT_SECONDS);
            addSummaryData(ActivitySummaryEntries.DESCENT_SECONDS, summaryProto.getElevation().getDownhillTime(), ActivitySummaryEntries.UNIT_SECONDS);
        }

        if (summaryProto.hasSwimmingData()) {
            addSummaryData(ActivitySummaryEntries.LAPS, summaryProto.getSwimmingData().getLaps(), ActivitySummaryEntries.UNIT_LAPS);
            switch (summaryProto.getSwimmingData().getLaneLengthUnit()) {
                case 0:
                    addSummaryData(ActivitySummaryEntries.LANE_LENGTH, summaryProto.getSwimmingData().getLaneLength(), ActivitySummaryEntries.UNIT_METERS);
                    break;
                case 1:
                    addSummaryData(ActivitySummaryEntries.LANE_LENGTH, summaryProto.getSwimmingData().getLaneLength(), ActivitySummaryEntries.UNIT_YARD);
                    break;
            }
            switch (summaryProto.getSwimmingData().getStyle()) {
                // TODO i18n these
                case 1:
                    addSummaryData(ActivitySummaryEntries.SWIM_STYLE, "breaststroke");
                    break;
                case 2:
                    addSummaryData(ActivitySummaryEntries.SWIM_STYLE, "freestyle");
                    break;
            }
            addSummaryData(ActivitySummaryEntries.STROKES, summaryProto.getSwimmingData().getStrokes(), ActivitySummaryEntries.UNIT_STROKES);
            addSummaryData(ActivitySummaryEntries.STROKE_RATE_AVG, summaryProto.getSwimmingData().getAvgStrokeRate(), ActivitySummaryEntries.UNIT_STROKES_PER_MINUTE);
            addSummaryData(ActivitySummaryEntries.STROKE_RATE_MAX, summaryProto.getSwimmingData().getMaxStrokeRate(), ActivitySummaryEntries.UNIT_STROKES_PER_MINUTE);
            addSummaryData(ActivitySummaryEntries.STROKE_DISTANCE_AVG, summaryProto.getSwimmingData().getAvgDps(), ActivitySummaryEntries.UNIT_CM);
            addSummaryData(ActivitySummaryEntries.SWOLF_INDEX, summaryProto.getSwimmingData().getSwolf(), ActivitySummaryEntries.UNIT_NONE);
        }
    }
}
