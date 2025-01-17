package com.example.gr.controller.service.btle.actions;

import android.bluetooth.BluetoothGatt;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.gr.controller.device.GBDevice;


public class SetDeviceStateAction extends PlainAction {
    private final GBDevice device;
    private final GBDevice.State deviceState;
    private final Context context;

    public SetDeviceStateAction(GBDevice device, GBDevice.State deviceState, Context context) {
        this.device = device;
        this.deviceState = deviceState;
        this.context = context;
    }

    @Override
    public boolean run(BluetoothGatt gatt) {
        device.setState(deviceState);
        device.sendDeviceUpdateIntent(getContext(), GBDevice.DeviceUpdateSubject.DEVICE_STATE);
        return true;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " to " + deviceState;
    }
}
