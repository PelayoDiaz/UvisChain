package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import com.uniovi.uvis.util.CryptoUtil;

public class Transaction implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -4482748116683610567L;

	/** The id of the transaction */
	private String id;
	
	/** The public key of the transaction's sender */
	private PublicKey sender;
	
	/** The public key of the transaction's receiver */
	private PublicKey receiver;
	
	/** The amount of money to send */
	private double amount;
	
	/** To prevent anybody else from spending funds in our wallet */
	private byte[] signature;
	
	/** The actual time at the moment of the creation of the Transaction */
	private long timeStamp;
	
	private ArrayList<TransactionInput> inputs;
	private ArrayList<TransactionOutput> outputs;
	
	public Transaction(PublicKey sender, PublicKey receiver, double amount, ArrayList<TransactionInput> inputs) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		this.outputs = new ArrayList<TransactionOutput>();
		this.timeStamp = new Date().getTime();
		this.id = calculateHash();
	}
	
	/**
	 * Sign all the data that can not be modified.
	 * 
	 * @param privateKey
	 * 			The private key to sign with.
	 */
	public void generateSignature(PrivateKey privateKey) {
		this.signature = CryptoUtil.getECDSASignature(privateKey, getData());
	}
	
	/**
	 * Verifies if the transaction has been modified.
	 * A transaction is valid if the data to be signed has not changed.
	 * 
	 * @return Boolean
	 * 			True if the signature is valid. False if not
	 */
	public boolean verifySignature() {
		return (signature == null) ? false : 
			CryptoUtil.verifyECDSASignature(this.sender, getData(), this.signature);
	}
	
	/**
	 * Get the data that can not be modified.
	 * 
	 * @return String
	 * 			The String of the data
	 */
	private String getData() {
		return CryptoUtil.getStringFromKey(this.sender) +
				CryptoUtil.getStringFromKey(this.receiver) +
				String.valueOf(this.amount);
	}

	/**
	 * @return the sender
	 */
	public PublicKey getSender() {
		return sender;
	}

	/**
	 * @return the receiver
	 */
	public PublicKey getReceiver() {
		return receiver;
	}
	
	
	/**
	 * Gets the hash of the transaction. It is used as its id.
	 * 
	 * @return String
	 * 			the hash of the transaction
	 */
	private String calculateHash() {
		return CryptoUtil.getSha256Hash(
				CryptoUtil.getStringFromKey(this.sender) +
				CryptoUtil.getStringFromKey(this.receiver) +
				this.amount +
				this.timeStamp);
	}	
}
