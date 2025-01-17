/*  Copyright (C) 2018-2024 Andreas Shimokawa, Daniele Gobbetti, Dmitry Markin

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
package com.example.gr.controller.device.huami.miband;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.annotation.Nullable;

import com.example.gr.controller.device.SimpleNotification;
import com.example.gr.controller.service.btle.BtLEAction;
import com.example.gr.controller.service.btle.GattCharacteristic;
import com.example.gr.controller.service.btle.TransactionBuilder;
import com.example.gr.controller.service.btle.profiles.alertnotification.AlertCategory;
import com.example.gr.controller.device.huami.HuamiSupport;


public class Mi2NotificationStrategy extends V2NotificationStrategy<HuamiSupport> {

    private final BluetoothGattCharacteristic alertLevelCharacteristic;

    public Mi2NotificationStrategy(HuamiSupport support) {
        super(support);
        alertLevelCharacteristic = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_ALERT_LEVEL);
    }

    @Override
    protected void sendCustomNotification(VibrationProfile vibrationProfile, SimpleNotification simpleNotification, BtLEAction extraAction, TransactionBuilder builder) {
        startNotify(builder, vibrationProfile.getAlertLevel(), simpleNotification);
        BluetoothGattCharacteristic alert = getSupport().getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_ALERT_LEVEL);
        byte repeat = (byte) (vibrationProfile.getRepeat() * (vibrationProfile.getOnOffSequence().length / 2));
        int waitDuration = 0;
        if (repeat > 0) {
            short vibration = (short) vibrationProfile.getOnOffSequence()[0];
            short pause = (short) vibrationProfile.getOnOffSequence()[1];
            waitDuration = (vibration + pause) * repeat;
            builder.write(alert, new byte[]{-1, (byte) (vibration & 255), (byte) (vibration >> 8 & 255), (byte) (pause & 255), (byte) (pause >> 8 & 255), repeat});
        }

        // Don't wait during an incoming call, otherwise we'll not be able to stop the call notification
        if (simpleNotification == null || simpleNotification.getAlertCategory() != AlertCategory.IncomingCall) {
            waitDuration = Math.max(waitDuration, 4000);
            builder.wait(waitDuration);
        }

        if (extraAction != null) {
            builder.add(extraAction);
        }
    }

    protected void startNotify(TransactionBuilder builder, int alertLevel, @Nullable SimpleNotification simpleNotification) {
        builder.write(alertLevelCharacteristic, new byte[] {(byte) alertLevel});

    }

    protected void stopNotify(TransactionBuilder builder) {
        builder.write(alertLevelCharacteristic, new byte[]{GattCharacteristic.NO_ALERT});
    }

    @Override
    public void sendCustomNotification(VibrationProfile vibrationProfile, @Nullable SimpleNotification simpleNotification, int flashTimes, int flashColour, int originalColour, long flashDuration, BtLEAction extraAction, TransactionBuilder builder) {
        // all other parameters are unfortunately not supported anymore ;-(
        sendCustomNotification(vibrationProfile, simpleNotification, extraAction, builder);
    }
}
