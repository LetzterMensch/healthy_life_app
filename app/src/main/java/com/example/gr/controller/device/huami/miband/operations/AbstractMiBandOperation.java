/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, Damien Gaignon

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
package com.example.gr.controller.device.huami.miband.operations;

import android.widget.Toast;

import com.example.gr.utils.GB;
import com.example.gr.controller.service.btle.AbstractBTLEDeviceSupport;
import com.example.gr.controller.service.btle.AbstractBTLEOperation;
import com.example.gr.controller.service.btle.TransactionBuilder;

import java.io.IOException;


public abstract class AbstractMiBandOperation<T extends AbstractBTLEDeviceSupport> extends AbstractBTLEOperation<T> {
    protected AbstractMiBandOperation(T support) {
        super(support);
    }

    @Override
    protected void prePerform() throws IOException {
        super.prePerform();
        System.out.println("inside miband operation");
        getDevice().setBusyTask("Operation starting..."); // mark as busy quickly to avoid interruptions from the outside
        TransactionBuilder builder = performInitialized("disabling some notifications");
        enableOtherNotifications(builder, false);
        enableNeededNotifications(builder, true);
        builder.queue(getQueue());
    }

    @Override
    protected void operationFinished() {
        operationStatus = OperationStatus.FINISHED;
        if (getDevice() != null && getDevice().isConnected()) {
            unsetBusy();
            try {
                TransactionBuilder builder = performInitialized("reenabling disabled notifications");
                handleFinished(builder);
                builder.setCallback(null); // unset ourselves from being the queue's gatt callback
                builder.queue(getQueue());
            } catch (IOException ex) {
                GB.toast(getContext(), "Error enabling Mi Band notifications, you may need to connect and disconnect", Toast.LENGTH_LONG, GB.ERROR, ex);
            }
        }
    }


    private void handleFinished(TransactionBuilder builder) {
        enableNeededNotifications(builder, false);
        enableOtherNotifications(builder, true);
    }

    /**
     * Enables or disables the notifications that are needed for the entire operation.
     * Enabled on operation start and disabled on operation finish.
     * @param builder
     * @param enable
     */
    protected abstract void enableNeededNotifications(TransactionBuilder builder, boolean enable);

    /**
     * Enables or disables certain kinds of notifications that could interfere with this
     * operation. Call this method once initially to disable other notifications, and once
     * when this operation has finished.
     *
     * @param builder
     * @param enable  true to enable, false to disable the other notifications
     */
    protected abstract void enableOtherNotifications(TransactionBuilder builder, boolean enable);
}
