package com.uniovi.uvis.services.impl.block;

import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * Creates a new block with the valid transactions pending of being processed in the chain
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class CreateBlock implements Command<Block>{
	
	private List<Transaction> transactions;

	public CreateBlock(List<Transaction> validTransactions) {
		this.transactions = validTransactions; //Processed Transactions
	}

	@Override
	public Block execute() {
		Block block = new Block(BlockChain.getInstance().getLastBlock().getId());
		block.addTransactions(transactions);
		return block;
	}

}
