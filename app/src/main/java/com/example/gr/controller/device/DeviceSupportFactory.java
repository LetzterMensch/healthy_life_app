
package com.example.gr.controller.device;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

import com.example.gr.R;
import com.example.gr.controller.device.model.DeviceType;
import com.example.gr.utils.GB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;


public class DeviceSupportFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSupportFactory.class);

    private final BluetoothAdapter mBtAdapter;
    private final Context mContext;

    public DeviceSupportFactory(Context context) {
        mContext = context;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public synchronized DeviceSupport createDeviceSupport(GBDevice device) throws GBException {
        DeviceSupport deviceSupport;
        String deviceAddress = device.getAddress();
        int indexFirstColon = deviceAddress.indexOf(":");
        if (indexFirstColon > 0) {
//            if (indexFirstColon == deviceAddress.lastIndexOf(":")) { // only one colon
//                deviceSupport = createTCPDeviceSupport(device);
//            } else {
//                // multiple colons -- bt?
//                deviceSupport = createBTDeviceSupport(device);
//            }
            deviceSupport = createBTDeviceSupport(device);
        } else {
            // no colon at all, maybe a class name?
            deviceSupport = createClassNameDeviceSupport(device);
        }

        if (deviceSupport != null) {
            return deviceSupport;
        }

        // no device found, check transport availability and warn
        checkBtAvailability();
        return null;
    }

    private DeviceSupport createClassNameDeviceSupport(GBDevice device) throws GBException {
        String className = device.getAddress();
        try {
            Class<?> deviceSupportClass = Class.forName(className);
            Constructor<?> constructor = deviceSupportClass.getConstructor();
            DeviceSupport support = (DeviceSupport) constructor.newInstance();
            // has to create the device itself
            support.setContext(device, null, mContext);
            return support;
        } catch (ClassNotFoundException e) {
            return null; // not a class, or not known at least
        } catch (Exception e) {
            throw new GBException("Error creating DeviceSupport instance for " + className, e);
        }
    }

    private void checkBtAvailability() {
        if (mBtAdapter == null) {
            GB.toast(mContext.getString(R.string.bluetooth_is_not_supported_), Toast.LENGTH_SHORT, GB.WARN);
        } else if (!mBtAdapter.isEnabled()) {
            GB.toast(mContext.getString(R.string.bluetooth_is_disabled_), Toast.LENGTH_SHORT, GB.WARN);
        }
    }

    private ServiceDeviceSupport createServiceDeviceSupport(GBDevice device) throws GBException {
        DeviceCoordinator coordinator = device.getDeviceCoordinator();
        Class supportClass = coordinator.getDeviceSupportClass();

        try {
            Constructor supportConstructor = supportClass.getConstructor(DeviceType.class);
            DeviceSupport supportInstance = (DeviceSupport) supportConstructor.newInstance(device.getType());
            return new ServiceDeviceSupport(supportInstance, coordinator.getInitialFlags());
        } catch (NoSuchMethodException e) {
            // ignore, let next call get the default, zero-argument constructor
        } catch (ReflectiveOperationException e) {
            LOG.error("error calling DeviceSupport constructor with argument 'DeviceType'");
            throw new GBException(e);
        }

        try {
            DeviceSupport supportInstance = (DeviceSupport) supportClass.newInstance();
            return new ServiceDeviceSupport(supportInstance, coordinator.getInitialFlags());
        } catch (ReflectiveOperationException e) {
            LOG.error("error calling DeviceSupport constructor with zero arguments");
            throw new GBException(e);
        }
    }

    private DeviceSupport createBTDeviceSupport(GBDevice gbDevice) throws GBException {
        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            try {
                DeviceSupport deviceSupport = createServiceDeviceSupport(gbDevice);
                if (deviceSupport != null) {
                    deviceSupport.setContext(gbDevice, mBtAdapter, mContext);
                    return deviceSupport;
                }
            } catch (Exception e) {
                throw new GBException(mContext.getString(R.string.cannot_connect_bt_address_invalid_), e);
            }
        }
        return null;
    }

//    private DeviceSupport createTCPDeviceSupport(GBDevice gbDevice) throws GBException {
//        try {
//            DeviceSupport deviceSupport = new ServiceDeviceSupport(new PebbleSupport(), EnumSet.of(ServiceDeviceSupport.Flags.BUSY_CHECKING));
//            deviceSupport.setContext(gbDevice, mBtAdapter, mContext);
//            return deviceSupport;
//        } catch (Exception e) {
//            throw new GBException("cannot connect to " + gbDevice, e); // FIXME: localize
//        }
//    }

}
