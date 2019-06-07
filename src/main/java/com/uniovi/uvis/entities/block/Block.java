package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.util.CryptoUtil;

public class Block extends AbstractHasheable implements Serializable, Sendable<BlockDto> {

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
	
	public Block(BlockDto dto) {
		this.id = dto.id;
		this.previousHash = dto.previousHash;
		this.merkleRoot = dto.merkleRoot;
		this.transactions = dto.transactions.stream().map(x -> new Transaction(x)).collect(Collectors.toList());
		this.nonce = dto.nonce;
		this.timeStamp = dto.timeStamp;
		this.mined = dto.mined;
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
	 * Adds a list of transactions to the block. The transactions must be 
	 * previously validated.
	 * 
	 * @param transactions
	 * 			The transactions to be added.
	 */
	public void addTransactions(List<Transaction> transactions) {
		this.transactions.addAll(transactions);
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
	
	@Override
	public BlockDto toDto() {
		BlockDto dto = new BlockDto();
		dto.id = this.id;
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
