package com.example.gr.controller.device;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.DBHandler;
import com.example.gr.model.database.DBHelper;
import com.example.gr.utils.DeviceHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Provides access to the list of devices managed by Gadgetbridge.
 * Changes to the devices (e.g. connection state) or the list of devices
 * are broadcasted via #ACTION_DEVICE_CHANGED
 */
public class DeviceManager {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceManager.class);

    public static final String BLUETOOTH_DEVICE_ACTION_ALIAS_CHANGED = "android.bluetooth.device.action.ALIAS_CHANGED";
    /**
     * Intent action to notify that the list of devices has changed.
     */
    public static final String ACTION_DEVICES_CHANGED
            = "com.example.gr.device.devicemanager.action.devices_changed";
    /**
     * Intent action to notify this class that the list of devices shall be refreshed.
     */
    public static final String ACTION_REFRESH_DEVICELIST
            = "com.example.gr.device.devicemanager.action.set_version";
    private final Context context;
    /**
     * This list is final, it will never be recreated. Only its contents change.
     * This allows direct access to the list from ListAdapters.
     */
    private final List<GBDevice> deviceList = new ArrayList<>();
    private List<GBDevice> selectedDevices = new ArrayList<>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_REFRESH_DEVICELIST: // fall through
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    refreshPairedDevices();
                    break;
                case BluetoothDevice.ACTION_NAME_CHANGED:
                case BLUETOOTH_DEVICE_ACTION_ALIAS_CHANGED:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String newName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                    if (device!=null)
                      updateDeviceName(device, newName);
                    break;
                case GBDevice.ACTION_DEVICE_CHANGED:
                    GBDevice dev = intent.getParcelableExtra(GBDevice.EXTRA_DEVICE);
                    if (dev.getAddress() != null) {
                        int index = deviceList.indexOf(dev); // search by address
                        if (index >= 0) {
                            deviceList.get(index).copyFromDevice(dev);
                        } else {
                            deviceList.add(dev);
                        }
                        if (dev.isInitialized()) {
                            try (DBHandler dbHandler = ControllerApplication.acquireDB()) {
                                DBHelper.getDevice(dev, dbHandler.getDaoSession()); // implicitly creates the device in database if not present, and updates device attributes
                            } catch (Exception ignore) {
                            }
                        }
                    }
                    updateSelectedDevice(dev);
                    refreshPairedDevices();
                    break;
            }
        }
    };

    public DeviceManager(Context context) {
        this.context = context;
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(DeviceManager.ACTION_REFRESH_DEVICELIST);
        filterLocal.addAction(GBDevice.ACTION_DEVICE_CHANGED);
        filterLocal.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filterLocal);

        IntentFilter filterGlobal = new IntentFilter();
        filterGlobal.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filterGlobal.addAction(BLUETOOTH_DEVICE_ACTION_ALIAS_CHANGED);
        filterGlobal.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(mReceiver, filterGlobal);

        refreshPairedDevices();
    }

    private void updateDeviceName(BluetoothDevice device, String newName) {
        for (GBDevice dev : deviceList) {
            if (device.getAddress().equals(dev.getAddress())) {
                if (!dev.getName().equals(newName)) {
                    dev.setName(newName);
                    notifyDevicesChanged();
                    return;
                }
            }
        }
    }

    private void updateSelectedDevice(GBDevice dev) {
        selectedDevices.clear();
        for(GBDevice device : deviceList){
            if(device.isInitialized()){
                selectedDevices.add(device);
            }
        }
    }

    private void refreshPairedDevices() {
        Set<GBDevice> availableDevices = DeviceHelper.getInstance().getAvailableDevices(context);
        deviceList.retainAll(availableDevices);
        for (GBDevice availableDevice : availableDevices) {
            if (!deviceList.contains(availableDevice)) {
                deviceList.add(availableDevice);
            }
        }

        Collections.sort(deviceList, new Comparator<GBDevice>() {
            @Override
            public int compare(GBDevice lhs, GBDevice rhs) {
                if (rhs.getStateOrdinal() - lhs.getStateOrdinal() == 0) {
                    return Collator.getInstance().compare(lhs.getAliasOrName(), rhs.getAliasOrName());
                }
                return (rhs.getStateOrdinal() - lhs.getStateOrdinal());
            }
        });
        notifyDevicesChanged();
    }

    /**
     * The returned list is final, it will never be recreated. Only its contents change.
     * This allows direct access to the list from ListAdapters.
     */
    public List<GBDevice> getDevices() {
        return Collections.unmodifiableList(deviceList);
    }

    public GBDevice getDeviceByAddress(String address){
        for(GBDevice device : deviceList){
            if(device.getAddress().compareToIgnoreCase(address) == 0){
                return device;
            }
        }
        return null;
    }

    public List<GBDevice> getSelectedDevices() {
        return selectedDevices;
    }

    private void notifyDevicesChanged() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_DEVICES_CHANGED));
    }
}
