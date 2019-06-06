package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.services.TransactionService;
import com.uniovi.uvis.services.impl.command.CommandExecutor;

@Service
public class TransactionServiceImpl implements TransactionService {

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public TransactionServiceImpl() {
		this.executor = new CommandExecutor();
	}

}
