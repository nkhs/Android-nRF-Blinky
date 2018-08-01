package com.ble.android.netknight.profile;

import no.nordicsemi.android.ble.BleManagerCallbacks;

public interface BlinkyManagerCallbacks extends BleManagerCallbacks {

	/**
	 * Called when a button was pressed or released on device
	 *
	 * @param state true if the button was pressed, false if released
	 */
	void onLevelReceived(final int state);

	/**
	 * Called when the data has been sent to the connected device.
	 *
	 * @param state true when LED was enabled, false when disabled
	 */
	void onModeReceived(final int state);
}
