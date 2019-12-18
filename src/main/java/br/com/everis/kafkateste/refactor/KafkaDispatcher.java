package br.com.everis.kafkateste.refactor;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDispatcher implements Closeable {
	private final KafkaProducer<String, String> producer;

	KafkaDispatcher() {
		this.producer = new KafkaProducer<>(properties());
	}

	private static Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

	void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
		Callback callback = (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println("sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset "
					+ data.offset() + "/ timestamp " + data.timestamp());
		};
		producer.send(record, callback).get();
	}

	@Override
	public void close() {
		producer.close();
	}

}