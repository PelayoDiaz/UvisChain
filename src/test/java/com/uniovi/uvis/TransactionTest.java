package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.transactions.UTXOs;
import com.uniovi.uvis.entities.wallet.Wallet;

public class TransactionTest {
	
	//Antes de cada prueba
	@Before
	public void setUp() throws Exception {
	}

	//Despues de cada prueba
	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Checks if a transaction is correctly signed and if it is verified in a correct way.
	 */
	public void creatingATransactionTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
		assertFalse(transaction.verifySignature());
		walletA.signTransaction(transaction);
		assertTrue(transaction.verifySignature());
	}
	
	@Test
	/**
	 * Tests if the method processTransaction returns false because the transaction isn't signed.
	 */
	public void processTransactionNotVerifiedTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
		assertFalse(transaction.processTransaction());
	}
	
	@Test
	/**
	 * Checks if the conditions to make a transaction not valid are correct.
	 */
	public void processTransactionNotValidTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		//Crear outputs en utxo
		TransactionOutput output1 = new TransactionOutput(walletA.getPublicKey(), 3, null);
		TransactionOutput output2 = new TransactionOutput(walletA.getPublicKey(), 0.01, null);
		UTXOs.getInstance().put(output1.getId(), output1);
		UTXOs.getInstance().put(output2.getId(), output2);
		
		//Crear inputs para la transaccion
		TransactionInput input1 = new TransactionInput(output1.getId());
		TransactionInput input2 = new TransactionInput(output2.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		
		//False because the amount is less than the minimum allowed
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 0.09, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
		
		//False because the total of the inputs is less than the minimum allowed
		inputs.remove(input1);
		inputs.add(input2);
		transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 1, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
		
		//False because the total of the inputs is less than amount to be sent.
		inputs.add(input1);
		transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 3.1, inputs);
		walletA.signTransaction(transaction);	
		assertFalse(transaction.processTransaction());
	}
	
	@Test
	/**
	 * Checks if the transaction is processed in a properly way with an exact amount of money.
	 */
	public void processTransactionValidExactAmountTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();

		// Checks that there is one utxo available and it belongs to walletA
		TransactionOutput output1 = new TransactionOutput(walletA.getPublicKey(), 3, null);
		UTXOs.getInstance().put(output1.getId(), output1);
		assertEquals(1, UTXOs.getInstance().getMap().size());
		assertTrue(UTXOs.getInstance().get(output1.getId()).belongsTo(walletA.getPublicKey()));
		
		// Crear inputs para la transaccion
		TransactionInput input1 = new TransactionInput(output1.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		
		//True with the exact amount of funds to send
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 3, inputs);
		walletA.signTransaction(transaction);	
		assertTrue(transaction.processTransaction());
		
		//Checks that there is still one utxo because the previous one has been deleted and that it belongs to walletb.
		assertEquals(1, UTXOs.getInstance().getMap().size());
		assertEquals(3, UTXOs.getInstance().get(transaction.getOutputs().get(0).getId()).getValue(), 0.01);
		assertTrue(UTXOs.getInstance().get(transaction.getOutputs().get(0).getId()).belongsTo(walletB.getPublicKey()));
	
		//Delete all the utxos for the next test
		UTXOs.getInstance().remove(transaction.getOutputs().get(0).getId());
	}
	
	@Test
	/**
	 * Checks if the transaction is processed in a properly way with a left over.
	 */
	public void processTransactionWithLeftOverTest() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();

		// Checks that there is two utxos available and they belong to walletA
		TransactionOutput output1 = new TransactionOutput(walletA.getPublicKey(), 3, null);
		TransactionOutput output2 = new TransactionOutput(walletA.getPublicKey(), 0.5, null);
		UTXOs.getInstance().put(output1.getId(), output1);
		UTXOs.getInstance().put(output2.getId(), output2);
		assertEquals(2, UTXOs.getInstance().getMap().size());
		assertTrue(UTXOs.getInstance().get(output1.getId()).belongsTo(walletA.getPublicKey()));
		assertTrue(UTXOs.getInstance().get(output2.getId()).belongsTo(walletA.getPublicKey()));
		
		// Crear inputs para la transaccion
		TransactionInput input1 = new TransactionInput(output1.getId());
		TransactionInput input2 = new TransactionInput(output2.getId());
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input1);
		inputs.add(input2);
		
		//Transaction from A to B. True with a left over
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 3, inputs);
		walletA.signTransaction(transaction);	
		assertTrue(transaction.processTransaction());
		
		//Checks that there are two utxos. The one with the amount to send and the one with the left over
		assertEquals(2, UTXOs.getInstance().getMap().size());
		
		assertEquals(3, UTXOs.getInstance().get(transaction.getOutputs().get(0).getId()).getValue(), 0.01);
		assertTrue(UTXOs.getInstance().get(transaction.getOutputs().get(0).getId()).belongsTo(walletB.getPublicKey()));
		
		assertEquals(0.5, UTXOs.getInstance().get(transaction.getOutputs().get(1).getId()).getValue(), 0.01);
		assertTrue(UTXOs.getInstance().get(transaction.getOutputs().get(1).getId()).belongsTo(walletA.getPublicKey()));
		
		//Delete all the utxos for the next test
		UTXOs.getInstance().remove(transaction.getOutputs().get(0).getId());
		UTXOs.getInstance().remove(transaction.getOutputs().get(1).getId());
	}
	

}
