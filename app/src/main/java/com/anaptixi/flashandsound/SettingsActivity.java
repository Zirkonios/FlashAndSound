package com.anaptixi.flashandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity {

    private Button retButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public void takeIp(View view) {

    }

    retButton = (Button) findViewById(R.id.retButton);
        retButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            return;
        }
    });
}
