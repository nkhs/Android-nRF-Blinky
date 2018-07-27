/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.blinky;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import no.nordicsemi.android.blinky.adapter.ExtendedBluetoothDevice;
import no.nordicsemi.android.blinky.viewmodels.BlinkyViewModel;

@SuppressWarnings("ConstantConditions")
public class BlinkyActivity extends AppCompatActivity implements RandomEventListener {
    public static final String EXTRA_DEVICE = "no.nordicsemi.android.blinky.EXTRA_DEVICE";

    byte currentLevel = 10;

//    boolean isRandomMode = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinky);

//        RandomThread randomThread = new RandomThread();
//        randomThread.setListener(this);
//        new Thread(randomThread).start();

        final Intent intent = getIntent();
        final ExtendedBluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
        final String deviceName = device.getName();
        final String deviceAddress = device.getAddress();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(deviceName);
        getSupportActionBar().setSubtitle(deviceAddress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the view model
        final BlinkyViewModel viewModel = ViewModelProviders.of(this).get(BlinkyViewModel.class);
        viewModel.connect(device);

        // Set up views


        final LinearLayout progressContainer = findViewById(R.id.progress_container);
        final TextView connectionState = findViewById(R.id.connection_state);
        final View content = findViewById(R.id.device_container);

        final Button btnLevel = findViewById(R.id.btn_level);
        btnLevel.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_level);
            dialog.findViewById(R.id.btn_level_1).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_1);
                currentLevel = 1;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_2).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_2);
                currentLevel = 2;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_3).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_3);
                currentLevel = 3;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_4).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_4);
                currentLevel = 4;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_5).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_5);
                currentLevel = 5;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_6).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_6);
                currentLevel = 6;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_7).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_7);
                currentLevel = 7;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_8).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_8);
                currentLevel = 8;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_9).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_9);
                currentLevel = 9;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_10).setOnClickListener(v -> {
                btnLevel.setText(R.string.level_10);
                currentLevel = 10;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });


            dialog.show();
        });
        final ToggleButton toggleStartStop = findViewById(R.id.btn_start_stop_toggle);
        toggleStartStop.setOnClickListener(view -> {
            if (toggleStartStop.isChecked()) {
                viewModel.writeMode((byte) 0x01);
            } else {
                viewModel.writeMode((byte) 0x02);
            }
        });
        final ToggleButton toggleRandomStartStop = findViewById(R.id.btn_start_random_stop_toggle);
        toggleRandomStartStop.setOnClickListener(view -> {
//            isRandomMode = toggleRandomStartStop.isChecked();
            if (toggleRandomStartStop.isChecked()) {
                // start random
                viewModel.writeMode((byte) 0x03);

                toggleStartStop.setEnabled(false);
                btnLevel.setEnabled(false);

            } else {
                // stop random
                viewModel.writeMode((byte) 0x02);

                toggleStartStop.setEnabled(true);
                btnLevel.setEnabled(true);
            }
        });


        viewModel.isDeviceReady().observe(this, deviceReady -> {
            progressContainer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        });
        viewModel.getConnectionState().observe(this, connectionState::setText);
        viewModel.isConnected().observe(this, connected -> {
            if (!connected) {
                Toast.makeText(this, R.string.state_disconnected, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.isSupported().observe(this, supported -> {
            if (!supported) {
                Toast.makeText(this, R.string.state_not_supported, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onSpeed(byte speed) {
//        Log.d("TEST", "random " + speed + ", " + isRandomMode);
//        if (!isRandomMode) return;
    }
}
