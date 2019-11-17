package com.uniovi.uvis.services.impl.block;

import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * Starts the process of mining to include a new block into the chain.
 * It tries to mine a block by creating a thread.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class Mine implements Command<Void> {
	
	private Block block;
	private List<Transaction> originalTransactions;

	/**
	 * Constructor.
	 * 
	 * @param block
	 * 			The block to be mined
	 * @param originalTransactions
	 * 			The transactions to be stored in the block.
	 */
	public Mine(Block block, List<Transaction> originalTransactions) {
		this.block = block;
		this.originalTransactions = originalTransactions;
	}

	@Override
	public Void execute() {
		Miner miner = new Miner(block, originalTransactions);
		miner.start();
		return null;
	}

}
