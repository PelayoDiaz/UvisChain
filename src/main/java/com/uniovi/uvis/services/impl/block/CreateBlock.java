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

	/**
	 * Constructor.
	 * 
	 * @param validTransactions
	 * 			The valid transactions to be included into the block.
	 */
	public CreateBlock(List<Transaction> validTransactions) {
		this.transactions = validTransactions; //Processed Transactions
	}

	@Override
	public Block execute() {
		if (this.transactions == null || this.transactions.size()==0) {
			return null;
		}
		Block block = new Block(BlockChain.getInstance().getLastBlock().getId());
		block.addTransactions(transactions);
		return block;
	}

}
