package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.util.CryptoUtil;

public class Block implements Serializable{

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 8295781920515003422L;
	
	/** The hash of the current Block*/
	private String hash;
	
	/** The hash of the previous Block in the chain*/
	private String previousHash;
	
	/** ¿¿?? */
	private String merkleRoot;
	
	/** The list of transactions contained in the block */
	private List<Transaction> transactions;
	
	/** The actual time at the moment of the creation of the Block */
	private long timeStamp;
	
//	private int nonce;
	
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = this.calculateHash();
	}
	
	/**
	 * Returns a hash representation of the actual information contained
	 * into the object. This hash will stay inmutable through the time that
	 * the block exists.
	 * 
	 * @return String
	 * 			the hash representation of the object.
	 */
	private String calculateHash() {
		String calculatedHash = CryptoUtil.getSha256Hash(
				this.previousHash + String.valueOf(this.timeStamp) + this.merkleRoot);
		return calculatedHash;
	}

	/**
	 * Returns the hash of the Block.
	 * 
	 * @return String
	 * 			the hash of the Block.
	 */
	public String getHash() {
		return hash;
	}
	
	
}
