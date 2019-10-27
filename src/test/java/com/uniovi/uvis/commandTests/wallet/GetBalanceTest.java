package com.uniovi.uvis.commandTests.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.wallet.GetBalance;

public class GetBalanceTest extends AbstractTest {

	private Wallet walletA;
	private Wallet walletB;

	private TransactionOutput output1;
	private TransactionOutput output2;

	// Antes de cada prueba
	@Before
	public void setUp() throws Exception {
		walletA = this.createValidWallet("walletA", "direccion1", "userA");
		walletB = this.createValidWallet("walletB", "direccion2", "userB");

		output1 = new TransactionOutput(walletA.getAddress(), 3, null);
		output2 = new TransactionOutput(walletA.getAddress(), 0.5, null);

		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		BlockChain.getInstance().putUTXO(output2.getId(), output2);
	}

	@Test
	/**
	 * Checks if the wallet gets the total amount of funds available well.
	 */
	public void getBalanceTest() {
		assertEquals(3.5, executor.execute(new GetBalance(this.walletA)), 0.01);
		assertEquals(0, executor.execute(new GetBalance(this.walletB)), 0.01);
		
		TransactionOutput output3 = new TransactionOutput(walletB.getAddress(), 4, null);
		BlockChain.getInstance().putUTXO(output3.getId(), output3);
		
		assertEquals(3.5, executor.execute(new GetBalance(this.walletA)), 0.01);
		assertEquals(4, executor.execute(new GetBalance(this.walletB)), 0.01);
	}

}
