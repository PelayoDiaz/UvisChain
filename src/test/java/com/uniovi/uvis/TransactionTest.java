package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;

public class TransactionTest {

	@Test
	/**
	 * Checks that a chain is created with a mined genesis block.
	 */
	public void creatingATransactionTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
		assertFalse(transaction.verifySignature());
		walletA.signTransaction(transaction);
		assertTrue(transaction.verifySignature());
	}

}
