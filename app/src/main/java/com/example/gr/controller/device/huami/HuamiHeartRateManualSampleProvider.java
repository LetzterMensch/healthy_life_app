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
package com.example.gr.controller.device.huami;

import androidx.annotation.NonNull;

import com.example.gr.model.database.entities.DaoSession;
import com.example.gr.model.database.entities.HuamiHeartRateManualSample;
import com.example.gr.model.database.entities.HuamiHeartRateManualSampleDao;
import com.example.gr.controller.device.AbstractTimeSampleProvider;
import com.example.gr.controller.device.GBDevice;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;

public class HuamiHeartRateManualSampleProvider extends AbstractTimeSampleProvider<HuamiHeartRateManualSample> {
    public HuamiHeartRateManualSampleProvider(final GBDevice device, final DaoSession session) {
        super(device, session);
    }

    @NonNull
    @Override
    public AbstractDao<HuamiHeartRateManualSample, ?> getSampleDao() {
        return getSession().getHuamiHeartRateManualSampleDao();
    }

    @NonNull
    @Override
    protected Property getTimestampSampleProperty() {
        return HuamiHeartRateManualSampleDao.Properties.Timestamp;
    }

    @NonNull
    @Override
    protected Property getDeviceIdentifierSampleProperty() {
        return HuamiHeartRateManualSampleDao.Properties.DeviceId;
    }

    @Override
    public HuamiHeartRateManualSample createSample() {
        return new HuamiHeartRateManualSample();
    }
}
