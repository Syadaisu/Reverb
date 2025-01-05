package com.reverb.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;
import java.net.URI;

@SpringBootApplication // Explicitly define entity package
public class ReverbAppMain {

	public static void main(String[] args) {
		SpringApplication.run(ReverbAppMain.class, args);
		System.out.println("Hello World!");
	}

	@Bean
	public CommandLineRunner openSwaggerUi() {
		return args -> {
			try {
				String url = "http://localhost:8181/swagger-ui/index.html#/";
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().browse(new URI(url));
				} else {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}
