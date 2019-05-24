package com.uniovi.uvis.entities.wallet;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.util.concurrent.AtomicDouble;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.transactions.UTXOs;
import com.uniovi.uvis.util.CryptoUtil;

public class Wallet implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1687361974319098902L;
	
	/** The private key to keep in secret to decode the information */
	private PrivateKey privateKey;
	
	/** The public key to share with people which send you information */
	private PublicKey publicKey;
	
	/** A hashMap which contains all the unspent outputs of the wallet that can be used as inputs. */
	private Map<String, TransactionOutput> utxos;
	
	public Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
		this.utxos = new HashMap<String, TransactionOutput>();
	}
	
	/**
	 * Returns the total balance of the wallet and stores the UTXOs in it.
	 * 
	 * @return double
	 * 				the total balance of the utxos in the wallet
	 */
	public double getBalance() {
		AtomicDouble total = new AtomicDouble(0);
		UTXOs.getInstance().getMap().forEach((k, v) -> {
			if (v.belongsTo(publicKey)) {
				utxos.put(v.getId(), v);
				total.addAndGet(v.getValue());
			}
		});
		return total.get();
	}
	
	/**
	 * Creates a Transaction with an amount of funds to be send to a receiver.
	 * 
	 * @param receiver
	 * 				the receiver of the transaction
	 * @param amount
	 * 				the amount of funds to be send
	 * @return Transaction
	 * 				the transaction created
	 */
	public Transaction sendFunds(PublicKey receiver, double amount) {
		if (this.getBalance() < amount) { //Checks if there is money enough. 
			return null;
		}
		ArrayList<TransactionInput> inputs = getTransactionInputs(amount);	
		return createTransaction(receiver, amount, inputs);
	}
	
	/**
	 * It process all the utxos of the wallet and turns them into Transaction inputs until
	 * the value of the utxos is enough to the amount required.
	 * 
	 * @param amount
	 * 				the amount to be send
	 * @return ArrayList<TransactionInput>
	 * 				the inputs for the transaction.
	 */
	private ArrayList<TransactionInput> getTransactionInputs(double amount) {
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		// Calculates the total utxos that can be send with the transaction
		AtomicDouble total = new AtomicDouble(0);
		this.utxos.forEach((k, v) -> {
			if (total.get() < amount) {
				total.addAndGet(v.getValue());
				inputs.add(new TransactionInput(v.getId()));
			}
		});
		
		return inputs;
	}
	
	/**
	 * Creates the transaction to be executed.
	 * 
	 * @param receiver
	 * 				The receiver of the funds
	 * @param amount
	 * 				The total funds to be sent.
	 * @param inputs
	 * 				The outputs of the wallet which contains the funds to be sent.
	 * @return Transaction
	 * 				The new transaction to be processed.
	 */
	private Transaction createTransaction(PublicKey receiver, double amount, ArrayList<TransactionInput> inputs) {
		Transaction transaction = new Transaction(this.publicKey, receiver, amount, inputs);
		this.signTransaction(transaction);
		inputs.forEach(x -> this.utxos.remove(x.getOutputId()));
		
		return transaction;
	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	/**
	 * Signs a transaction with the private key of the wallet.
	 * 
	 * @param transaction
	 * 			The transaction to be signed.
	 */
	public void signTransaction(Transaction transaction) {
		if (transaction.getSender().equals(this.publicKey)) {
			transaction.generateSignature(this.privateKey);
		}
	}

}
