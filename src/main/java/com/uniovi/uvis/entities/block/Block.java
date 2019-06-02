package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.util.CryptoUtil;

public class Block extends AbstractHasheable implements Serializable{

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 8295781920515003422L;
	
	/** The hash of the previous Block in the chain */
	private String previousHash;
	
	/** The merkle root of all the transactions to be included in the hash */
	private String merkleRoot;
	
	/** The list of transactions contained in the block */
	private List<Transaction> transactions;
	
	/** Counter to increment when mining the block. It's the proof of work of the block */
	private int nonce;
	
	/** Tells if a block is mined or not */
	private boolean mined;
	
	public Block(String previousHash) {
		super();
		this.previousHash = previousHash;
		this.mined = false;
		this.transactions = new ArrayList<Transaction>();
	}

	@Override
	public String calculateHash() {
		String calculatedHash = CryptoUtil.getSha256Hash(
				this.previousHash + 
				String.valueOf(this.getTimeStamp()) + 
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
		if (mined || this.transactions.size()<=0) return;
		
		this.merkleRoot = CryptoUtil.getMerkleRoot(this.transactions);
		String target = new String(new char[difficulty]).replace('\0', '0');
		do {
			nonce ++;
			this.id = this.calculateHash();
		} while (!id.substring(0, difficulty).equals(target));
		this.mined = true;
	}
	
	/**
	 * Adds a list of transactions to the block. It will check every transaction and
	 * add to the block only the valid ones.
	 * 
	 * @param transactions
	 * 			The transactions to be added.
	 */
	public void addTransactions(List<Transaction> transactions) {
		transactions.forEach(x -> addTransaction(x));
	}
	
	/**
	 * Add a transaction to the block. A transaction can only be added if:
	 *  - The Block is not the genesis block.
	 *  - The Transaction is processed correctly.
	 *  
	 * @param transaction
	 * 			The transaction to be added.
	 * 
	 * @return Boolean
	 * 			True if it is added, false if not
	 */
	public boolean addTransaction(Transaction transaction) {
		//If it is the genesis block, add it without process.
		if (!this.previousHash.equals("0") && !canBeAdded(transaction)) {
			return false;
		}
		this.transactions.add(transaction);
		return true;
	}
	
	/**
	 * Checks if a transaction can be added to the block.
	 * It checks if the transaction is null and if the block is not the genesis block.
	 * Finally, it proccess the transaction and allow to add the transaction if everything is correct.
	 * 
	 * @param transaction
	 * 			The transaction to be checked.
	 * 
	 * @return Boolean
	 * 			True, if the transaction can be added. False if not.
	 */
	private boolean canBeAdded(Transaction transaction) {
		return transaction != null && transaction.processTransaction();
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
	
	public BlockDto toDto() {
		BlockDto dto = new BlockDto();
		dto.hash = this.id;
		dto.previousHash = this.previousHash;
		dto.merkleRoot = this.merkleRoot;
		dto.transactions = this.transactions.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.nonce = this.nonce;
		dto.timeStamp = this.getTimeStamp();
		dto.mined = this.mined;
		
		return dto;
	}

	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return new ArrayList<Transaction>(this.transactions);
	}
}
