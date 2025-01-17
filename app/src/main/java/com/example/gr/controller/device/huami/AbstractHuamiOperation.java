/*  Copyright (C) 2018-2024 Andreas Shimokawa, José Rebelo

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

import com.example.gr.controller.device.huami.miband.operations.AbstractMiBandOperation;
import com.example.gr.controller.service.btle.TransactionBuilder;

public abstract class AbstractHuamiOperation extends AbstractMiBandOperation<HuamiSupport> {
    protected AbstractHuamiOperation(HuamiSupport support) {
        super(support);
    }

    @Override
    protected void enableOtherNotifications(final TransactionBuilder builder, final boolean enable) {
        // TODO: check which notifications we should disable and re-enable here
//        builder.notify(getCharacteristic(MiBandService.UUID_CHARACTERISTIC_REALTIME_STEPS), enable)
//                .notify(getCharacteristic(MiBandService.UUID_CHARACTERISTIC_SENSOR_DATA), enable);
    }
}
