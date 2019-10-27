package com.uniovi.uvis.commandTests.blockchain;

import static org.junit.Assert.assertEquals;

import java.security.PublicKey;
import java.util.ArrayList;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.TransactionInputDto;
import com.uniovi.uvis.entities.dto.TransactionOutputDto;
import com.uniovi.uvis.services.impl.blockchain.AddTransaction;
import com.uniovi.uvis.util.CryptoUtil;

public class AddTransactionTest extends AbstractTest {

	@Test
	/**
	 * Checks that a transaction can be added to the blockchain.
	 */
	public void addTransactionCorrectTest() {
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
		executor.execute(new AddTransaction(this.createTransactionDto()));
		assertEquals(1, BlockChain.getInstance().getPendingTransactions().size());
	}
	
	@Test
	/**
	 * Checks that a transaction is not added if the method receives a null value
	 */
	public void addNullTransactionTest() {
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
		executor.execute(new AddTransaction(null));
		assertEquals(0, BlockChain.getInstance().getPendingTransactions().size());
	}
	
	/**
	 * Creates a TransactionDto to be added to the chain.
	 * 
	 * @return TransactionDto
	 */
	private TransactionDto createTransactionDto() {
		PublicKey pk = CryptoUtil.generateKeyPair().getPublic();
		TransactionDto dto = new TransactionDto();
		dto.id = "prueba";
		dto.sender = pk.getEncoded();
		dto.inputs = new ArrayList<TransactionInputDto>();
		dto.outputs = new ArrayList<TransactionOutputDto>();
		return dto;
	}

}
