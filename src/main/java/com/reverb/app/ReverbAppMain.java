package com.reverb.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
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
