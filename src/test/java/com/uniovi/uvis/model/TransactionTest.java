package com.uniovi.uvis.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;

public class TransactionTest extends AbstractTest {
	
	private Wallet walletA;
	private Wallet walletB;
	
	//Antes de cada prueba
	@Before
	public void setUp() throws Exception {
		walletA = new Wallet("walletA", "direccion1", "userA");
		walletB = new Wallet("walletB", "direccion2", "userB");
	}

	@Test
	/**
	 * Checks if a transaction is correctly signed and if it is verified in a correct way.
	 */
	public void creatingATransactionTest() {		
		Transaction transaction = new Transaction(walletA, walletB.getAddress(), 5, null);
		assertFalse(transaction.verifySignature());
		walletA.signTransaction(transaction);
		assertTrue(transaction.verifySignature());
	}
	
	@Test
	/**
	 * Tests if the method processTransaction returns false because the transaction isn't signed.
	 */
	public void processTransactionNotVerifiedTest() {		
		Transaction transaction = new Transaction(walletA, walletB.getAddress(), 5, null);
		assertFalse(transaction.processTransaction());
	}
	
	@Test
	/**
	 * Checks if the conditions to make a transaction not valid are correct.
	 */
	public void processTransactionNotValidTest() {
		
		//Creatin outputs
		TransactionOutput output1 = new TransactionOutput(walletA.getAddress(), 3, null);
		TransactionOutput output2 = new TransactionOutput(walletA.getAddress(), 0.009, null);
		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		BlockChain.getInstance().putUTXO(output2.getId(), output2);
		
		//Creating inputs
		TransactionInput input1 = new TransactionInput(output1.getId());
		TransactionInput input2 = new TransactionInput(output2.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		
		//False because the amount is less than the minimum allowed
		Transaction transaction = new Transaction(walletA, walletB.getAddress(), 0.009, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
		
		//False because the total of the inputs is less than the minimum allowed
		inputs.remove(input1);
		inputs.add(input2);
		transaction = new Transaction(walletA, walletB.getAddress(), 1, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
		
		//False because the total of the inputs is less than amount to be sent.
		inputs.add(input1);
		transaction = new Transaction(walletA, walletB.getAddress(), 3.1, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
	}
	
	@Test
	/**
	 * Checks if the transaction is processed in a properly way with an exact amount of money.
	 */
	public void processTransactionValidExactAmountTest() {
		// Checks that there is two utxo available, one from the coinbase and the other which belongs to walletA
		TransactionOutput output1 = new TransactionOutput(walletA.getAddress(), 3, null);
		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		assertEquals(2, BlockChain.getInstance().getUTXOMap().size());
		assertTrue(BlockChain.getInstance().getUTXO(output1.getId()).belongsTo(walletA.getAddress()));
		
		// Crear inputs para la transaccion
		TransactionInput input1 = new TransactionInput(output1.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		
		//True with the exact amount of funds to send
		Transaction transaction = new Transaction(walletA, walletB.getAddress(), 3, inputs);
		walletA.signTransaction(transaction);	
		assertTrue(transaction.processTransaction());
		
		//Checks that there is still two utxo because the previous one has been deleted and that it belongs to walletb.
		assertEquals(2, BlockChain.getInstance().getUTXOMap().size());
		assertEquals(3, BlockChain.getInstance().getUTXO(transaction.getOutputs().get(0).getId()).getValue(), 0.01);
		assertTrue(BlockChain.getInstance().getUTXO(transaction.getOutputs().get(0).getId()).belongsTo(walletB.getAddress()));
	}
	
	@Test
	/**
	 * Checks if the transaction is processed in a properly way with a left over.
	 */
	public void processTransactionWithLeftOverTest() {
		// Checks that there is three utxos available, the first one from coinbase, and the other two belong to walletA
		TransactionOutput output1 = new TransactionOutput(walletA.getAddress(), 3, null);
		TransactionOutput output2 = new TransactionOutput(walletA.getAddress(), 0.5, null);
		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		BlockChain.getInstance().putUTXO(output2.getId(), output2);
		assertEquals(3, BlockChain.getInstance().getUTXOMap().size());
		assertTrue(BlockChain.getInstance().getUTXO(output1.getId()).belongsTo(walletA.getAddress()));
		assertTrue(BlockChain.getInstance().getUTXO(output2.getId()).belongsTo(walletA.getAddress()));
		
		// Crear inputs para la transaccion
		TransactionInput input1 = new TransactionInput(output1.getId());
		TransactionInput input2 = new TransactionInput(output2.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		inputs.add(input2);
		
		//Transaction from A to B. True with a left over
		Transaction transaction = new Transaction(walletA, walletB.getAddress(), 3, inputs);
		walletA.signTransaction(transaction);	
		assertTrue(transaction.processTransaction());
		
		assertEquals(3, BlockChain.getInstance().getUTXO(transaction.getOutputs().get(0).getId()).getValue(), 0.01);
		assertTrue(BlockChain.getInstance().getUTXO(transaction.getOutputs().get(0).getId()).belongsTo(walletB.getAddress()));
		
		assertEquals(0.5, BlockChain.getInstance().getUTXO(transaction.getOutputs().get(1).getId()).getValue(), 0.01);
		assertTrue(BlockChain.getInstance().getUTXO(transaction.getOutputs().get(1).getId()).belongsTo(walletA.getAddress()));
	}

}
