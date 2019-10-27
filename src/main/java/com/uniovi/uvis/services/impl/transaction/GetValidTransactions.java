package com.uniovi.uvis.services.impl.transaction;

import java.util.List;
import java.util.stream.Collectors;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * Returns the transactions which are valid and can be added to the next Block.
 * It checks all the transactions of the chain which are waiting for being included in a block.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class GetValidTransactions implements Command<List<Transaction>>{
	
	
	@Override
	public List<Transaction> execute() {
		List<Transaction> validTransactions = BlockChain.getInstance().getPendingTransactions()
				.stream().filter(x -> x.isValid()).collect(Collectors.toList());
		return validTransactions;
	}

}
