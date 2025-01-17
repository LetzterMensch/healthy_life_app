/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, Taavi Eomäe

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
package com.example.gr.controller.externalevents;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.controller.device.DeviceManager;
import com.example.gr.utils.Prefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.gr.ControllerApplication;
public class BluetoothStateChangeReceiver extends BroadcastReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(BluetoothStateChangeReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {

                Intent refreshIntent = new Intent(DeviceManager.ACTION_REFRESH_DEVICELIST);
                LocalBroadcastManager.getInstance(context).sendBroadcast(refreshIntent);

                Prefs prefs = ControllerApplication.getPrefs();
                if (!prefs.getBoolean("general_autoconnectonbluetooth", false)) {
                    return;
                }

                LOG.info("Bluetooth turned on (ACTION_STATE_CHANGED) => connecting...");
                ControllerApplication.deviceService().connect();
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                LOG.info("Bluetooth turned off => disconnecting...");
                ControllerApplication.deviceService().disconnect();
            }
        }
    }
}
