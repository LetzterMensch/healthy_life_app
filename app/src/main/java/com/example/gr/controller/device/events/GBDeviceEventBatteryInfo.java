/*  Copyright (C) 2015-2024 Andreas Shimokawa, Daniele Gobbetti, José Rebelo,
    Petr Vaněk

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
package com.example.gr.controller.device.events;


import com.example.gr.controller.device.model.BatteryState;

import java.util.GregorianCalendar;


public class GBDeviceEventBatteryInfo extends GBDeviceEvent {
    public GregorianCalendar lastChargeTime = null;
    public BatteryState state = BatteryState.UNKNOWN;
    public int batteryIndex = 0;
    public int level = 50;
    public int numCharges = -1;
    public float voltage = -1f;

    public boolean extendedInfoAvailable() {
        if (numCharges != -1 && lastChargeTime != null) {
            return true;
        }
        return false;
    }
}
