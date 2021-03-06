
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
public class ControlActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

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
        setContentView(R.layout.activity_control);

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
        viewModel = ViewModelProviders.of(this).get(BlinkyViewModel.class);
        viewModel.connect(device);

        // Set up views
        labelOutlet = findViewById(R.id.labelOutlet);

        final LinearLayout progressContainer = findViewById(R.id.progress_container);
        final TextView connectionState = findViewById(R.id.connection_state);
        final View content = findViewById(R.id.device_container);

        btnLevelOutlet = findViewById(R.id.btn_level);
        btnLevelOutlet.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_level);
            dialog.findViewById(R.id.btn_level_1).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_1);
                currentLevel = 1;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_2).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_2);
                currentLevel = 2;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_3).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_3);
                currentLevel = 3;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_4).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_4);
                currentLevel = 4;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_5).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_5);
                currentLevel = 5;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_6).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_6);
                currentLevel = 6;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_7).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_7);
                currentLevel = 7;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_8).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_8);
                currentLevel = 8;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_9).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_9);
                currentLevel = 9;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btn_level_10).setOnClickListener(v -> {
                btnLevelOutlet.setText(R.string.level_10);
                currentLevel = 10;
                viewModel.writeLevel(currentLevel);
                dialog.dismiss();
            });


            dialog.show();
        });

        toggleSwitch = findViewById(R.id.switch_button);
        toggleSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            updateButton(isChecked, true, false);
        });

        randomSwitch = findViewById(R.id.switch_random);
        randomSwitch.setOnCheckedChangeListener((view, isChecked) -> {
            updateRandomButton(isChecked, true, false);
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
        viewModel.getLevelState().observe(this, level -> btnLevelOutlet.setText("Level " + level));
        viewModel.getModeState().observe(this, mode -> {

            switch (mode) {
                case 0x01:
                    this.updateButton(true, false, true);
                    this.labelOutlet.setVisibility(View.VISIBLE);
                    break;
                case 0x02:
                    this.updateButton(false, false, true);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                case 0x03:
                    this.updateRandomButton(true, false, true);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                case 0x04:
                    this.updateRandomButton(false, false, true);
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
                default:
                    this.labelOutlet.setVisibility(View.INVISIBLE);
                    break;
            }
        });

    }

    void updateButton(boolean isChecked, boolean isUpdate, boolean updateSwitch) {
        if (isChecked) {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x01);
            }
            randomSwitch.setEnabled(false);
            randomSwitch.setChecked(false);
        } else {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x02);
            }

            randomSwitch.setEnabled(true);
        }

        if (updateSwitch) {
            toggleSwitch.setChecked(isChecked);
        }
    }


    void updateRandomButton(boolean isCheckedRandom, boolean isUpdate, boolean updateSwitch) {
        if (isCheckedRandom) {
            if (isUpdate) {

                viewModel.writeMode((byte) 0x03);
            }
            toggleSwitch.setEnabled(false);
            toggleSwitch.setChecked(false);
            btnLevelOutlet.setEnabled(false);
            btnLevelOutlet.setBackground(getResources().getDrawable(R.drawable.start_stop_button_background_disabled));

        } else {
            if (isUpdate) {
                viewModel.writeMode((byte) 0x04);
            }

            toggleSwitch.setEnabled(true);
            btnLevelOutlet.setEnabled(true);
            btnLevelOutlet.setBackground(getResources().getDrawable(R.drawable.start_stop_button_background));
        }
        if (updateSwitch) {
            randomSwitch.setChecked(isCheckedRandom);
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

}
