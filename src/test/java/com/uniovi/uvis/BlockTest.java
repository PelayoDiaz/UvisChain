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
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getId().startsWith("00"));
		
		Block secondBlock = new Block(genesisBlock.getId());
		secondBlock.mine(3);
		assertTrue(secondBlock.isMined());
		assertTrue(secondBlock.getId().startsWith("000"));
		
		Block thirdBlock = new Block(secondBlock.getId());
		thirdBlock.mine(5);
		assertTrue(thirdBlock.isMined());
		assertTrue(thirdBlock.getId().startsWith("00000"));
	}
	
	@Test
	/**
	 * Checks that a mined block can not be mined again.
	 */
	public void miningMinedBlockTest() {
		Block genesisBlock = new Block("0");
		genesisBlock.mine(2);
		assertTrue(genesisBlock.isMined());
		assertTrue(genesisBlock.getId().startsWith("00"));
		String previousHash = genesisBlock.getId();
		
		genesisBlock.mine(3);
		assertEquals(previousHash, genesisBlock.getId());
	}

}
