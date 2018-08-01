package com.ble.android.netknight.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.util.Log;


import com.ble.android.netknight.R;
import com.ble.android.netknight.adapter.ExtendedBluetoothDevice;
import com.ble.android.netknight.profile.BlinkyManager;
import com.ble.android.netknight.profile.BlinkyManagerCallbacks;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class BlinkyViewModel extends AndroidViewModel implements BlinkyManagerCallbacks {
    private final BlinkyManager mBlinkyManager;

    // Connection states Connecting, Connected, Disconnecting, Disconnected etc.
    private final MutableLiveData<String> mConnectionState = new MutableLiveData<>();

    // Flag to determine if the device is connected
    private final MutableLiveData<Boolean> mIsConnected = new MutableLiveData<>();

    // Flag to determine if the device has required services
    private final SingleLiveEvent<Boolean> mIsSupported = new SingleLiveEvent<>();

    // Flag to determine if the device is ready
    private final MutableLiveData<Void> mOnDeviceReady = new MutableLiveData<>();


    // Flag that holds the pressed released state of the button on the devkit. Pressed is true, Released is False
    private final MutableLiveData<Integer> mLevelState = new MutableLiveData<>();
    private final MutableLiveData<Integer> mModeState = new MutableLiveData<>();

    public LiveData<Void> isDeviceReady() {
        return mOnDeviceReady;
    }

    public LiveData<String> getConnectionState() {
        return mConnectionState;
    }

    public LiveData<Boolean> isConnected() {
        return mIsConnected;
    }

    public LiveData<Integer> getLevelState() {
        return mLevelState;
    }

    public LiveData<Integer> getModeState() {
        return mModeState;
    }

    public LiveData<Boolean> isSupported() {
        return mIsSupported;
    }

    public BlinkyViewModel(@NonNull final Application application) {
        super(application);

        // Initialize the manager
        mBlinkyManager = new BlinkyManager(getApplication());
        mBlinkyManager.setGattCallbacks(this);
    }

    /**
     * Connect to peripheral
     */
    public void connect(final ExtendedBluetoothDevice device) {
        final LogSession logSession = Logger.newSession(getApplication(), null, device.getAddress(), device.getName());
        mBlinkyManager.setLogger(logSession);
        mBlinkyManager.connect(device.getDevice());
    }

    /**
     * Disconnect from peripheral
     */
    private void disconnect() {
        mBlinkyManager.disconnect();
    }

    public void writeMode(final byte mode) {
        mBlinkyManager.sendMode(mode);
    }

    public void writeLevel(final byte speed) {
        mBlinkyManager.sendLevel(speed);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (mBlinkyManager.isConnected()) {
            disconnect();
        }
    }

    @Override
    public void onLevelReceived(final int state) {
        Log.d("TAG", "" + state);
        mLevelState.postValue(state);
    }

    @Override
    public void onModeReceived(final int mode) {
        mModeState.postValue(mode);
    }

    @Override
    public void onDeviceConnecting(final BluetoothDevice device) {
        mConnectionState.postValue(getApplication().getString(R.string.state_connecting));
    }

    @Override
    public void onDeviceConnected(final BluetoothDevice device) {
        mIsConnected.postValue(true);
        mConnectionState.postValue(getApplication().getString(R.string.state_discovering_services));
    }

    @Override
    public void onDeviceDisconnecting(final BluetoothDevice device) {
        mIsConnected.postValue(false);
    }

    @Override
    public void onDeviceDisconnected(final BluetoothDevice device) {
        mIsConnected.postValue(false);
    }

    @Override
    public void onLinklossOccur(final BluetoothDevice device) {
        mIsConnected.postValue(false);
    }

    @Override
    public void onServicesDiscovered(final BluetoothDevice device, final boolean optionalServicesFound) {
        mConnectionState.postValue(getApplication().getString(R.string.state_initializing));
    }

    @Override
    public void onDeviceReady(final BluetoothDevice device) {
        mIsSupported.postValue(true);
        mConnectionState.postValue(getApplication().getString(R.string.state_discovering_services_completed, device.getName()));
        mOnDeviceReady.postValue(null);
    }

    @Override
    public boolean shouldEnableBatteryLevelNotifications(final BluetoothDevice device) {
        // Blinky doesn't have Battery Service
        return false;
    }

    @Override
    public void onBatteryValueReceived(final BluetoothDevice device, final int value) {
        // Blinky doesn't have Battery Service
    }

    @Override
    public void onBondingRequired(final BluetoothDevice device) {
        // Blinky does not require bonding
    }

    @Override
    public void onBonded(final BluetoothDevice device) {
        // Blinky does not require bonding
    }

    @Override
    public void onError(final BluetoothDevice device, final String message, final int errorCode) {
        // TODO implement
    }

    @Override
    public void onDeviceNotSupported(final BluetoothDevice device) {
        mIsSupported.postValue(false);
    }
}
