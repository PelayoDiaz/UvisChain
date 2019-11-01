package com.uniovi.uvis.commandTests.wallet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.wallet.SendFunds;
import com.uniovi.uvis.services.impl.wallet.SendPrize;

public class SendFundsAndPrizeTest extends AbstractTest {

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
	 * Checks if the new transaction is created and added to the list of pending transactions.
	 */
	public void sendFundsTest() {
		Transaction transaction = executor.execute(new SendFunds(walletA, walletB.getAddress(), 3.5));
		assertNotNull(transaction);
		assertEquals(3.5, transaction.getAmount(), 0.01);
		assertEquals(0, transaction.getOutputs().size());
		assertEquals(1, BlockChain.getInstance().getPendingTransactions().size());
		assertEquals(transaction, BlockChain.getInstance().getPendingTransactions().get(0));
	}
	
	@Test
	/**
	 * Checks that a transaction can not be created because it has null parameters.
	 */
	public void sendFundsNullTest() {
		//The transaction can not be created because the sender is null.
		assertNull(executor.execute(new SendFunds(null, walletB.getAddress(), 3.5)));
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
		
		//The transaction can not be created because the receiver is null.
		assertNull(executor.execute(new SendFunds(walletA, null, 3.5)));
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
	}
	
	@Test
	/**
	 * Checks if the new transaction is created and added to the list of pending transactions.
	 */
	public void sendPrizeTest() {
		Transaction transaction = executor.execute(new SendPrize(walletA, walletB.getAddress(), 3.5));
		assertNotNull(transaction);
		assertEquals(3.5, transaction.getAmount(), 0.01);
		assertEquals(0, transaction.getOutputs().size());
		assertEquals(1, BlockChain.getInstance().getPendingTransactions().size());
		assertEquals(transaction, BlockChain.getInstance().getPendingTransactions().get(0));
	}
	
	@Test
	/**
	 * Checks that a transaction can not be created because it has null parameters.
	 */
	public void sendPrizeNullTest() {
		//The transaction can not be created because the sender is null.
		assertNull(executor.execute(new SendPrize(null, walletB.getAddress(), 3.5)));
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
		
		//The transaction can not be created because the receiver is null.
		assertNull(executor.execute(new SendPrize(walletA, null, 3.5)));
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
	}

}
