package com.uniovi.uvis.commandTests.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.services.impl.transaction.CheckTransaction;

public class CheckTransactionTest extends AbstractTest {

	@Test
	/**
	 * It checks that a transaction is valid.
	 */
	public void checkTransactionValidTest() {
		TransactionDto transactionDto = createValidTransaction(5).toDto();
		assertTrue(executor.execute(new CheckTransaction(transactionDto)));
	}
	
	@Test
	/**
	 * Checks that a transaction is invalid because there is no sender
	 * with this direction
	 */
	public void transactionInvalidNoSenderAddressTest() {
		TransactionDto transactionDto = createValidTransaction(5).toDto();
		transactionDto.senderAddress = "modificado";
		assertFalse(executor.execute(new CheckTransaction(transactionDto)));
	}
	
	@Test
	/**
	 * Checks that a transaction is invalid because there is no sender
	 * with this direction
	 */
	public void transactionInvalidNoReceiverAddressTest() {
		TransactionDto transactionDto = createValidTransaction(5).toDto();
		transactionDto.receiver = "modificado";
		assertFalse(executor.execute(new CheckTransaction(transactionDto)));
	}
	
	@Test
	/**
	 * Checks that a transaction is invalid because the amount of money to be sent
	 * is less than the minimum allowed.
	 */
	public void transactionInvalidAmountLessThanMinimum() {
		TransactionDto transactionDto = createValidTransaction(5).toDto();
		transactionDto.amount = 0.001;
		assertFalse(executor.execute(new CheckTransaction(transactionDto)));
	}

}
