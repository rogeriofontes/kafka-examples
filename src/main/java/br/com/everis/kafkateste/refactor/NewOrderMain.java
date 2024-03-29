package br.com.everis.kafkateste.refactor;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		try (KafkaDispatcher dispatcher = new KafkaDispatcher()) {
			for (int i = 0; i < 10; i++) {

				String key = UUID.randomUUID().toString();
				String value = key + ",67523,1234";
				dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

				String email = "Thank you for your order! We are processing your order!";
				dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
			}
		}
	}
}
