package com.uniovi.uvis.commandTests.transaction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.transaction.GetValidTransactions;

public class GetValidTransactionTest extends AbstractTest {

	@Test
	/**
	 * It checks that the commands returns a list with the valid transactions
	 */
	public void validPendingTransactionTest() {
		//At the beginning the list of pending transaction must be empty
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
		
		BlockChain.getInstance().addPendingTransaction(this.createValidTransaction(5));
		
		assertEquals(1, executor.execute(new GetValidTransactions()).size());
	}
	
	@Test
	/**
	 * It checks that it doesn't returns those transactions whose signature is invalid.
	 */
	public void invalidSignatureTransactionTest() {
		//At the beginning the list of pending transaction must be empty
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
		Transaction transaction = this.createValidTransaction(5);
		TransactionDto dto = transaction.toDto();
		dto.amount = 1;
		//The transaction has been modified so it is not returned.
		BlockChain.getInstance().addPendingTransaction(new Transaction(dto));
		
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
	}
	
	@Test
	/**
	 * It checks that it doesn't returns those transactions whose amount of
	 * funds is less than the minimum allowed.
	 */
	public void amountLessThanMinimumTransactionTest() {
		//At the beginning the list of pending transaction must be empty
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
		Transaction transaction = this.createValidTransaction(5);
		TransactionDto dto = transaction.toDto();
		dto.amount = 0.009;
		//The transaction has been modified so it is not returned.
		BlockChain.getInstance().addPendingTransaction(new Transaction(dto));
		
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
	}
	
	@Test
	/**
	 * It checks that it doesn't returns those transactions whose inputs value
	 * are less than the amount to be sent.
	 */
	public void amountGreaterThanInputValueTransactionTest() {
		//At the beginning the list of pending transaction must be empty
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
		Transaction transaction = this.createValidTransaction(5);
		TransactionDto dto = transaction.toDto();
		dto.amount = 11;
		//The transaction has been modified so it is not returned.
		BlockChain.getInstance().addPendingTransaction(new Transaction(dto));
		
		assertEquals(0, executor.execute(new GetValidTransactions()).size());
	}

}
