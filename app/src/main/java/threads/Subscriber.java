package threads;

import android.app.Activity;
import android.widget.Toast;

import com.anaptixi.flashandsound.FlashAndSound;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import CustomSettings.CustomSettings;

import static CustomSettings.CustomSettings.topic;

/**
 * Created by orfeas on 16/1/2018.
 */

public class Subscriber {
    private static MqttClient sampleClient = null;

    public static void subscribe(final FlashAndSound parent) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            if (sampleClient != null && sampleClient.isConnected()) {
                return;
            }

            sampleClient = new MqttClient(CustomSettings.broker, CustomSettings.clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(parent);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, 2);
            // sampleClient.isConnected();
            //Toast.makeText(this, "MQTT READY", Toast.LENGTH_LONG).show();

            parent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(parent, "MQTT READY", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MqttException me) {
            me.printStackTrace();

            parent.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(parent, "MQTT ERROR! ", Toast.LENGTH_SHORT).show();
                }
            });
            sampleClient = null;
        }
    }

    public static void unsubscribe(final FlashAndSound parent) {
        if (sampleClient != null && sampleClient.isConnected()) {
            try {
                sampleClient.disconnect();
                sampleClient.close();
                sampleClient = null;
            } catch (Exception ex) {

            }
        }

    }
}
