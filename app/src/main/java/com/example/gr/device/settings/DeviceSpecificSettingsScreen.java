/*  Copyright (C) 2024 José Rebelo

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
package com.example.gr.device.settings;

import androidx.annotation.XmlRes;

import com.example.gr.R;


public enum DeviceSpecificSettingsScreen {
    AUTHENTICATION("pref_screen_authentication", R.xml.devicesettings_root_authentication),
    ;

    private final String key;
    @XmlRes
    private final int xml;

    DeviceSpecificSettingsScreen(final String key, final int xml) {
        this.key = key;
        this.xml = xml;
    }

    public String getKey() {
        return key;
    }

    public int getXml() {
        return xml;
    }
}
