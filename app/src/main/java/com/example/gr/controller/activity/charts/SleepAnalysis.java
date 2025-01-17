/*  Copyright (C) 2019-2024 José Rebelo, Petr Vaněk, Q-er

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
package com.example.gr.controller.activity.charts;

import com.example.gr.utils.constant.ActivityKind;
import com.example.gr.model.data.sample.ActivitySample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SleepAnalysis {

    public static final long MIN_SESSION_LENGTH = 5 * 60;
    public static final long MAX_WAKE_PHASE_LENGTH = 2 * 60 * 60;

    public List<SleepSession> calculateSleepSessions(List<? extends ActivitySample> samples) {
        List<SleepSession> result = new ArrayList<>();

        ActivitySample previousSample = null;
        Date sleepStart = null;
        Date sleepEnd = null;
        long lightSleepDuration = 0;
        long deepSleepDuration = 0;
        long remSleepDuration = 0;
        long durationSinceLastSleep = 0;

        for (ActivitySample sample : samples) {
            if (isSleep(sample)) {
                if (sleepStart == null)
                    sleepStart = getDateFromSample(sample);
                sleepEnd = getDateFromSample(sample);

                durationSinceLastSleep = 0;
            } else {
                //exclude "not worn" times from sleep sessions as this makes a discrepancy with the charts
                if (lightSleepDuration + deepSleepDuration + remSleepDuration > MIN_SESSION_LENGTH)
                    result.add(new SleepSession(sleepStart, sleepEnd, lightSleepDuration, deepSleepDuration, remSleepDuration));
                sleepStart = null;
                sleepEnd = null;
                lightSleepDuration = 0;
                deepSleepDuration = 0;
                remSleepDuration = 0;
            }

            if (previousSample != null) {
                long durationSinceLastSample = sample.getTimestamp() - previousSample.getTimestamp();
                if (sample.getKind() == ActivityKind.TYPE_LIGHT_SLEEP) {
                    lightSleepDuration += durationSinceLastSample;
                } else if (sample.getKind() == ActivityKind.TYPE_DEEP_SLEEP) {
                    deepSleepDuration += durationSinceLastSample;
                } else if (sample.getKind() == ActivityKind.TYPE_REM_SLEEP) {
                    remSleepDuration += durationSinceLastSample;
                } else {
                    durationSinceLastSleep += durationSinceLastSample;
                    if (sleepStart != null && durationSinceLastSleep > MAX_WAKE_PHASE_LENGTH) {
                        if (lightSleepDuration + deepSleepDuration + remSleepDuration > MIN_SESSION_LENGTH)
                            result.add(new SleepSession(sleepStart, sleepEnd, lightSleepDuration, deepSleepDuration, remSleepDuration));
                        sleepStart = null;
                        sleepEnd = null;
                        lightSleepDuration = 0;
                        deepSleepDuration = 0;
                        remSleepDuration = 0;
                    }
                }
            }

            previousSample = sample;
        }
        if (lightSleepDuration + deepSleepDuration + remSleepDuration > MIN_SESSION_LENGTH) {
            result.add(new SleepSession(sleepStart, sleepEnd, lightSleepDuration, deepSleepDuration, remSleepDuration));
        }
        return result;
    }

    private boolean isSleep(ActivitySample sample) {
        return sample.getKind() == ActivityKind.TYPE_DEEP_SLEEP ||
                sample.getKind() == ActivityKind.TYPE_LIGHT_SLEEP ||
                sample.getKind() == ActivityKind.TYPE_REM_SLEEP;
    }

    private Date getDateFromSample(ActivitySample sample) {
        return new Date(sample.getTimestamp() * 1000L);
    }


    public static class SleepSession {
        private final Date sleepStart;
        private final Date sleepEnd;
        private final long lightSleepDuration;
        private final long deepSleepDuration;
        private final long remSleepDuration;

        private SleepSession(Date sleepStart,
                             Date sleepEnd,
                             long lightSleepDuration,
                             long deepSleepDuration,
                             long remSleepDuration) {
            this.sleepStart = sleepStart;
            this.sleepEnd = sleepEnd;
            this.lightSleepDuration = lightSleepDuration;
            this.deepSleepDuration = deepSleepDuration;
            this.remSleepDuration = remSleepDuration;
        }

        public Date getSleepStart() {
            return sleepStart;
        }

        public Date getSleepEnd() {
            return sleepEnd;
        }

        public long getLightSleepDuration() {
            return lightSleepDuration;
        }

        public long getDeepSleepDuration() {
            return deepSleepDuration;
        }

        public long getRemSleepDuration() {
            return remSleepDuration;
        }
    }
}
