package com.uniovi.uvis;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uniovi.uvis.communication.NodeServer;
import com.uniovi.uvis.entities.block.Block;

@SpringBootApplication
public class UvisServerApplication {

	public static void main(String[] args) {
//		NodeServer server = new NodeServer();
//        try {
//			server.start(6666);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        
		SpringApplication.run(UvisServerApplication.class, args);
	}

}
