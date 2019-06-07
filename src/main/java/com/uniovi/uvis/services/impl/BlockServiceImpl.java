package com.uniovi.uvis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.BlockService;
import com.uniovi.uvis.services.impl.block.CreateBlock;
import com.uniovi.uvis.services.impl.command.CommandExecutor;
import com.uniovi.uvis.services.impl.transaction.GetValidTransactions;;

@Service
public class BlockServiceImpl implements BlockService {
	
	/** Code executor for services. */
	private CommandExecutor executor;
	
	public BlockServiceImpl() {
		this.executor = new CommandExecutor();
	}

	@Override
	public Block createBlock() { 
		List<Transaction> validTransactions = executor.execute(new GetValidTransactions());
		return executor.execute(new CreateBlock(validTransactions));
	}

}
