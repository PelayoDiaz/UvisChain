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
	
	/** Counter to increment when mining the block */
	private int nonce;
	
	/** Tells if a block is mined or not */
	private boolean mined;
	
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = this.calculateHash();
		this.mined = false;
	}
	
	/**
	 * Returns a hash representation of the actual information contained
	 * into the object. This hash will stay inmutable through the time that
	 * the block exists.
	 * 
	 * @return String
	 * 			the hash representation of the object.
	 */
	public String calculateHash() {
		String calculatedHash = CryptoUtil.getSha256Hash(
				this.previousHash + 
				String.valueOf(this.timeStamp) + 
				this.merkleRoot +
				String.valueOf(this.nonce));
		return calculatedHash;
	}
	
	/**
	 * Mines the block. For a block to be mined, it is necessary to achieve a number
	 * of 0's in the hash.
	 * 
	 * @param difficulty
	 * 		the number of 0's to achieve.
	 */
	public void mine(int difficulty) {
		if (mined) return;
		
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce ++;
			this.hash = this.calculateHash();
		}
		this.mined = true;
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

	/**
	 * Return the hash of the previous Block in the chain.
	 * 
	 * @return String
	 * 			the previousHash.
	 */
	public String getPreviousHash() {
		return previousHash;
	}

	/**
	 * Says if a block is mined or not.
	 * 
	 * @return the mined
	 */
	public boolean isMined() {
		return mined;
	}
	
	
	
	
}
