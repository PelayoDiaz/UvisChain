package com.uniovi.uvis.entities.block;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

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
	}
	
	public Boolean isChainValid() {
		try {
			processChain();
		} catch (IllegalStateException e) {
			return false;
		}
		return true;
	}
	
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
	
	private void checkActualBlockHash(Block currentBlock) {
		if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
			throw new IllegalStateException("The block has been modified!");
		}
	}
	
	private void checkPreviousBlockHash(Block currentBlock, Block previousBlock) {
		if (currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
			throw new IllegalStateException("The previous block has been modified!");
		}
	}
	
}
