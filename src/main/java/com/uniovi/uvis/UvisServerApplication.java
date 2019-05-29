package com.uniovi.uvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uniovi.uvis.entities.block.Block;

@SpringBootApplication
public class UvisServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UvisServerApplication.class, args);
	}

}
