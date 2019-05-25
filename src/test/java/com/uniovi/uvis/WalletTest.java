package com.uniovi.uvis;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.transactions.UTXOs;
import com.uniovi.uvis.entities.wallet.Wallet;

public class WalletTest {
	
	private Wallet walletA;
	private Wallet walletB;
	private TransactionOutput output1;
	private TransactionOutput output2;
	
	//Antes de cada prueba
	@Before
	public void setUp() throws Exception {
		walletA = new Wallet();
		walletB = new Wallet();
		output1 = new TransactionOutput(walletA.getPublicKey(), 3, null);
		output2 = new TransactionOutput(walletA.getPublicKey(), 0.5, null);
		UTXOs.getInstance().put(output1.getId(), output1);
		UTXOs.getInstance().put(output2.getId(), output2);	
	}

	// Despues de cada prueba
	@After
	public void tearDown() throws Exception {
		UTXOs.getInstance().remove(output1.getId());
		UTXOs.getInstance().remove(output2.getId());
	}

	@Test
	/**
	 * Checks if the wallet gets the total amount of funds available well. 
	 */
	public void getBalanceTest() {
		assertEquals(3.5, walletA.getBalance(), 0.01);
		assertEquals(0, walletB.getBalance(), 0.01);
		
		TransactionOutput output3 = new TransactionOutput(walletB.getPublicKey(), 4, null);
		UTXOs.getInstance().put(output3.getId(), output3);
		
		assertEquals(3.5, walletA.getBalance(), 0.01);
		assertEquals(4, walletB.getBalance(), 0.01);
		
		//Delete all the utxos for the next test
		UTXOs.getInstance().remove(output3.getId());
	}
	
	@Test
	/**
	 * Checks that a transaction can not be created because there aren't enough funds.
	 */
	public void noSendFundsTest() {
		assertNull(walletA.sendFunds(walletB.getPublicKey(), 4));
	}

	@Test
	/**
	 * Checks that a transaction can be created because there are enough funds.
	 */
	public void sendFundsTest() {
		Transaction transaction = walletA.sendFunds(walletB.getPublicKey(), 3.5);
		assertNotNull(transaction);
		assertEquals(3.5, transaction.getAmount(), 0.01);
		assertEquals(0, transaction.getOutputs().size());
	}
}
