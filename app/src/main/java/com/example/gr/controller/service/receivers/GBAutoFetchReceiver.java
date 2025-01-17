/*  Copyright (C) 2018-2024 Daniele Gobbetti, José Rebelo, Martin

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
package com.example.gr.controller.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

import com.example.gr.ControllerApplication;
import com.example.gr.controller.device.model.RecordedDataTypes;


public class GBAutoFetchReceiver extends BroadcastReceiver {
    private Date lastSync = new Date();

    @Override
    public void onReceive(Context context, Intent intent) {
        Date nextSync = DateUtils.addMinutes(lastSync, ControllerApplication.getPrefs().getInt("auto_fetch_interval_limit", 0));
        if (nextSync.before(new Date())) {
            ControllerApplication.deviceService().onFetchRecordedData(RecordedDataTypes.TYPE_SYNC);
            lastSync = new Date();
        }
    }
}
