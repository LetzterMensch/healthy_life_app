/*  Copyright (C) 2015-2024 Andreas Shimokawa, Arjan Schrijver, Carsten
    Pfeiffer, José Rebelo, Julien Pivotto, Kasha, Sebastian Kranz, Steffen
    Liebergeld, Uwe Hermann

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
package com.example.gr.handler;

import android.location.Location;
import android.net.Uri;

import com.example.gr.device.model.Alarm;
import com.example.gr.device.model.CalendarEventSpec;
import com.example.gr.device.model.CallSpec;
import com.example.gr.device.model.CannedMessagesSpec;
import com.example.gr.device.model.Contact;
import com.example.gr.device.model.MusicSpec;
import com.example.gr.device.model.MusicStateSpec;
import com.example.gr.device.model.NavigationInfoSpec;
import com.example.gr.device.model.NotificationSpec;
import com.example.gr.device.model.Reminder;
import com.example.gr.device.model.WeatherSpec;
import com.example.gr.device.model.WorldClock;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Specifies all events that Gadgetbridge intends to send to the gadget device.
 * Implementations can decide to ignore events that they do not support.
 * Implementations need to send/encode event to the connected device.
 */
public interface EventHandler {
    void onNotification(NotificationSpec notificationSpec);

    void onDeleteNotification(int id);

    void onSetTime();

    void onSetAlarms(ArrayList<? extends Alarm> alarms);

    void onSetReminders(ArrayList<? extends Reminder> reminders);

//    void onSetLoyaltyCards(ArrayList<LoyaltyCard> cards);

    void onSetWorldClocks(ArrayList<? extends WorldClock> clocks);

    void onSetContacts(ArrayList<? extends Contact> contacts);

    void onSetCallState(CallSpec callSpec);

    void onSetCannedMessages(CannedMessagesSpec cannedMessagesSpec);

    void onSetMusicState(MusicStateSpec stateSpec);

    void onSetMusicInfo(MusicSpec musicSpec);

    /**
     * Sets the current phone media volume.
     *
     * @param volume the volume percentage (0 to 100).
     */
    void onSetPhoneVolume(final float volume);

    void onChangePhoneSilentMode(int ringerMode);

    void onSetNavigationInfo(NavigationInfoSpec navigationInfoSpec);

    void onEnableRealtimeSteps(boolean enable);

//    void onInstallApp(Uri uri);

    void onAppInfoReq();

    void onAppStart(UUID uuid, boolean start);

    void onAppDownload(UUID uuid);

    void onAppDelete(UUID uuid);

    void onAppConfiguration(UUID appUuid, String config, Integer id);

    void onAppReorder(UUID uuids[]);

    void onFetchRecordedData(int dataTypes);

    void onReset(int flags);

    void onHeartRateTest();

    void onEnableRealtimeHeartRateMeasurement(boolean enable);

    void onFindDevice(boolean start);

    void onFindPhone(boolean start);

    void onSetConstantVibration(int integer);

    void onScreenshotReq();

    void onEnableHeartRateSleepSupport(boolean enable);

    void onSetHeartRateMeasurementInterval(int seconds);

    void onAddCalendarEvent(CalendarEventSpec calendarEventSpec);

    void onDeleteCalendarEvent(byte type, long id);

    /**
     * Sets the given option in the device, typically with values from the preferences.
     * The config name is device specific.
     * @param config the device specific option to set on the device
     */
    void onSendConfiguration(String config);

    /**
     * Gets the given option from the device, sets the values in the preferences.
     * The config name is device specific.
     * @param config the device specific option to get from the device
     */
    void onReadConfiguration(String config);

    void onTestNewFunction();

    void onSendWeather(WeatherSpec weatherSpec);

    void onSetFmFrequency(float frequency);

    /**
     * Set the device's led color.
     * @param color the new color, in ARGB, with alpha = 255
     */
    void onSetLedColor(int color);

    void onPowerOff();

    void onSetGpsLocation(Location location);
}
