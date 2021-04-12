package com.example.contactlistserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactListServerApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(ContactListServerApplication.class, args);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
