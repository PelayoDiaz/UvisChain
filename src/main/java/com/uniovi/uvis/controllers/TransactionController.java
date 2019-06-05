package com.uniovi.uvis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.TransactionServiceImpl;


/**
 * It controlls all the messages related to create and send transactions
 * 
 * @author Pelayo Díaz Soto
 *
 */
@RestController
public class TransactionController {
	
	@Autowired
	private TransactionServiceImpl transactionService;

	
	

}
