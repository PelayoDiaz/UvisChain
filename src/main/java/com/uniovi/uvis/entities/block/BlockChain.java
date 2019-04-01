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
		Block currentBlock;
		Block previousBlock;
		
		for (int i=1; i<chain.size(); i++) {
			currentBlock = chain.get(i);
			previousBlock = chain.get(i-1);
			
			assertActualBlockHash(currentBlock);
		}
	}
	
	private void assertActualBlockHash(Block currentBlock) {
		t
	}
	
}
