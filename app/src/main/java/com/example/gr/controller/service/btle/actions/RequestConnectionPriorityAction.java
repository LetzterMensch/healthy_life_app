/*  Copyright (C) 2023-2024 José Rebelo

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

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;

import com.example.gr.controller.service.btle.BtLEAction;

public class RequestConnectionPriorityAction extends BtLEAction {
    private int priority;

    public RequestConnectionPriorityAction(int priority) {
        super(null);
        this.priority = priority;
    }

    @Override
    public boolean expectsResult() {
        return true;
    }

    @Override
    @SuppressLint("MissingPermission")
    public boolean run(final BluetoothGatt gatt) {
        return gatt.requestConnectionPriority(priority);
    }
}
