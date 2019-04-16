package com.uniovi.uvis;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.uniovi.uvis.entities.block.Block;

public class BlockTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Checks that the blocks are created in an appropriate way.
	 */
	public void creatingBlocksTests() {
		Block genesisBlock = new Block("0");
		Block secondBlock = new Block(genesisBlock.getHash());
		Block thirdBlock = new Block(secondBlock.getHash());
		
		assertEquals("0", genesisBlock.getPreviousHash());
		assertEquals(genesisBlock.getHash(), secondBlock.getPreviousHash());
		assertEquals(secondBlock.getHash(), thirdBlock.getPreviousHash());
	}
	
	@Test
	/**
	 * Checks that the blocks can be mined correctly.
	 */
	public void miningBlocksTests() {
		Block genesisBlock = new Block("0");
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getHash().startsWith("00"));
		
		Block secondBlock = new Block(genesisBlock.getHash());
		secondBlock.mine(3);
		assertTrue(secondBlock.isMined());
		assertTrue(secondBlock.getHash().startsWith("000"));
		
		Block thirdBlock = new Block(secondBlock.getHash());
		thirdBlock.mine(5);
		assertTrue(thirdBlock.isMined());
		assertTrue(thirdBlock.getHash().startsWith("00000"));
	}
	
	@Test
	/**
	 * Checks that a mined block can not be mined again.
	 */
	public void miningMinedBlockTest() {
		Block genesisBlock = new Block("0");
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getHash().startsWith("00"));
		String previousHash = genesisBlock.getHash();
		
		genesisBlock.mine(3);
		assertEquals(previousHash, genesisBlock.getHash());
	}

}
