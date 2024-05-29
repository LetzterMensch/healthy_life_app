package com.example.gr.device.scan;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import androidx.annotation.Nullable;

/**
 * A scan event originating from either BT or BLE scan. References the BluetoothDevice, rssi,
 * and service UUIDs, if any.
 */
public class GBScanEvent {
    private final BluetoothDevice device;
    private final short rssi;

    @Nullable
    private final ParcelUuid[] serviceUuids;

    public GBScanEvent(final BluetoothDevice device, final short rssi, @Nullable final ParcelUuid[] serviceUuids) {
        this.device = device;
        this.rssi = rssi;
        this.serviceUuids = serviceUuids;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public short getRssi() {
        return rssi;
    }

    @Nullable
    public ParcelUuid[] getServiceUuids() {
        return serviceUuids;
    }
}
