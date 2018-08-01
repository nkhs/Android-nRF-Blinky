
package com.ble.android.netknight.profile;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.log.LogContract;

public class BlinkyManager extends BleManager<BlinkyManagerCallbacks> {

//    public final static UUID BLE_UUID_SERVICE = UUID.fromString("00001977-0000-1000-8000-00805f9b34fb");//180d
    public final static UUID BLE_UUID_SERVICE = UUID.fromString("00001207-0000-1000-8000-00805f9b34fb");
    /**
     * BUTTON characteristic UUID.
     */
//    private final static UUID BLE_UUID_MODE_CHAR = UUID.fromString("00001006-0000-1000-8000-00805f9b34fb");
    private final static UUID BLE_UUID_MODE_CHAR = UUID.fromString("00001208-0000-1000-8000-00805f9b34fb");
    /**
     * LED characteristic UUID.
     */
//    private final static UUID BLE_UUID_LEVEL_CHAR = UUID.fromString("00001007-0000-1000-8000-00805f9b34fb");//2a37
    private final static UUID BLE_UUID_LEVEL_CHAR = UUID.fromString("00001209-0000-1000-8000-00805f9b34fb");

    //    private final static UUID BLE_UUID_MODE_NOTIFY_CHAR = UUID.fromString("00001028-0000-1000-8000-00805f9b34fb");
    private final static UUID BLE_UUID_MODE_NOTIFY_CHAR = UUID.fromString("00001210-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mModeCharacteristic, mModeNotifyCharacteristic, mLevelCharacteristic;

    public BlinkyManager(final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return mGattCallback;
    }

    @Override
    protected boolean shouldAutoConnect() {
        // If you want to connect to the device using autoConnect flag = true, return true here.
        // Read the documentation of this method.
        return super.shouldAutoConnect();
    }

    /**
     * BluetoothGatt callbacks for connection/disconnection, service discovery, receiving indication, etc
     */
    private final BleManagerGattCallback mGattCallback = new BleManagerGattCallback() {

        @Override
        protected Deque<Request> initGatt(final BluetoothGatt gatt) {
            final LinkedList<Request> requests = new LinkedList<>();

//            requests.push(Request.newReadRequest(mModeCharacteristic));
            requests.push(Request.newReadRequest(mLevelCharacteristic));
            requests.push(Request.newEnableNotificationsRequest(mLevelCharacteristic));

            requests.push(Request.newReadRequest(mModeNotifyCharacteristic));
            requests.push(Request.newEnableNotificationsRequest(mModeNotifyCharacteristic));
            return requests;
        }

        @Override
        public boolean isRequiredServiceSupported(final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BLE_UUID_SERVICE);
            if (service != null) {
                mModeCharacteristic = service.getCharacteristic(BLE_UUID_MODE_CHAR);
                mLevelCharacteristic = service.getCharacteristic(BLE_UUID_LEVEL_CHAR);
                mModeNotifyCharacteristic = service.getCharacteristic(BLE_UUID_MODE_NOTIFY_CHAR);
            }

            boolean writeRequest = false;
            if (mModeCharacteristic != null) {
                final int rxProperties = mModeCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
            }
            if (!writeRequest) return false;

            writeRequest = false;
            if (mLevelCharacteristic != null) {
                final int rxProperties = mLevelCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0;
            }
            if (!writeRequest) return false;

            writeRequest = false;
            if (mModeNotifyCharacteristic != null) {
                final int rxProperties = mModeNotifyCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0;
            }

            return writeRequest && mModeCharacteristic != null && mModeNotifyCharacteristic != null && mLevelCharacteristic != null;
        }

        @Override
        protected void onDeviceDisconnected() {
            mModeCharacteristic = null;
            mLevelCharacteristic = null;
        }

        @Override
        protected void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            final int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            if (characteristic == mLevelCharacteristic) {
                mCallbacks.onLevelReceived(data);
            }
            if (characteristic == mModeNotifyCharacteristic) {
                mCallbacks.onModeReceived(data);
            }

        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // This method is only called for LED characteristic

        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            final int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            if (characteristic == mLevelCharacteristic) {
                mCallbacks.onLevelReceived(data);
            }
            if (characteristic == mModeNotifyCharacteristic) {
                mCallbacks.onModeReceived(data);
            }
        }
    };

    public void sendMode(final byte byteSpeed) {
        // Are we connected?
        if (mModeCharacteristic == null)
            return;

        final byte[] command = new byte[]{(byteSpeed)};
        log(LogContract.Log.Level.VERBOSE, "Write Mode: " + byteSpeed);
        writeCharacteristic(mModeCharacteristic, command);
    }


    public void sendLevel(final byte byteSpeed) {
        // Are we connected?
        if (mLevelCharacteristic == null)
            return;

        final byte[] command = new byte[]{(byteSpeed)};
        log(LogContract.Log.Level.VERBOSE, "Write Level: " + byteSpeed);
        writeCharacteristic(mLevelCharacteristic, command);
    }

}
