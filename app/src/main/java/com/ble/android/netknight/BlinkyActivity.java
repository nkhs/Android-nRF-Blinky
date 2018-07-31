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

package com.ble.android.netknight;

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

import com.ble.android.netknight.viewmodels.BlinkyViewModel;
import com.suke.widget.SwitchButton;

import com.ble.android.netknight.adapter.ExtendedBluetoothDevice;

@SuppressWarnings("ConstantConditions")
public class BlinkyActivity extends AppCompatActivity implements RandomEventListener {
    public static final String EXTRA_DEVICE = "no.nordicsemi.android.blinky.EXTRA_DEVICE";

    byte currentLevel = 10;

    //    boolean isRandomMode = false;
    BlinkyViewModel viewModel;
    SwitchButton toggleSwitch;
    SwitchButton randomSwitch;
    Button btnLevelOutlet;
    TextView labelOutlet;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(no.nordicsemi.android.blinky.R.layout.activity_blinky);

//        RandomThread randomThread = new RandomThread();
//        randomThread.setListener(this);
//        new Thread(randomThread).start();

        final Intent intent = getIntent();
        final ExtendedBluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
        final String deviceName = device.getName();
        final String deviceAddress = device.getAddress();

        final Toolbar toolbar = findViewById(no.nordicsemi.android.blinky.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(deviceName);
        getSupportActionBar().setSubtitle(deviceAddress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the view model
        viewModel = ViewModelProviders.of(this).get(BlinkyViewModel.class);
        viewModel.connect(device);

        // Set up views
        labelOutlet = findViewById(no.nordicsemi.android.blinky.R.id.labelOutlet);

        final LinearLayout progressContainer = findViewById(no.nordicsemi.android.blinky.R.id.progress_container);
        final TextView connectionState = findViewById(no.nordicsemi.android.blinky.R.id.connection_state);
        final View content = findViewById(no.nordicsemi.android.blinky.R.id.device_container);

        btnLevelOutlet = findViewById(no.nordicsemi.android.blinky.R.id.btn_level);
        btnLevelOutlet.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(no.nordicsemi.android.blinky.R.layout.dialog_level);
            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_1).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_1);
                currentLevel = 1;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_2).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_2);
                currentLevel = 2;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_3).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_3);
                currentLevel = 3;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_4).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_4);
                currentLevel = 4;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_5).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_5);
                currentLevel = 5;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_6).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_6);
                currentLevel = 6;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_7).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_7);
                currentLevel = 7;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_8).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_8);
                currentLevel = 8;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_9).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_9);
                currentLevel = 9;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(no.nordicsemi.android.blinky.R.id.btn_level_10).setOnClickListener(v -> {
                btnLevelOutlet.setText(no.nordicsemi.android.blinky.R.string.level_10);
                currentLevel = 10;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });


            dialog.show();
        });

        toggleSwitch = findViewById(no.nordicsemi.android.blinky.R.id.switch_button);
        toggleSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            updateButton(isChecked, true);
        });

        randomSwitch = findViewById(no.nordicsemi.android.blinky.R.id.switch_random);
        randomSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            updateRandomButton(isChecked, true);
        });

        viewModel.isDeviceReady().observe(this, deviceReady -> {
            progressContainer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        });
        viewModel.getConnectionState().observe(this, connectionState::setText);
        viewModel.isConnected().observe(this, connected -> {
            if (!connected) {
                Toast.makeText(this, no.nordicsemi.android.blinky.R.string.state_disconnected, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.isSupported().observe(this, supported -> {
            if (!supported) {
                Toast.makeText(this, no.nordicsemi.android.blinky.R.string.state_not_supported, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getLevelState().observe(this, level -> btnLevelOutlet.setText("Level " + level));
        viewModel.getModeState().observe(this, mode -> {
            switch (mode) {
                case 0x01:
                    this.updateButton(true, false);
                    this.labelOutlet.setVisibility(View.VISIBLE);
                    break;
                case 0x02:
                    this.updateButton(false, false);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                case 0x03:
                    this.updateRandomButton(true, false);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                case 0x04:
                    this.updateRandomButton(false, false);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                default:
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
            }
        });

    }

    void updateButton(boolean isChecked, boolean isUpdate) {
        if (isChecked) {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x01);
            }
            randomSwitch.setEnabled(false);

        } else {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x02);
            }

            randomSwitch.setEnabled(true);
        }
    }

    void updateRandomButton(boolean isCheckedRandom, boolean isUpdate) {
        if (isCheckedRandom) {
            if (isUpdate) {

                viewModel.writeMode((byte) 0x03);
            }
            toggleSwitch.setEnabled(false);
            btnLevelOutlet.setEnabled(false);


        } else {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x04);
            }

            toggleSwitch.setEnabled(true);
            btnLevelOutlet.setEnabled(true);
        }
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
