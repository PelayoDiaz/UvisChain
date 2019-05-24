package com.uniovi.uvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.transactions.UTXOs;
import com.uniovi.uvis.entities.wallet.Wallet;

public class TransactionTest {

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
	
	/**
	 * Checks if the transaction is processed in a properly way.
	 */
	public void processTransactionValidTest() {
		
	}
	

}
