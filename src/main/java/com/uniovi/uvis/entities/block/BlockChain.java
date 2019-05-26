package com.uniovi.uvis.entities.block;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;

public class BlockChain implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1713493104111662876L;
	
	/** The minimum value allowed to be sent */
	public static final double MINIMUM_TRANSACTION = 0.1;
	
	public static final int DIFFICULTY = 4;

	/** The BlockChain which contains all the blocks. */
	private List<Block> chain;

	public BlockChain() {
		this.chain = new ArrayList<Block>();
		this.chain.add(new Block("0")); //Genesis Block
		this.getLastBlock().mine(4);
	}
	
	/**
	 * Adds a new block to the chain. To do this, the last Block
	 * into the chain has to be mined.
	 * 
	 * @param block
	 * 			the block to be added.
	 */
	public void addBlock(Block block) {
		block.mine(DIFFICULTY);
		if (getLastBlock().isMined() && block.isMined()) { 
			this.chain.add(block);
		}
	}
	
	/**
	 * Returns true if the chain is valid.  False if not
	 * The chain's validity depends on different conditions.
	 * 
	 * @return Boolean
	 * 		true if the chain is valid.  False if not.
	 */
	public boolean isChainValid() {
		try {
			processChain();
		} catch (IllegalStateException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Process all the blocks of the chain and checks the different
	 * conditions for a block to be valid.
	 */
	private void processChain() {
		Block currentBlock;
		Block previousBlock;
		
		for (int i=1; i<chain.size(); i++) {
			currentBlock = chain.get(i);
			previousBlock = chain.get(i-1);
			
			checkActualBlockHash(currentBlock);
			checkPreviousBlockHash(currentBlock, previousBlock);
			checkIfActualIsMined(currentBlock);
			checkTransactions(currentBlock);
		}
	}
	
	private void checkTransactions(Block currentBlock) {
		for (Transaction transaction : currentBlock.getTransactions()) {
			//Checks if the Transaction is verified
			if (!transaction.verifySignature()) {
				throw new IllegalStateException("Signature in transaction is invalid!");
			}
			//Checks if the inputs or outputs have been modified
			if (transaction.getInputsValue() != transaction.getOutputsValue()) {
				throw new IllegalStateException("Inputs are not equals to outputs in transaction");
			}
			if (!transaction.getOutputs().get(0).belongsTo(transaction.getReceiver())) {
				throw new IllegalStateException("Output receiver is not who it should be");
			}
			if (transaction.getOutputs().get(1)!=null && !transaction.getOutputs().get(1).belongsTo(transaction.getReceiver())) {
				throw new IllegalStateException("The left over of the transaction is not for the sender");
			}
		}
		
	}

	/**
	 * Checks if the actual block is mined or not.
	 * 
	 * @param currentBlock
	 * 			the block to check.
	 */
	private void checkIfActualIsMined(Block currentBlock) {
		if (!currentBlock.isMined()) {
			throw new IllegalStateException("The block is not Mined!");
		}
	}

	/**
	 * Checks that the current block hasn't been modified. For this purpose
	 * it checks if the saved hash is equals to the resultant calculated hash.
	 * 
	 * @param currentBlock
	 * 		the block to check.
	 */
	private void checkActualBlockHash(Block currentBlock) {
		if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
			throw new IllegalStateException("The block has been modified!");
		}
	}
	
	/**
	 * Checks that the chain has not been altered or the blocks into modified.
	 * It checks the saved hash from the current block with the calculated hash
	 * from the previous block.
	 * 
	 * @param currentBlock
	 * 			the actual block int the chain
	 * @param previousBlock
	 * 			the previous block in the chain
	 */
	private void checkPreviousBlockHash(Block currentBlock, Block previousBlock) {
		if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
			throw new IllegalStateException("The previous block has been modified!");
		}
	}
	
	/**
	 * Returns the last block in the chain.
	 * 
	 * @return Block
	 * 		the last block in the chain.
	 */
	public Block getLastBlock() {
		return this.chain.get(this.chain.size()-1);
	}
	
	/**
	 * Returns the length of the chain.
	 * 
	 * @return int
	 * 		the length of the chain.
	 */
	public int length() {
		return this.chain.size();
	}
	
	@Override
	public String toString() {
		String gsonChain = new GsonBuilder().setPrettyPrinting().create().toJson(this.chain);
		return gsonChain;
	}
	
}
