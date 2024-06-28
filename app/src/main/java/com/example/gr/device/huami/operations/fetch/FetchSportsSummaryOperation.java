/*  Copyright (C) 2018-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniel
    Dakhno, Daniele Gobbetti, José Rebelo, Oleg Vasilev, Petr Vaněk

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

import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.constant.ActivityKind;
import com.example.gr.data.ActivitySummaryJsonSummary;
import com.example.gr.data.parser.ActivitySummaryParser;
import com.example.gr.database.DBHandler;
import com.example.gr.database.DBHelper;
import com.example.gr.database.LocalDatabase;
import com.example.gr.database.entities.BaseActivitySummary;
import com.example.gr.database.entities.BaseActivitySummaryDao;
import com.example.gr.database.entities.DaoSession;
import com.example.gr.database.entities.Device;
import com.example.gr.database.entities.User;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.service.btle.TransactionBuilder;
import com.example.gr.device.huami.AbstractHuamiActivityDetailsParser;
import com.example.gr.device.huami.HuamiActivitySummaryParser;
import com.example.gr.device.huami.HuamiSupport;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.GB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * An operation that fetches activity data. For every fetch, a new operation must
 * be created, i.e. an operation may not be reused for multiple fetches.
 */
public class FetchSportsSummaryOperation extends AbstractFetchOperation {
    private static final Logger LOG = LoggerFactory.getLogger(FetchSportsSummaryOperation.class);

    public FetchSportsSummaryOperation(HuamiSupport support, int fetchCount) {
        super(support);
        setName("fetching sport summaries");
        this.fetchCount = fetchCount;
    }

    @Override
    protected String taskDescription() {
        return getContext().getString(R.string.busy_task_fetch_sports_summaries);
    }

    @Override
    protected void startFetching(TransactionBuilder builder) {
        LOG.info("start" + getName());
        final GregorianCalendar sinceWhen = getLastSuccessfulSyncTime();
        startFetching(builder, HuamiFetchDataType.SPORTS_SUMMARIES.getCode(), sinceWhen);
    }

    @Override
    protected boolean processBufferedData() {
        System.out.println("insde process Buffered Data");
        LOG.info("{} has finished round {}", getName(), fetchCount);

        if (buffer.size() < 2) {
            LOG.warn("Buffer size {} too small for activity summary", buffer.size());
            return false;
        }

        final DeviceCoordinator coordinator = getDevice().getDeviceCoordinator();
        final ActivitySummaryParser summaryParser = coordinator.getActivitySummaryParser(getDevice());

        BaseActivitySummary summary = new BaseActivitySummary();
        summary.setStartTime(getLastStartTimestamp().getTime()); // due to a bug this has to be set
        summary.setRawSummaryData(buffer.toByteArray());
        try {
            summary = summaryParser.parseBinaryData(summary);
        } catch (final Exception e) {
            GB.toast(getContext(), "Failed to parse activity summary", Toast.LENGTH_LONG, GB.ERROR, e);
            return false;
        }
        if (summary == null) {
            return false;
        }
        summary.setSummaryData(null); // remove json before saving to database,
        try (DBHandler dbHandler = ControllerApplication.acquireDB()) {
            final DaoSession session = dbHandler.getDaoSession();
            final Device device = DBHelper.getDevice(getDevice(), session);
            final User user = DBHelper.getUser(session);
            summary.setDevice(device);
            summary.setUser(user);
            summary.setRawSummaryData(buffer.toByteArray());
            //Check if the record is already existed
//            QueryBuilder<BaseActivitySummary> qb = session.getBaseActivitySummaryDao().queryBuilder();
//            qb.where(BaseActivitySummaryDao.Properties.StartTime.eq(summary.getStartTime()));
//            if(qb.build() != null) return true;
            long timestamp = summary.getEndTime().getTime() + 10000;

            SharedPreferences sharedPreferences = ControllerApplication.getDeviceSpecificSharedPrefs(getDevice().getAddress());
            long lastTimeStamp = sharedPreferences.getLong("lastSportsActivityTimeMillis",0);
            if(lastTimeStamp < timestamp){
                session.getBaseActivitySummaryDao().insertOrReplace(summary);
                SharedPreferences.Editor editor = ControllerApplication.getDeviceSpecificSharedPrefs(getDevice().getAddress()).edit();
                editor.remove("lastSportsActivityTimeMillis"); //FIXME: key reconstruction is BAD
                editor.putLong("lastSportsActivityTimeMillis", timestamp);
                editor.apply();
            }
        } catch (final Exception ex) {
            GB.toast(getContext(), "Error saving activity summary", Toast.LENGTH_LONG, GB.ERROR, ex);
            return false;
        }

        final AbstractHuamiActivityDetailsParser detailsParser = ((HuamiActivitySummaryParser) summaryParser).getDetailsParser(summary);
        final FetchSportsDetailsOperation nextOperation = new FetchSportsDetailsOperation(summary, detailsParser, getSupport(), getLastSyncTimeKey(), fetchCount);
        getSupport().getFetchOperationQueue().add(0, nextOperation);

        return true;
    }

    @Override
    protected String getLastSyncTimeKey() {
        return "lastSportsActivityTimeMillis";
    }
}
