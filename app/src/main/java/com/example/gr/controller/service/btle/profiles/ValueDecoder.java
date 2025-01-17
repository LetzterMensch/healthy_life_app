/*  Copyright (C) 2016-2024 Alicia Hormann, Carsten Pfeiffer

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
package com.example.gr.controller.service.btle.profiles;

import android.bluetooth.BluetoothGattCharacteristic;

import com.example.gr.controller.service.btle.GattCharacteristic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValueDecoder {
    private static final Logger LOG = LoggerFactory.getLogger(ValueDecoder.class);

    public static int decodeInt(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
    }

    public static int decodePercent(BluetoothGattCharacteristic characteristic) {
        int percent = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        if (percent > 100 || percent < 0) {
            LOG.warn("Unexpected percent value: " + percent + ": " + GattCharacteristic.toString(characteristic));
            percent = Math.min(100, Math.max(0, percent));
        }
        return percent;
    }
}
