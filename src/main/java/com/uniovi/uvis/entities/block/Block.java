package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.block.Miner;
import com.uniovi.uvis.util.CryptoUtil;

/**
 * The blocks into the chain.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class Block extends AbstractHasheable implements Serializable, Sendable<BlockDto> {
	
	private Logger logger = LogManager.getLogger(Block.class);

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
	
	/**
	 * Default constructor.
	 * 
	 * @param previousHash
	 * 			The hash of the previous block in the chain.
	 */
	public Block(String previousHash) {
		super();
		this.previousHash = previousHash;
		this.mined = false;
		this.transactions = new ArrayList<Transaction>();
	}
	
	/**
	 * Constructor to convert a DTO into its equivalent entity.
	 * 
	 * @param dto
	 * 			The BlockDto to convert.
	 */
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
		logger.info("Process of Mining started. Go for something to do. This may take a while");
		
		do {
			nonce ++;
			this.id = this.calculateHash();
		} while (!id.substring(0, difficulty).equals(target) && Miner.keepMining());
		
		if (id.substring(0, difficulty).equals(target)) {
			this.mined = true;
			logger.info("Congrats! You found a solution!");
		} else {
			logger.info("Canceling... You may want to try it later?");
		}
		Miner.keepMining(true);
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
	
	/**
	 * Removes a transaction from the block
	 * @param transaction
	 * 			The transaction to delete.
	 */
	public void removeTransaction(Transaction transaction) {
		if (transaction!=null) {
			this.transactions.remove(transaction);
		}
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
