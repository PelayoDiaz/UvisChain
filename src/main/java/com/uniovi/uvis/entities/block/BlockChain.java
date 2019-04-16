package com.uniovi.uvis.entities.block;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockChain implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1713493104111662876L;

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
		if (getLastBlock().isMined()) {
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
	public Boolean isChainValid() {
		try {
			processChain();
		} catch (IllegalStateException e) {
			return new Boolean(false);
		}
		return new Boolean(true);
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
	 * Returns the last block in the chain.
	 * 
	 * @return Block
	 * 		the last block in the chain.
	 */
	public Block getLastBlock() {
		return this.chain.get(this.chain.size()-1);
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
		if (currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
			throw new IllegalStateException("The previous block has been modified!");
		}
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
	
}
