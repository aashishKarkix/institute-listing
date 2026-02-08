package com.institute.listing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ListingApplication {

	public static void main(String[] args) {
		System.out.println("CICD is running");
		SpringApplication.run(ListingApplication.class, args);
	}

}
