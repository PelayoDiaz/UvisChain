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

}
