/*  Copyright (C) 2018-2024 Andreas Shimokawa, Daniele Gobbetti

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

import androidx.annotation.NonNull;

import com.example.gr.controller.device.SimpleNotification;
import com.example.gr.controller.service.btle.BLETypeConversions;
import com.example.gr.controller.service.btle.BtLEAction;
import com.example.gr.controller.service.btle.GattCharacteristic;
import com.example.gr.controller.service.btle.TransactionBuilder;
import com.example.gr.controller.service.btle.profiles.alertnotification.AlertCategory;
import com.example.gr.controller.service.btle.profiles.alertnotification.AlertNotificationProfile;
import com.example.gr.controller.service.btle.profiles.alertnotification.NewAlert;
import com.example.gr.controller.service.btle.profiles.alertnotification.OverflowStrategy;
import com.example.gr.controller.device.huami.HuamiIcon;
import com.example.gr.controller.device.huami.HuamiSupport;
import com.example.gr.utils.StringUtils;

public class Mi2TextNotificationStrategy extends Mi2NotificationStrategy {
    private final BluetoothGattCharacteristic newAlertCharacteristic;

    public Mi2TextNotificationStrategy(HuamiSupport support) {
        super(support);
        newAlertCharacteristic = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_NEW_ALERT);
    }

    @Override
    protected void sendCustomNotification(VibrationProfile vibrationProfile, SimpleNotification simpleNotification, BtLEAction extraAction, TransactionBuilder builder) {
        if (simpleNotification != null && simpleNotification.getAlertCategory() == AlertCategory.IncomingCall) {
            // incoming calls are notified solely via NewAlert including caller ID
            sendAlert(simpleNotification, builder);
            return;
        }

        // announce text messages with configured alerts first
        super.sendCustomNotification(vibrationProfile, simpleNotification, extraAction, builder);
        // and finally send the text message, if any
        if (simpleNotification != null && !StringUtils.isEmpty(simpleNotification.getMessage())) {
            sendAlert(simpleNotification, builder);
        }
    }

    @Override
    protected void startNotify(TransactionBuilder builder, int alertLevel, SimpleNotification simpleNotification) {
        builder.write(newAlertCharacteristic, getNotifyMessage(simpleNotification));
    }

    protected byte[] getNotifyMessage(SimpleNotification simpleNotification) {
        int numAlerts = 1;
        if (simpleNotification != null && simpleNotification.getNotificationType() != null && simpleNotification.getAlertCategory() != AlertCategory.SMS) {
            byte customIconId = HuamiIcon.mapToIconId(simpleNotification.getNotificationType());
            if (customIconId == HuamiIcon.EMAIL) {
                // unfortunately. the email icon breaks the notification, fall back to a standard AlertCategory
                return new byte[]{BLETypeConversions.fromUint8(AlertCategory.Email.getId()), BLETypeConversions.fromUint8(numAlerts)};
            }
            return new byte[]{BLETypeConversions.fromUint8(AlertCategory.CustomHuami.getId()), BLETypeConversions.fromUint8(numAlerts), customIconId};
        }
        return new byte[] { BLETypeConversions.fromUint8(AlertCategory.SMS.getId()), BLETypeConversions.fromUint8(numAlerts)};
    }

    protected void sendAlert(@NonNull SimpleNotification simpleNotification, TransactionBuilder builder) {
        AlertNotificationProfile<?> profile = new AlertNotificationProfile<>(getSupport());
        // override the alert category,  since only SMS and incoming call support text notification
        AlertCategory category = AlertCategory.SMS;
        if (simpleNotification.getAlertCategory() == AlertCategory.IncomingCall) {
            category = simpleNotification.getAlertCategory();
        }
        NewAlert alert = new NewAlert(category, 1, simpleNotification.getMessage());
        profile.newAlert(builder, alert, OverflowStrategy.MAKE_MULTIPLE);
    }

    @Override
    public void stopCurrentNotification(TransactionBuilder builder) {
        BluetoothGattCharacteristic alert = getSupport().getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_NEW_ALERT);
        builder.write(alert, new byte[]{(byte) AlertCategory.IncomingCall.getId(), 0});
    }
}
