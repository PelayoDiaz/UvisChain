package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
}
