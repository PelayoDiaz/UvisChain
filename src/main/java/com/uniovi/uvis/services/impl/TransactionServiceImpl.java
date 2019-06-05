package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public TransactionServiceImpl() {
		this.executor = new CommandExecutor();
	}

}
