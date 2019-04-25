package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;

public class BlockChainTest {

	@Test
	/**
	 * Checks that a chain is created with a mined genesis block.
	 */
	public void creatingAChainTest() {
		BlockChain chain = new BlockChain();
		assertEquals(1, chain.length());
		assertEquals(true, chain.getLastBlock().isMined());
	}
	
	@Test
	/**
	 * Checks that a block cannot be added to a chain with the last block
	 * without being mined.
	 */
	public void addNewBlockWithPreviousNotMinedTest() {
		BlockChain chain = new BlockChain();
		assertEquals(1, chain.length());
		assertTrue(chain.getLastBlock().isMined());
		
		//We add the first block but we don't mined it so a second block cannot be added
		Block block1 = new Block(chain.getLastBlock().getHash());
		chain.addBlock(block1);
		assertEquals(2, chain.length());
		assertFalse(chain.getLastBlock().isMined());
		
		//Checks that the second block has not been added.
		Block block2 = new Block(chain.getLastBlock().getHash());
		chain.addBlock(block2);
		assertEquals(2, chain.length());
		assertFalse(chain.getLastBlock().isMined());
	}
	
	@Test
	/**
	 * Checks that a block cannot be added to a chain with the last block
	 * without being mined.
	 */
	public void addNewBlockWithPreviousMinedTest() {
		BlockChain chain = new BlockChain();
		assertEquals(1, chain.length());
		assertTrue(chain.getLastBlock().isMined());
		
		//We add the first block and we mined it so a second block can be added
		Block block1 = new Block(chain.getLastBlock().getHash());
		chain.addBlock(block1);
		assertEquals(2, chain.length());
		assertFalse(chain.getLastBlock().isMined());
		//Mine the block
		chain.getLastBlock().mine(4);
		assertTrue(chain.getLastBlock().isMined());
		
		//Checks that the second block has not been added.
		Block block2 = new Block(chain.getLastBlock().getHash());
		chain.addBlock(block2);
		assertEquals(3, chain.length());
		assertFalse(chain.getLastBlock().isMined());
	}

}
