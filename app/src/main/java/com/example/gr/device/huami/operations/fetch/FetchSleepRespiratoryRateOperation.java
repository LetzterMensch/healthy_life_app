/*  Copyright (C) 2023-2024 Daniel Dakhno, José Rebelo

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
package com.example.gr.device.huami.operations.fetch;

import android.widget.Toast;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.database.DBHandler;
import com.example.gr.database.DBHelper;
import com.example.gr.database.entities.DaoSession;
import com.example.gr.database.entities.Device;
import com.example.gr.database.entities.HuamiSleepRespiratoryRateSample;
import com.example.gr.database.entities.User;
import com.example.gr.device.huami.HuamiCoordinator;
import com.example.gr.device.huami.HuamiSleepRespiratoryRateSampleProvider;
import com.example.gr.device.huami.HuamiSupport;
import com.example.gr.utils.GB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * An operation that fetches sleep respiratory rate data.
 */
public class FetchSleepRespiratoryRateOperation extends AbstractRepeatingFetchOperation {
    private static final Logger LOG = LoggerFactory.getLogger(FetchSleepRespiratoryRateOperation.class);

    public FetchSleepRespiratoryRateOperation(final HuamiSupport support) {
        super(support, HuamiFetchDataType.SLEEP_RESPIRATORY_RATE);
    }

    @Override
    protected String taskDescription() {
        return getContext().getString(R.string.busy_task_fetch_sleep_respiratory_rate_data);
    }

    @Override
    protected boolean handleActivityData(final GregorianCalendar timestamp, final byte[] bytes) {
        if (bytes.length % 8 != 0) {
            LOG.error("Unexpected length for sleep respiratory rate data {}, not divisible by 8", bytes.length);
            return false;
        }

        final List<HuamiSleepRespiratoryRateSample> samples = new ArrayList<>();

        final ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        while (buf.position() < bytes.length) {
            final long timestampSeconds = buf.getInt();
            final byte utcOffsetInQuarterHours = buf.get();
            final int respiratoryRate = buf.get() & 0xff;
            final byte unknown1 = buf.get(); // always 0?
            final byte unknown2 = buf.get(); // mostly 1, sometimes 2, 4 when waking up?

            timestamp.setTimeInMillis(timestampSeconds * 1000L);

            LOG.trace("Sleep Respiratory Rate at {} + {}: respiratoryRate={} unknown1={} unknown2={}", timestamp.getTime(), respiratoryRate, utcOffsetInQuarterHours, unknown1, unknown2);
            final HuamiSleepRespiratoryRateSample sample = new HuamiSleepRespiratoryRateSample();
            sample.setTimestamp(timestamp.getTimeInMillis());
            sample.setUtcOffset(utcOffsetInQuarterHours * 900000);
            sample.setRate(respiratoryRate);
            samples.add(sample);
        }

        return persistSamples(samples);
    }

    protected boolean persistSamples(final List<HuamiSleepRespiratoryRateSample> samples) {
        try (DBHandler handler = ControllerApplication.acquireDB()) {
            final DaoSession session = handler.getDaoSession();

            final Device device = DBHelper.getDevice(getDevice(), session);
            final User user = DBHelper.getUser(session);

            final HuamiCoordinator coordinator = (HuamiCoordinator) getDevice().getDeviceCoordinator();
            final HuamiSleepRespiratoryRateSampleProvider sampleProvider = coordinator.getSleepRespiratoryRateSampleProvider(getDevice(), session);

            for (final HuamiSleepRespiratoryRateSample sample : samples) {
                sample.setDevice(device);
                sample.setUser(user);
            }

            LOG.debug("Will persist {} sleep respiratory rate samples", samples.size());
            sampleProvider.addSamples(samples);
        } catch (final Exception e) {
            GB.toast(getContext(), "Error saving sleep respiratory rate samples", Toast.LENGTH_LONG, GB.ERROR, e);
            return false;
        }

        return true;
    }

    @Override
    protected String getLastSyncTimeKey() {
        return "lastSleepRespiratoryRateTimeMillis";
    }
}
