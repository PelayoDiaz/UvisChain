package com.uniovi.uvis.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.Block;

public class BlockTest extends AbstractTest {

	@Test
	/**
	 * Checks that the blocks are created in an appropriate way.
	 */
	public void creatingBlocksTests() {
		Block genesisBlock = new Block("0");
		Block secondBlock = new Block(genesisBlock.getId());
		Block thirdBlock = new Block(secondBlock.getId());
		
		assertEquals("0", genesisBlock.getPreviousHash());
		assertEquals(genesisBlock.getId(), secondBlock.getPreviousHash());
		assertEquals(secondBlock.getId(), thirdBlock.getPreviousHash());
	}
	
	@Test
	/**
	 * Checks that the blocks can be mined correctly.
	 */
	public void miningBlocksTests() {
		Block genesisBlock = new Block("0");
		genesisBlock.addTransactions(this.createTransactionsList());
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getId().startsWith("00"));
		
		Block secondBlock = new Block(genesisBlock.getId());
		secondBlock.addTransactions(this.createTransactionsList());
		secondBlock.mine(3);
		assertTrue(secondBlock.isMined());
		assertTrue(secondBlock.getId().startsWith("000"));
		
		Block thirdBlock = new Block(secondBlock.getId());
		thirdBlock.addTransactions(this.createTransactionsList());
		thirdBlock.mine(4);
		assertTrue(thirdBlock.isMined());
		assertTrue(thirdBlock.getId().startsWith("0000"));
	}
	
	@Test
	/**
	 * Checks that a mined block can not be mined again.
	 */
	public void miningMinedBlockTest() {
		Block genesisBlock = new Block("0");
		genesisBlock.addTransactions(this.createTransactionsList());
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getId().startsWith("00"));
		String previousHash = genesisBlock.getId();
		
		genesisBlock.mine(3);
		assertEquals(previousHash, genesisBlock.getId());
	}
	
	@Test
	/**
	 * Checks that a block can not be mined withou transactions.
	 */
	public void miningBlockWithoutTransactionsTest() {
		Block genesisBlock = new Block("0");
		genesisBlock.mine(2);
		assertFalse(genesisBlock.isMined());
	}

}
