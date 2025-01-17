/*  Copyright (C) 2015-2024 Carsten Pfeiffer

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
package com.example.gr.controller.service.btle.actions;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import com.example.gr.controller.device.GBDevice;


public class SetDeviceBusyAction extends PlainAction {
    private final GBDevice device;
    private final Context context;
    private final String busyTask;

    /**
     * When run, will mark the device as busy (or not busy).
     *
     * @param device   the device to mark
     * @param busyTask the task name to set as busy task, or null to mark as not busy
     * @param context
     */
    public SetDeviceBusyAction(GBDevice device, String busyTask, Context context) {
        this.device = device;
        this.busyTask = busyTask;
        this.context = context;
    }

    @Override
    public boolean run(BluetoothGatt gatt) {
        device.setBusyTask(busyTask);
        device.sendDeviceUpdateIntent(context);
        return true;
    }

    @Override
    public String toString() {
        return getCreationTime() + ": " + getClass().getName() + ": " + busyTask;
    }
}
