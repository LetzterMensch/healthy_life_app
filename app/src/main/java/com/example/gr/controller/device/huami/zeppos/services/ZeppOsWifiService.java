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
package com.example.gr.controller.device.huami.zeppos.services;

import android.widget.Toast;

import com.example.gr.controller.device.events.GBDeviceEventUpdatePreferences;
import com.example.gr.utils.constant.DeviceSettingsPreferenceConst;
import com.example.gr.controller.service.btle.TransactionBuilder;
import com.example.gr.controller.device.huami.zeppos.AbstractZeppOsService;
import com.example.gr.controller.device.huami.zeppos.ZeppOsSupport;
import com.example.gr.utils.GB;
import com.example.gr.utils.Prefs;
import com.example.gr.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;



public class ZeppOsWifiService extends AbstractZeppOsService {
    private static final Logger LOG = LoggerFactory.getLogger(ZeppOsWifiService.class);

    private static final short ENDPOINT = 0x0003;

    public static final byte WIFI_CMD_HOTSPOT_START = 0x11;
    public static final byte WIFI_CMD_HOTSPOT_STOP = 0x12;
    public static final byte WIFI_CMD_HOTSPOT_STATE = 0x13;

    private Callback mCallback = null;

    public ZeppOsWifiService(final ZeppOsSupport support) {
        super(support, true);
    }

    @Override
    public short getEndpoint() {
        return ENDPOINT;
    }

    @Override
    public void handlePayload(final byte[] payload) {
        switch (payload[0]) {
            case WIFI_CMD_HOTSPOT_STATE:
                LOG.info("Wi-Fi hotspot state = {}", payload[1]);

                final String stateHex = String.format(Locale.ROOT, "0x%02x", payload[1]);
                final String stateStr;
                switch (payload[1]) {
                    case 0x00:
                        stateStr = "Stopped (" + stateHex + ")";
                        if (mCallback != null) {
                            mCallback.onWifiHotspotStop();
                        }
                        break;
                    case 0x02:
                        stateStr = "Started (" + stateHex + ")";
                        if (mCallback != null) {
                            mCallback.onWifiHotspotStart();
                        }
                        break;
                    default:
                        stateStr = "Unknown (" + stateHex + ")";
                        break;
                }

                // TODO: This toast + preference update should not be here
                if (mCallback == null) {
                    // If there's no callback, show a toast (eg. used from developer options)
                    GB.toast("Wi-Fi hotspot state: " + stateStr, Toast.LENGTH_SHORT, GB.INFO);
                }

                final GBDeviceEventUpdatePreferences evt = new GBDeviceEventUpdatePreferences()
                        .withPreference(DeviceSettingsPreferenceConst.WIFI_HOTSPOT_STATUS, stateStr);
                getSupport().evaluateGBDeviceEvent(evt);
                return;
            default:
                LOG.warn("Unexpected Wi-Fi byte {}", String.format("0x%02x", payload[0]));
        }
    }

    @Override
    public boolean onSendConfiguration(final String config, final Prefs prefs) {
        switch (config) {
            case DeviceSettingsPreferenceConst.WIFI_HOTSPOT_START:
                final String ssid = prefs.getString(DeviceSettingsPreferenceConst.WIFI_HOTSPOT_SSID, "");
                if (StringUtils.isNullOrEmpty(ssid)) {
                    LOG.error("Wi-Fi hotspot SSID not specified");
                    return true;
                }

                final String password = prefs.getString(DeviceSettingsPreferenceConst.WIFI_HOTSPOT_PASSWORD, "");
                if (StringUtils.isNullOrEmpty(password) || password.length() < 8) {
                    LOG.error("Wi-Fi hotspot password is not valid");
                    return true;
                }
                startWifiHotspot(ssid, password);
                return true;
            case DeviceSettingsPreferenceConst.WIFI_HOTSPOT_STOP:
                stopWifiHotspot();
                return true;
            case DeviceSettingsPreferenceConst.WIFI_HOTSPOT_SSID:
            case DeviceSettingsPreferenceConst.WIFI_HOTSPOT_PASSWORD:
            case DeviceSettingsPreferenceConst.WIFI_HOTSPOT_STATUS:
                // Ignore preferences that are not reloadable
                return true;
        }

        return false;
    }

    @Override
    public void initialize(final TransactionBuilder builder) {

    }

    public void setCallback(final Callback callback) {
        mCallback = callback;
    }

    public void removeCallback() {
        mCallback = null;
    }

    public void startWifiHotspot(final String ssid, final String password) {
        LOG.info("Starting Wi-Fi hotspot SSID={}", ssid);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            baos.write(WIFI_CMD_HOTSPOT_START);
            baos.write(ssid.getBytes(StandardCharsets.UTF_8));
            baos.write(0);
            baos.write(password.getBytes(StandardCharsets.UTF_8));
            baos.write(0);
            baos.write("gadgetbridge".getBytes(StandardCharsets.UTF_8));
            baos.write(0);
        } catch (final IOException e) {
            LOG.error("Failed to create command", e);
            return;
        }

        write("start wifi hotspot", baos.toByteArray());
    }

    public void stopWifiHotspot() {
        LOG.info("Stopping Wi-Fi hotspot");

        write("start wifi hotspot", WIFI_CMD_HOTSPOT_STOP);
    }

    public interface Callback {
        void onWifiHotspotStart();

        void onWifiHotspotStop();
    }
}
