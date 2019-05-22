package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

import com.uniovi.uvis.util.CryptoUtil;

public class TransactionOutput implements Serializable{

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 5665651396410700747L;

	/** The id of the transactionOutput. */
	private String id;
	
	/** The public key of the transaction's receiver */
	private PublicKey receiver;
	
	/** The amount of coins they own */
	private double value;
	
	/** The id of the transaction where this output was created. */
	private String parentTransactionId;
	
	/** The actual time at the moment of the creation of the Transaction */
	private long timeStamp;

	public TransactionOutput(PublicKey receiver, double value, String parentTransactionId) {
		this.receiver = receiver;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.timeStamp = new Date().getTime();
		this.id = calculateHash();
	}
	
	/**
	 * Says if the value belongs to a publicKey or not.
	 * 
	 * @param publicKey
	 * 			The public key to check.
	 * @return
	 * 			True if it belongs to. False if not.
	 */
	public boolean belongsTo(PublicKey publicKey) {
		return this.receiver.equals(publicKey);
	}
	
	/**
	 * Gets the hash of the output. It is used as its id.
	 * 
	 * @return String
	 * 			the hash of the transaction
	 */
	private String calculateHash() {
		return CryptoUtil.getSha256Hash(
				CryptoUtil.getStringFromKey(this.receiver) +
				this.value +
				this.timeStamp);
	}
	
	
}
