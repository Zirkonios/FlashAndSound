package com.anaptixi.flashandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import CustomSettings.CustomSettings;
import threadsupport.SuperPublisher;

public class FrequencyActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency);

        final EditText e1 = findViewById(R.id.editTextFrequency);

        try {
            e1.setText(CustomSettings.frequency);
        } catch (Exception ex) {

        }

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frequency = e1.getText().toString();

                CustomSettings.frequency = frequency;

                sendCommand(frequency);
            }
        });
    }

    private static void sendCommand(String answer) {
        Log.i("FrequencyActivity", "sendCommand: " + answer);
        SuperPublisher publisher = new SuperPublisher(answer);
        Thread backgroundThread = new Thread(publisher);
        backgroundThread.start();
    }
}
