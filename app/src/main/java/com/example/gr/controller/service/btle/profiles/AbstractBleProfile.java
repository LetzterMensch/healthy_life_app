/*  Copyright (C) 2016-2024 Andreas Shimokawa, Carsten Pfeiffer, Damien
    Gaignon, Daniele Gobbetti, Taavi Eomäe

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
package com.example.gr.controller.service.btle.profiles;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;

import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.service.btle.AbstractBTLEDeviceSupport;
import com.example.gr.controller.service.btle.AbstractGattCallback;
import com.example.gr.controller.service.btle.BtLEQueue;
import com.example.gr.controller.service.btle.TransactionBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;



/**
 * Base class for all BLE profiles, with things that all implementations are
 * expected to use.
 * <p>
 * Instances are used in the context of a concrete AbstractBTLEDeviceSupport instance,
 * i.e. a concrete device.
 *
 * @see nodomain.freeyourgadget.gadgetbridge.service.btle.GattService
 * @see nodomain.freeyourgadget.gadgetbridge.service.btle.GattCharacteristic
 * @see https://www.bluetooth.com/specifications/assigned-numbers
 */
public abstract class AbstractBleProfile<T extends AbstractBTLEDeviceSupport> extends AbstractGattCallback {
    private final T mSupport;

    private List<IntentListener> listeners = new ArrayList<IntentListener>(1);

    public AbstractBleProfile(T support) {
        this.mSupport = support;
    }

    public void addListener(IntentListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public boolean removeListener(IntentListener listener) {
        return listeners.remove(listener);
    }

    protected List<IntentListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * All notifications should be sent through this methods to make them testable.
     * @param intent the intent to broadcast
     */
    protected void notify(Intent intent) {
        // note: we send synchronously in order to keep the processing order of BLE events
        // in BtleQueue and the reception of results.
        for (IntentListener listener : listeners) {
            listener.notify(intent);
        }
    }

    /**
     * Delegates to the DeviceSupport instance and additionally sets this instance as the Gatt
     * callback for the transaction.
     *
     * @param taskName
     * @return
     * @throws IOException
     */
    public TransactionBuilder performInitialized(String taskName) throws IOException {
        TransactionBuilder builder = mSupport.performInitialized(taskName);
        builder.setCallback(this);
        return builder;
    }

    public Context getContext() {
        return mSupport.getContext();
    }

    protected GBDevice getDevice() {
        return mSupport.getDevice();
    }

    protected BluetoothGattCharacteristic getCharacteristic(UUID uuid) {
        return mSupport.getCharacteristic(uuid);
    }

    protected BtLEQueue getQueue() {
        return mSupport.getQueue();
    }

    public void enableNotify(TransactionBuilder builder, boolean enable) {
    }

}
