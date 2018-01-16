package threadsupport;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import CustomSettings.CustomSettings;

public class SuperPublisher implements Runnable {
    private String answer;
    private String topic = CustomSettings.topic2;
    private String clientId = CustomSettings.clientId2;

    public SuperPublisher(String answer) {
        this.answer = answer;
    }

    public void run() {
        MemoryPersistence persistence = new MemoryPersistence();
        String broker = CustomSettings.broker;

        try {
            MqttClient sampleClient= new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts= new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            MqttMessage message = new MqttMessage(answer.getBytes());
            message.setQos(2);

            sampleClient.publish(topic, message);
            sampleClient.disconnect();
            sampleClient.close();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}

