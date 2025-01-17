/*  Copyright (C) 2015-2024 Carsten Pfeiffer, Daniele Gobbetti

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
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;

import com.example.gr.controller.service.btle.BtLEAction;


/**
 * Invokes a read operation on a given GATT characteristic.
 * The result will be made available asynchronously through the
 * {@link BluetoothGattCallback}
 */
public class ReadAction extends BtLEAction {

    public ReadAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean run(BluetoothGatt gatt) {
        int properties = getCharacteristic().getProperties();
        if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            return gatt.readCharacteristic(getCharacteristic());
        }
        return false;
    }

    @Override
    public boolean expectsResult() {
        return true;
    }
}
