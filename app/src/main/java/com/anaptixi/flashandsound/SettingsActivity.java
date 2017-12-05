package com.anaptixi.flashandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import CustomSettings.CustomSettings;

import static CustomSettings.CustomSettings.topic;


public class SettingsActivity extends AppCompatActivity {

    private Button buttonA;
    private Button buttonB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText e1 = findViewById(R.id.editTextIP);
        final EditText e2 = findViewById(R.id.editTextPort);

        try {
            e1.setText(CustomSettings.broker.replace("tcp://", "").split(":")[0]);
        } catch (Exception ex) {

        }

        try {
            e2.setText(CustomSettings.broker.replace("tcp://", "").split(":")[1]);
        } catch (Exception ex) {

        }

        buttonA = findViewById(R.id.button);
        buttonB = findViewById(R.id.button2);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = e1.getText().toString();
                String port = e2.getText().toString();

                String broker = "tcp://" + ip + ":" + port;
                CustomSettings.broker = broker;
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = e1.getText().toString();
                String port = e2.getText().toString();

                String broker = "tcp://" + ip + ":" + port;
                CustomSettings.broker = broker;
            }
        });
    }
}
