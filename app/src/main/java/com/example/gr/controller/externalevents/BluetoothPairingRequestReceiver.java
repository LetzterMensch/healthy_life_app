/*  Copyright (C) 2017-2024 Andreas Shimokawa, Daniel Dakhno, Daniele Gobbetti,
    João Paulo Barraca, Taavi Eomäe

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

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gr.controller.device.DeviceCoordinator;
import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.service.DeviceCommunicationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BluetoothPairingRequestReceiver extends BroadcastReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(BluetoothConnectReceiver.class);

    final DeviceCommunicationService service;

    public BluetoothPairingRequestReceiver(DeviceCommunicationService service) {
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (!action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
            return;
        }
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (device == null) {
            return;
        }

        GBDevice gbDevice = null;
        try {
            gbDevice = service.getDeviceByAddress(device.getAddress());

            DeviceCoordinator coordinator = gbDevice.getDeviceCoordinator();
            try {
                if (coordinator.getBondingStyle() == DeviceCoordinator.BONDING_STYLE_NONE) {
                    LOG.info("Aborting unwanted pairing request");
                    abortBroadcast();
                }
            } catch (Exception e) {
                LOG.warn("Could not abort pairing request process");
            }
        } catch (DeviceCommunicationService.DeviceNotFoundException e) {
            e.printStackTrace();
        }
    }
}