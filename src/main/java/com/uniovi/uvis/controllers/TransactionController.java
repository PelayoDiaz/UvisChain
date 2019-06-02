package com.uniovi.uvis.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uniovi.uvis.entities.dto.TransactionDto;

/**
 * It controlls all the messages related to create and send transactions
 * 
 * @author Pelayo Díaz Soto
 *
 */
@Controller
public class TransactionController {

	@MessageMapping("/chain.sendTransaction")
	@SendTo("/topic/transactions")
	public TransactionDto createTransaction(TransactionDto transaction) {
		TransactionDto transaction2 = new TransactionDto();
//		transaction2.setContent("volvemos");
		return transaction2;
	}

}
