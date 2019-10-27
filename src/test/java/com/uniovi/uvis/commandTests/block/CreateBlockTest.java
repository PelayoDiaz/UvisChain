package com.uniovi.uvis.commandTests.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.block.CreateBlock;

public class CreateBlockTest extends AbstractTest {
	
	@Test
	/**
	 * Checks that a block can not be created with a null list of transactions
	 */
	public void createBlockNullTransactionsTest() {
		Block block = executor.execute(new CreateBlock(null));
		assertEquals(null, block);
	}
	
	@Test
	/**
	 * Checks that a block can not be created with an empty list of transactions
	 */
	public void createBlockEmptyTransactionsListTest() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Block block = executor.execute(new CreateBlock(transactions));
		assertEquals(null, block);
	}

	@Test
	/**
	 * Checks that a block is created in a correct way..
	 */
	public void createBlockCorrectTest() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Transaction transaction = this.createTransaction();
		transactions.add(transaction);		
		Block block = executor.execute(new CreateBlock(transactions));
		
		//Checks that there's only one transaction in the block
		assertEquals(1, block.getTransactions().size());
		//Checks that the transaction in the block is the same that was previously created.
		assertEquals(transaction, block.getTransactions().get(0));
		//Checks that the previousHash of the block, is the one of the last in the chain.
		assertEquals(BlockChain.getInstance().getLastBlock().getId(), block.getPreviousHash());
		//Checks that the block is not mined.
		assertFalse(block.isMined());
	}
	
	/**
	 * Creates a transaction.
	 * 
	 * @return Transaction
	 * 			The created Transaction.
	 */
	private Transaction createTransaction() {
		Wallet coinbase = new Wallet("coinBase", "coinBase", "coinBase");
		TransactionOutput output = new TransactionOutput(coinbase.getAddress(), 1, null);
		TransactionInput input = new TransactionInput(output.getId());
		input.setUtxo(output);
		Transaction genesisTransaction = new Transaction(coinbase, coinbase.getAddress(), 1, new ArrayList<TransactionInput>(Arrays.asList(input)));
		return genesisTransaction;
	}
	
}
