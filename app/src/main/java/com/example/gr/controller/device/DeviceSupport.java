
package com.example.gr.controller.device;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.example.gr.controller.handler.EventHandler;


/**
 * Provides the communication support for a specific device. Instances will <b>only</b>
 * be used inside the DeviceCommunicationService. Has hooks to manage the life cycle
 * of a device: instances of this interface will be created, initialized, and disposed
 * as needed.
 * <p/>
 * Implementations need to act accordingly, in order to establish, reestablish or close
 * the connection to the device.
 * <p/>
 * In principle, this interface is agnostic to the kind of transport, i.e. whether the
 * device is connected via Bluetooth, Bluetooth LE, Wifi or something else, however at the
 * moment, only the BluetoothAdapter is passed to implementations.
 */
public interface DeviceSupport extends EventHandler {
    /**
     * Sets all context information needed for the instance to operate.
     *
     * @param gbDevice  the device to operate with
     * @param btAdapter the bluetooth adapter to use
     * @param context   the android context, e.g. to look up resources
     */
    void setContext(GBDevice gbDevice, BluetoothAdapter btAdapter, Context context);

    /**
     * Returns whether a transport-level connection is established with the device
     *
     * @return whether the device is connected with the system running this software
     */
    boolean isConnected();

    /**
     * Attempts an initial connection to the device, typically after the user "discovered"
     * and connects to it for the first time. Some implementations may perform an additional
     * initialization or application-level pairing compared to the regular {@link #connect()}.
     * <p/>
     * Implementations may perform the connection in a synchronous or asynchronous way.
     * Returns true if a connection attempt was made. If the implementation is synchronous
     * it may also return true if the connection was successfully established, however
     * callers shall not rely on that.
     * <p/>
     * The actual connection state change (successful or not) will be reported via the
     * #getDevice device as a device change Intent.
     *
     * Note: the default implementation {@link AbstractDeviceSupport#connectFirstTime()} just
     * calls {@link #connect()}
     *
     * @see #connect()
     * @see GBDevice#ACTION_DEVICE_CHANGED
     */
    boolean connectFirstTime();

    /**
     * Attempts to establish a connection to the device. Implementations may perform
     * the connection in a synchronous or asynchronous way.
     * Returns true if a connection attempt was made. If the implementation is synchronous
     * it may also return true if the connection was successfully established, however
     * callers shall not rely on that.
     * <p/>
     * The actual connection state change (successful or not) will be reported via the
     * #getDevice device as a device change Intent.
     *
     * @see #connectFirstTime()
     * @see GBDevice#ACTION_DEVICE_CHANGED
     */
    boolean connect();

    /**
     * Disposes of this instance, closing all connections and freeing all resources.
     * Instances will not be reused after having been disposed.
     */
    void dispose();

    /**
     * Returns true if a connection attempt shall be made automatically whenever
     * needed (e.g. when a notification shall be sent to the device while not connected.
     */
    boolean useAutoConnect();

    /**
     * Configures this instance to automatically attempt to reconnect after a connection loss.
     * How, how long, or how often is up to the implementation.
     * Note that tome implementations may not support automatic reconnection at all.
     * @param enable
     */
    void setAutoReconnect(boolean enable);

    /**
     * Returns whether this instance to configured to automatically attempt to reconnect after a
     * connection loss.
     */
    boolean getAutoReconnect();

    void setScanReconnect(boolean enable);

    boolean getScanReconnect();

    /**
     * Returns whether the gatt callback should be implicitly set to the one on the transaction,
     * even if it was not set directly on the transaction. If true, the gatt callback will always
     * be set to the one in the transaction, even if null and not explicitly set to null.
     * See https://codeberg.org/Freeyourgadget/Gadgetbridge/pulls/2912 for more information.
     * This should be false by default, but we are making it configurable to avoid breaking
     * older devices that rely on this behavior.
     */
    boolean getImplicitCallbackModify();

    /**
     * Returns the associated device this instance communicates with.
     */
    GBDevice getDevice();

    /**
     * Returns the bluetooth adapter. When we support different transports
     * than Bluetooth, we should use a generic type T and rename this method
     * to getTransportAdapter()
     */
    BluetoothAdapter getBluetoothAdapter();

    /**
     * Returns the Android context to use, e.g. to look up resources.
     */
    Context getContext();

    /**
     * converts String in a device specific way, e.g. re-map characters for a custom font
     */
    String customStringFilter(String inputString);
}
