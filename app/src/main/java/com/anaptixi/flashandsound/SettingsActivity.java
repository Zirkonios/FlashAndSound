package com.anaptixi.flashandsound;

import android.opengl.Visibility;
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
import threads.Subscriber;

import static CustomSettings.CustomSettings.topic;


public class SettingsActivity extends AppCompatActivity {
    public static FlashAndSound parent;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonD;
    private Button buttonE;
    private Button buttonF;

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
        buttonC = findViewById(R.id.button3);
        buttonD = findViewById(R.id.button4);
        buttonE = findViewById(R.id.button5);
        buttonF = findViewById(R.id.button6);

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

        buttonC.setOnClickListener(new View.OnClickListener() { // manual
            @Override
            public void onClick(View v) {
                CustomSettings.autoflag = false;
                buttonE.setVisibility(View.VISIBLE);
                buttonF.setVisibility(View.VISIBLE);
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() { // auto
            @Override
            public void onClick(View v) {
                CustomSettings.autoflag = true;
                buttonE.setVisibility(View.INVISIBLE);
                buttonF.setVisibility(View.INVISIBLE);
            }
        });

        buttonE.setOnClickListener(new View.OnClickListener() { // auto
            @Override
            public void onClick(View v) {
                Subscriber.subscribe(parent);
            }
        });

        buttonF.setOnClickListener(new View.OnClickListener() { // auto
            @Override
            public void onClick(View v) {
                Subscriber.unsubscribe(parent);
            }
        });

        if (CustomSettings.autoflag) {
            buttonE.setVisibility(View.INVISIBLE);
            buttonF.setVisibility(View.INVISIBLE);
        } else {
            buttonE.setVisibility(View.VISIBLE);
            buttonF.setVisibility(View.VISIBLE);
        }
    }
}
