package org.example.receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReceiverPropsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiverPropsApplication.class, args);
	}

}
