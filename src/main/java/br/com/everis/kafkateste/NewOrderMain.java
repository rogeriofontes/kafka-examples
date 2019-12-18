package br.com.everis.kafkateste;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {
	 private static KafkaProducer<String, String> producer = null;
	 
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		producer = new KafkaProducer<>(properties());
		String topic =  "ECOMMERCE_NEW_ORDER";
		String key = UUID.randomUUID().toString();
		String value = key + ", 2323009999, 3232323003999";
		String topicEmail = "ECOMMERCE_SEND_EMAIL";
		
		String email = "Estamos processando sua order";
		
		Callback callback = (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			}
			System.out.println(data.topic() + ":::partition: " + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp() );
		};
		
		ProducerRecord<String, String> record = new ProducerRecord<>(topic , key, value);
		producer.send(record, callback).get();
		
		ProducerRecord<String, String> emailRecord = new ProducerRecord<>(topicEmail , key, email);
		producer.send(emailRecord, callback).get();
	}

	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

}
