/*  Copyright (C) 2018-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, José Rebelo, Oleg Vasilev, Sebastian Krey, Your Name

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

import android.text.format.DateUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.constant.ActivityKind;
import com.example.gr.database.DBHandler;
import com.example.gr.database.entities.BaseActivitySummary;
import com.example.gr.service.btle.BLETypeConversions;
import com.example.gr.service.btle.TransactionBuilder;
import com.example.gr.device.huami.AbstractHuamiActivityDetailsParser;
import com.example.gr.device.huami.HuamiActivityDetailsParser;
import com.example.gr.device.huami.HuamiSupport;
import com.example.gr.device.model.ActivityTrack;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.FileUtils;
import com.example.gr.utils.GB;


/**
 * An operation that fetches activity data. For every fetch, a new operation must
 * be created, i.e. an operation may not be reused for multiple fetches.
 */
public class FetchSportsDetailsOperation extends AbstractFetchOperation {
    private static final Logger LOG = LoggerFactory.getLogger(FetchSportsDetailsOperation.class);
    private final AbstractHuamiActivityDetailsParser detailsParser;
    private final BaseActivitySummary summary;
    private final String lastSyncTimeKey;

    FetchSportsDetailsOperation(@NonNull BaseActivitySummary summary,
                                @NonNull AbstractHuamiActivityDetailsParser detailsParser,
                                @NonNull HuamiSupport support,
                                @NonNull String lastSyncTimeKey,
                                int fetchCount) {
        super(support);
        setName("fetching sport details");
        this.summary = summary;
        this.detailsParser = detailsParser;
        this.lastSyncTimeKey = lastSyncTimeKey;
        this.fetchCount = fetchCount;
    }

    @Override
    protected String taskDescription() {
        return getContext().getString(R.string.busy_task_fetch_sports_details);
    }

    @Override
    protected void startFetching(TransactionBuilder builder) {
        LOG.info("start " + getName());
        final GregorianCalendar sinceWhen = getLastSuccessfulSyncTime();
        startFetching(builder, HuamiFetchDataType.SPORTS_DETAILS.getCode(), sinceWhen);
    }

    @Override
    protected boolean processBufferedData() {
        LOG.info("{} has finished round {}", getName(), fetchCount);

        if (buffer.size() == 0) {
            LOG.warn("Buffer is empty");
            return false;
        }

        if (detailsParser instanceof HuamiActivityDetailsParser) {
            ((HuamiActivityDetailsParser) detailsParser).setSkipCounterByte(false); // is already stripped
        }

        try {
            final ActivityTrack track = detailsParser.parse(buffer.toByteArray());
//            final ActivityTrackExporter exporter = createExporter();
            final String trackType;
            switch (summary.getActivityKind()) {
                case ActivityKind.TYPE_CYCLING:
                    trackType = getContext().getString(R.string.activity_type_biking);
                    break;
                case ActivityKind.TYPE_RUNNING:
                    trackType = getContext().getString(R.string.activity_type_running);
                    break;
                case ActivityKind.TYPE_WALKING:
                    trackType = getContext().getString(R.string.activity_type_walking);
                    break;
                case ActivityKind.TYPE_HIKING:
                    trackType = getContext().getString(R.string.activity_type_hiking);
                    break;
                case ActivityKind.TYPE_CLIMBING:
                    trackType = getContext().getString(R.string.activity_type_climbing);
                    break;
                case ActivityKind.TYPE_SWIMMING:
                    trackType = getContext().getString(R.string.activity_type_swimming);
                    break;
                default:
                    trackType = "track";
                    break;
            }

            final String rawBytesPath = saveRawBytes();

            final String fileName = FileUtils.makeValidFileName("gadgetbridge-" + trackType.toLowerCase() + "-" + DateTimeUtils.formatIso8601(summary.getStartTime()) + ".gpx");
            final File targetFile = new File(FileUtils.getExternalFilesDir(), fileName);

            boolean exportGpxSuccess = true;
//            try {
//                exporter.performExport(track, targetFile);
//            } catch (final ActivityTrackExporter.GPXTrackEmptyException ex) {
//                exportGpxSuccess = false;
//            }

            try (DBHandler dbHandler = ControllerApplication.acquireDB()) {
                if (exportGpxSuccess) {
                    summary.setGpxTrack(targetFile.getAbsolutePath());
                }
                if (rawBytesPath != null) {
                    summary.setRawDetailsPath(rawBytesPath);
                }
                dbHandler.getDaoSession().getBaseActivitySummaryDao().update(summary);
            }
        } catch (final Exception e) {
            GB.toast(getContext(), "Error saving activity details: " + e.getMessage(), Toast.LENGTH_LONG, GB.ERROR, e);
            return false;
        }

        // Always increment the sync timestamp on success, even if we did not get data
        final GregorianCalendar endTime = BLETypeConversions.createCalendar();
        endTime.setTime(summary.getEndTime());
        saveLastSyncTimestamp(endTime);

        if (needsAnotherFetch(endTime)) {
            final FetchSportsSummaryOperation nextOperation = new FetchSportsSummaryOperation(getSupport(), fetchCount);
            getSupport().getFetchOperationQueue().add(0, nextOperation);
        }

        return true;
    }

    private boolean needsAnotherFetch(GregorianCalendar lastSyncTimestamp) {
        // We have 2 operations per fetch round: summary + details
        if (fetchCount > 10) {
            LOG.warn("Already have 5 fetch rounds, not doing another one.");
            return false;
        }

        if (DateUtils.isToday(lastSyncTimestamp.getTimeInMillis())) {
            LOG.info("Hopefully no further fetch needed, last synced timestamp is from today.");
            return false;
        }

        if (lastSyncTimestamp.getTimeInMillis() > System.currentTimeMillis()) {
            LOG.warn("Not doing another fetch since last synced timestamp is in the future: {}", DateTimeUtils.formatDateTime(lastSyncTimestamp.getTime()));
            return false;
        }

        LOG.info("Doing another fetch since last sync timestamp is still too old: {}", DateTimeUtils.formatDateTime(lastSyncTimestamp.getTime()));
        return true;
    }

//    private ActivityTrackExporter createExporter() {
//        final GPXExporter exporter = new GPXExporter();
//        exporter.setCreator(ControllerApplication.app().getNameAndVersion());
//        return exporter;
//    }

    @Override
    protected String getLastSyncTimeKey() {
        return lastSyncTimeKey;
    }

    @Override
    protected GregorianCalendar getLastSuccessfulSyncTime() {
        final GregorianCalendar calendar = BLETypeConversions.createCalendar();
        calendar.setTime(summary.getStartTime());
        return calendar;
    }

    private String saveRawBytes() {
        final String fileName = FileUtils.makeValidFileName(String.format("%s.bin", DateTimeUtils.formatIso8601(summary.getStartTime())));
        FileOutputStream outputStream = null;

        try {
            final File targetFolder = new File(FileUtils.getExternalFilesDir(), "rawDetails");
            targetFolder.mkdirs();
            final File targetFile = new File(targetFolder, fileName);
            outputStream = new FileOutputStream(targetFile);
            outputStream.write(buffer.toByteArray());
            outputStream.close();
            return targetFile.getAbsolutePath();
        } catch (final IOException e) {
            LOG.error("Failed to save raw bytes", e);
        }

        return null;
    }
}
