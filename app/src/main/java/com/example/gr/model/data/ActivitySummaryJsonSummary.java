/*  Copyright (C) 2020-2024 José Rebelo, Petr Vaněk, Reiner Herrmann,
    Sebastian Krey

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
package com.example.gr.model.data;

import static com.example.gr.controller.device.model.ActivitySummaryEntries.ACTIVE_SECONDS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ALTITUDE_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ALTITUDE_BASE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ALTITUDE_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ALTITUDE_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ASCENT_DISTANCE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ASCENT_METERS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ASCENT_SECONDS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.CADENCE_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.CADENCE_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.CADENCE_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.CALORIES_BURNT;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.DESCENT_DISTANCE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.DESCENT_METERS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.DESCENT_SECONDS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.DISTANCE_METERS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ELEVATION_GAIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.ELEVATION_LOSS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.FLAT_DISTANCE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.FLAT_SECONDS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_AEROBIC;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_ANAEROBIC;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_EXTREME;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_FAT_BURN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_NA;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.HR_ZONE_WARM_UP;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.LANE_LENGTH;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.LAPS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.LAP_PACE_AVERAGE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.MAXIMUM_OXYGEN_UPTAKE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.PACE_AVG_SECONDS_KM;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.PACE_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.PACE_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.SPEED_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.SPEED_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.SPEED_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STEPS;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STRIDE_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STRIDE_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STRIDE_MIN;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STRIDE_TOTAL;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STROKES;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STROKE_AVG_PER_SECOND;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STROKE_DISTANCE_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STROKE_RATE_AVG;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.STROKE_RATE_MAX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.SWIM_STYLE;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.SWOLF_INDEX;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.TRAINING_EFFECT_AEROBIC;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.TRAINING_EFFECT_ANAEROBIC;
import static com.example.gr.controller.device.model.ActivitySummaryEntries.WORKOUT_LOAD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.gr.model.data.parser.ActivitySummaryParser;
import com.example.gr.model.database.entities.BaseActivitySummary;

public class ActivitySummaryJsonSummary {
    private static final Logger LOG = LoggerFactory.getLogger(ActivitySummaryJsonSummary.class);
    private JSONObject groupData;
    private JSONObject summaryData;
    private JSONObject summaryGroupedList;
    private ActivitySummaryParser summaryParser;
    private BaseActivitySummary baseActivitySummary;

    public ActivitySummaryJsonSummary(final ActivitySummaryParser summaryParser, BaseActivitySummary baseActivitySummary){
        this.summaryParser=summaryParser;
        this.baseActivitySummary=baseActivitySummary;
    }

    private JSONObject setSummaryData(BaseActivitySummary item){
        String summary = getCorrectSummary(item);
        JSONObject jsonSummary = getJSONSummary(summary);
        if (jsonSummary != null) {
            //add additionally computed values here

            if (item.getBaseAltitude() != null && item.getBaseAltitude() != -20000) {
                JSONObject baseAltitudeValues;
                try {
                    baseAltitudeValues = new JSONObject();
                    baseAltitudeValues.put("value", item.getBaseAltitude());
                    baseAltitudeValues.put("unit", "meters");
                    jsonSummary.put("baseAltitude", baseAltitudeValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonSummary.has("distanceMeters") && jsonSummary.has("activeSeconds")) {
                JSONObject averageSpeed;
                try {
                    JSONObject distanceMeters = (JSONObject) jsonSummary.get("distanceMeters");
                    JSONObject activeSeconds = (JSONObject) jsonSummary.get("activeSeconds");
                    double distance = distanceMeters.getDouble("value");
                    double duration = activeSeconds.getDouble("value");
                    averageSpeed = new JSONObject();
                    averageSpeed.put("value", distance / duration);
                    averageSpeed.put("unit", "meters_second");
                    jsonSummary.put("averageSpeed", averageSpeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonSummary;
    }

    public JSONObject getSummaryData(){
        //returns json with summaryData
        if (summaryData==null) summaryData=setSummaryData(baseActivitySummary);
        return summaryData;
    }

    private String getCorrectSummary(BaseActivitySummary item){
        if (item.getRawSummaryData() != null) {
            try {
                item = summaryParser.parseBinaryData(item);
            } catch (final Exception e) {
                LOG.error("Failed to re-parse corrected summary", e);
            }
        }
        return item.getSummaryData();
    }

    private JSONObject getJSONSummary(String sumData){
        JSONObject summarySubdata = null;
        if (sumData != null) {
            try {
                summarySubdata = new JSONObject(sumData);
            } catch (JSONException e) {
            }
        }
        return summarySubdata;
    }

    public JSONObject getSummaryGroupedList() {
        //returns list grouped by activity groups as per createActivitySummaryGroups
        if (summaryData==null) summaryData=setSummaryData(baseActivitySummary);
        if (summaryGroupedList==null) summaryGroupedList=setSummaryGroupedList(summaryData);
        return summaryGroupedList;
    }
    private JSONObject setSummaryGroupedList(JSONObject summaryDatalist){
        this.groupData = createActivitySummaryGroups(); //structure for grouping activities into groups, when vizualizing

        if (summaryDatalist == null) return null;
        Iterator<String> keys = summaryDatalist.keys();
        Map<String, JSONArray> activeGroups = new HashMap<>();

        while (keys.hasNext()) {
            String key = keys.next();
            try {
                JSONObject innerData = (JSONObject) summaryDatalist.get(key);
                Object value = innerData.get("value");
                String unit = innerData.getString("unit");
                String groupName = getGroup(key);

                JSONArray group = activeGroups.get(groupName);
                if (group == null) {
                    group = new JSONArray();
                    activeGroups.put(groupName, group);
                }

                JSONObject item = new JSONObject();
                item.put("name", key);
                item.put("value", value);
                item.put("unit", unit);
                group.put(item);
            } catch (JSONException e) {
                LOG.error("SportsActivity internal error building grouped summary", e);
            }
        }

        // Reorder collected groups according to the order set by this.groupData.
        JSONObject grouped = new JSONObject();
        Iterator<String> names = this.groupData.keys();
        while(names.hasNext()) {
            String groupName = names.next();
            JSONArray group = activeGroups.get(groupName);
            if (group != null) {
                try {
                    grouped.put(groupName, group);
                } catch (JSONException e) {
                    LOG.error("SportsActivity internal error building grouped summary", e);
                }
            }
        }
        return grouped;
    }

    private String getGroup(String searchItem) {
        // NB: Default group must be present in group JSONObject created by createActivitySummaryGroups
        String defaultGroup = "Activity";
        if (groupData == null) return defaultGroup;
        Iterator<String> keys = groupData.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                JSONArray itemList = (JSONArray) groupData.get(key);
                for (int i = 0; i < itemList.length(); i++) {
                    if (itemList.getString(i).equals(searchItem)) {
                        return key;
                    }
                }
            } catch (JSONException e) {
                LOG.error("SportsActivity", e);
            }
        }
        return defaultGroup;
    }
    private JSONObject createActivitySummaryGroups(){
        final Map<String, List<String>> groupDefinitions = new LinkedHashMap<String, List<String>>() {{
            // NB: Default group Activity must be present in this definition, otherwise it wouldn't
            // be shown.
            put("Activity", Arrays.asList(
                    DISTANCE_METERS, STEPS, ACTIVE_SECONDS, CALORIES_BURNT, STRIDE_TOTAL,
                    HR_AVG, HR_MAX, HR_MIN, STRIDE_AVG, STRIDE_MAX, STRIDE_MIN
            ));
            put("Speed", Arrays.asList(
                    SPEED_AVG, SPEED_MAX, SPEED_MIN, PACE_AVG_SECONDS_KM, PACE_MIN,
                    PACE_MAX, "averageSpeed2", CADENCE_AVG, CADENCE_MAX, CADENCE_MIN
            ));
            put("Elevation", Arrays.asList(
                    ASCENT_METERS, DESCENT_METERS, ALTITUDE_MAX, ALTITUDE_MIN, ALTITUDE_AVG,
                    ALTITUDE_BASE, ASCENT_SECONDS, DESCENT_SECONDS, FLAT_SECONDS, ASCENT_DISTANCE,
                    DESCENT_DISTANCE, FLAT_DISTANCE, ELEVATION_GAIN, ELEVATION_LOSS
            ));
            put("HeartRateZones", Arrays.asList(
                    HR_ZONE_NA, HR_ZONE_WARM_UP, HR_ZONE_FAT_BURN, HR_ZONE_AEROBIC, HR_ZONE_ANAEROBIC,
                    HR_ZONE_EXTREME
            ));
            put("Strokes", Arrays.asList(
                    STROKE_DISTANCE_AVG, STROKE_AVG_PER_SECOND, STROKES,
                    STROKE_RATE_AVG, STROKE_RATE_MAX
            ));
            put("Swimming", Arrays.asList(
                    SWOLF_INDEX, SWIM_STYLE
            ));
            put("TrainingEffect", Arrays.asList(
                    TRAINING_EFFECT_AEROBIC, TRAINING_EFFECT_ANAEROBIC, WORKOUT_LOAD,
                    MAXIMUM_OXYGEN_UPTAKE
            ));
            put("laps", Arrays.asList(
                    LAP_PACE_AVERAGE, LAPS, LANE_LENGTH
            ));
        }};

        return new JSONObject(groupDefinitions);
    }
}
