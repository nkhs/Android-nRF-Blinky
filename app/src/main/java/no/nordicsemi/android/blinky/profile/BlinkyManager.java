/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.blinky.profile;

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
    /**
     * Nordic Blinky Service UUID.
     */
    public final static UUID LBS_UUID_SERVICE = UUID.fromString("00001975-0000-1000-8000-00805f9b34fb");
//    public final static UUID LBS_UUID_SERVICE = UUID.fromString("00001207-0000-1000-8000-00805f9b34fb");
    /**
     * BUTTON characteristic UUID.
     */
    private final static UUID LBS_UUID_MODE_CHAR = UUID.fromString("00001006-0000-1000-8000-00805f9b34fb");
//    private final static UUID LBS_UUID_MODE_CHAR = UUID.fromString("00001208-0000-1000-8000-00805f9b34fb");
    /**
     * LED characteristic UUID.
     */
    private final static UUID LBS_UUID_LEVEL_CHAR = UUID.fromString("00001007-0000-1000-8000-00805f9b34fb");
//    private final static UUID LBS_UUID_LEVEL_CHAR = UUID.fromString("00001209-0000-1000-8000-00805f9b34fb");

    private final static UUID LBS_UUID_MODE_NOTI_CHAR = UUID.fromString("00001006-0000-1000-8000-00805f9b34fb");
//    private final static UUID LBS_UUID_MODE_NOTI_CHAR = UUID.fromString("00001208-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mModeCharacteristic, mLevelCharacteristic;

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
            requests.push(Request.newReadRequest(mLevelCharacteristic));
            requests.push(Request.newReadRequest(mModeCharacteristic));
            // requests.push(Request.newEnableNotificationsRequest(mModeCharacteristic));
            return requests;
        }

        @Override
        public boolean isRequiredServiceSupported(final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(LBS_UUID_SERVICE);
            if (service != null) {
                mModeCharacteristic = service.getCharacteristic(LBS_UUID_MODE_CHAR);
                mLevelCharacteristic = service.getCharacteristic(LBS_UUID_LEVEL_CHAR);
            }

            boolean writeRequest = false;
            if (mLevelCharacteristic != null) {
                final int rxProperties = mLevelCharacteristic.getProperties();
                writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
            }

            return mModeCharacteristic != null && mLevelCharacteristic != null && writeRequest;
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
                final boolean ledOn = data == 0x01;
                log(LogContract.Log.Level.APPLICATION, "LED " + (ledOn ? "ON" : "OFF"));
                mCallbacks.onDataSent(ledOn);
            } else {
                final boolean buttonPressed = data == 0x01;
                log(LogContract.Log.Level.APPLICATION, "Button " + (buttonPressed ? "pressed" : "released"));
                mCallbacks.onDataReceived(buttonPressed);
            }
        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // This method is only called for LED characteristic
            final int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            final boolean ledOn = data == 0x01;
            log(LogContract.Log.Level.APPLICATION, "LED " + (ledOn ? "ON" : "OFF"));
            mCallbacks.onDataSent(ledOn);
        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // This method is only called for Button characteristic
            final int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            final boolean buttonPressed = data == 0x01;
            log(LogContract.Log.Level.APPLICATION, "Button " + (buttonPressed ? "pressed" : "released"));
            mCallbacks.onDataReceived(buttonPressed);
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
