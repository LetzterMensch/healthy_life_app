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
import com.example.gr.model.database.entities.HuamiPaiSample;
import com.example.gr.model.database.entities.HuamiPaiSampleDao;
import com.example.gr.controller.device.AbstractTimeSampleProvider;
import com.example.gr.controller.device.GBDevice;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;

public class HuamiPaiSampleProvider extends AbstractTimeSampleProvider<HuamiPaiSample> {
    public HuamiPaiSampleProvider(final GBDevice device, final DaoSession session) {
        super(device, session);
    }

    @NonNull
    @Override
    public AbstractDao<HuamiPaiSample, ?> getSampleDao() {
        return getSession().getHuamiPaiSampleDao();
    }

    @NonNull
    @Override
    protected Property getTimestampSampleProperty() {
        return HuamiPaiSampleDao.Properties.Timestamp;
    }

    @NonNull
    @Override
    protected Property getDeviceIdentifierSampleProperty() {
        return HuamiPaiSampleDao.Properties.DeviceId;
    }

    @Override
    public HuamiPaiSample createSample() {
        return new HuamiPaiSample();
    }
}
