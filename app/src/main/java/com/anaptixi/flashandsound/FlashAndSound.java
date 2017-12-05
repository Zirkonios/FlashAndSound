package com.anaptixi.flashandsound;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import CustomSettings.CustomSettings;

import static CustomSettings.CustomSettings.topic;


public class FlashAndSound extends AppCompatActivity implements MqttCallback {

    private Switch switFlash;
    private Switch switSound;
    private Button OffButton;
    private Camera camera;
    private boolean FlashCon = false;
    private boolean hasFlash = false;
    private Parameters params;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_and_sound);

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(FlashAndSound.this).create();
            alert.setTitle("Error");
            alert.setMessage("Device has no flash light!");
            alert.setButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        }

        switFlash = (Switch) findViewById(R.id.switchflash);
        switFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switFlash.isChecked()) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

        switSound = (Switch) findViewById(R.id.switchSound);
        switSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switSound.isChecked()) {
                    playSound();
                } else {
                    stopSound();
                }
            }
        });

        OffButton = (Button) findViewById(R.id.OffButton);
        OffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switFlash.isChecked()) {
                    turnOffFlash();
                }
                if (switSound.isChecked()) {
                    stopSound();
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.flashicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getCamera();


        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(CustomSettings.broker, CustomSettings.clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(this);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, 2);
            Toast.makeText(this, "MQTT READY", Toast.LENGTH_LONG).show();
        } catch (MqttException me) {
            me.printStackTrace();

            Toast.makeText(this, "MQTT ERROR", Toast.LENGTH_LONG).show();
        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!FlashCon) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            FlashCon = true;

            switFlash.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_settings:
                Intent aboutIntent = new Intent(FlashAndSound.this, SettingsActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.id_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void turnOffFlash() {
        if (FlashCon) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            FlashCon = false;

            switFlash.setChecked(false);
        }
    }

    private void playSound() {
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.holmusic);
            mp.start();
        }
        switSound.setChecked(true);
    }

    private void stopSound() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }

        switSound.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        turnOffFlash();
        stopSound();

        if (camera != null) {
            camera.release();
            camera = null;
        }

        if (mp != null) {
            mp.release();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // ******************************************************************************
    // ++++++                         M Q T T  callbacks                       ++++++
    // ******************************************************************************
    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String data = new String(message.getPayload());

        if (data.equals("fon")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    turnOnFlash();
                }
            });
        }
        if (data.equals("foff")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    turnOffFlash();
                }
            });
        }
        if (data.equals("son")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playSound();
                }
            });
        }
        if (data.equals("soff")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stopSound();
                }
            });
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
