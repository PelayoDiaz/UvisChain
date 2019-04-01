package com.uniovi.uvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uniovi.uvis.entities.block.Block;

@SpringBootApplication
public class UvisServerApplication {

	public static void main(String[] args) {
		Block block1 = new Block("0");
		System.out.println("block1: "+block1.getHash());
		Block block2 = new Block(block1.getHash());
		System.out.println("block2: "+block2.getHash());
//		SpringApplication.run(UvisServerApplication.class, args);
	}

}
