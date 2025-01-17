/*  Copyright (C) 2016-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti

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

import androidx.annotation.NonNull;

import com.example.gr.model.database.entities.DaoSession;
import com.example.gr.model.database.entities.MiBandActivitySample;
import com.example.gr.model.database.entities.MiBandActivitySampleDao;
import com.example.gr.controller.device.AbstractSampleProvider;
import com.example.gr.controller.device.GBDevice;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;

/**
 * Base class for Mi1 and Mi2 sample providers. At the moment they both share the
 * same activity sample class.
 */
public abstract class AbstractMiBandSampleProvider extends AbstractSampleProvider<MiBandActivitySample> {

    // maybe this should be configurable 256 seems way off, though.
    private final float movementDivisor = 180.0f; //256.0f;

    public AbstractMiBandSampleProvider(GBDevice device, DaoSession session) {
        super(device, session);
    }

    @Override
    public float normalizeIntensity(int rawIntensity) {
        return rawIntensity / movementDivisor;
    }

    @Override
    public AbstractDao<MiBandActivitySample, ?> getSampleDao() {
        return getSession().getMiBandActivitySampleDao();
    }

    @NonNull
    @Override
    protected Property getTimestampSampleProperty() {
        return MiBandActivitySampleDao.Properties.Timestamp;
    }

    @NonNull
    @Override
    protected Property getDeviceIdentifierSampleProperty() {
        return MiBandActivitySampleDao.Properties.DeviceId;
    }

    @Override
    protected Property getRawKindSampleProperty() {
        return MiBandActivitySampleDao.Properties.RawKind;
    }

    @Override
    public MiBandActivitySample createActivitySample() {
        return new MiBandActivitySample();
    }
}
