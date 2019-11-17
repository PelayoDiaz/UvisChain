package com.uniovi.uvis.services;

import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.transactions.Transaction;

/**
 * Interface for all the operations related to manipulate a block
 * 
 * @author Pelayo Díaz Soto
 *
 */
public interface BlockService {

	/**
	 * It creates a block to be mined when stored into the blockchain
	 * 
	 * @return Block
	 * 			The new created block
	 */
	public Block createBlock();
	
	/**
	 * It tries to mine a block to be added to the chain.
	 * 
	 * @param block
	 * 			The block to be added to the chain if mined goes successfully.
	 * 
	 * @param originalTransactions
	 * 			The block to be added to the chain if mined goes successfully.
	 * 
	 * @return BlockDto
	 * 			The information of the generated node
	 */
	public void mine(Block block, List<Transaction> originalTransactions);
}
