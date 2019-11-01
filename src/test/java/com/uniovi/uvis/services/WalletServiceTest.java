package com.uniovi.uvis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.WalletServiceImpl;

public class WalletServiceTest extends AbstractTest {

	private Wallet walletA;
	private Wallet walletB;

	private TransactionOutput output1;
	private TransactionOutput output2;
	
	private WalletService walletService;

	// Antes de cada prueba
	@Before
	public void setUp() throws Exception {
		walletA = this.createValidWallet("walletA", "direccion1", "userA");
		walletB = this.createValidWallet("walletB", "direccion2", "userB");

		output1 = new TransactionOutput(walletA.getAddress(), 3, null);
		output2 = new TransactionOutput(walletA.getAddress(), 0.5, null);

		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		BlockChain.getInstance().putUTXO(output2.getId(), output2);
		
		walletService = new WalletServiceImpl();
	}

	@Test
	/**
	 * Checks that the sendFunds command is executed only if the transaction is valid
	 */
	public void sendFundsInvalidTransactionTest() {
		TransactionDto transactionDto = this.createValidTransaction(5).toDto();
		transactionDto.senderAddress = "modificado";
		
		Transaction transaction = walletService.sendFunds(transactionDto);
		assertNull(transaction);
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
	}
	
	@Test
	/**
	 * Checks that the sendFunds command is executed only if there is balance enough.
	 */
	public void sendFundsNotBalanceEnoughTest() {
		TransactionDto dto = new TransactionDto();
		dto.senderAddress = walletA.getAddress();
		dto.receiver = walletB.getAddress();
		dto.amount = 3.6;
		
		Transaction transaction = walletService.sendFunds(dto);
		assertNull(transaction);
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
	}
	
	@Test
	/**
	 * Checks that the sendFunds command is executed when there is the exact balance.
	 */
	public void sendFundsExactBalanceTest() {
		TransactionDto dto = new TransactionDto();
		dto.senderAddress = walletA.getAddress();
		dto.receiver = walletB.getAddress();
		dto.amount = 3.5;
		
		Transaction transaction = walletService.sendFunds(dto);
		assertNotNull(transaction);
		assertEquals(3.5, transaction.getAmount(), 0.01);
		assertEquals(0, transaction.getOutputs().size());
		assertEquals(1, BlockChain.getInstance().getPendingTransactions().size());
		assertEquals(transaction, BlockChain.getInstance().getPendingTransactions().get(0));
	}

	@Test
	/**
	 * Checks that the sendFunds command is executed when there is more balance than the needed.
	 */
	public void sendFundsMoreBalanceTest() {
		TransactionDto dto = new TransactionDto();
		dto.senderAddress = walletA.getAddress();
		dto.receiver = walletB.getAddress();
		dto.amount = 3;
		
		Transaction transaction = walletService.sendFunds(dto);
		assertNotNull(transaction);
		assertEquals(3, transaction.getAmount(), 0.01);
		assertEquals(0, transaction.getOutputs().size());
		assertEquals(1, BlockChain.getInstance().getPendingTransactions().size());
		assertEquals(transaction, BlockChain.getInstance().getPendingTransactions().get(0));
	}
}
