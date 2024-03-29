package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;
import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.TransactionOutputDto;
import com.uniovi.uvis.util.CryptoUtil;

/**
 * Class to store the amount of funds available to a wallet.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class TransactionOutput extends AbstractHasheable implements Serializable, Sendable<TransactionOutputDto> {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 5665651396410700747L;
	
	/** The address of the wallet's receiver */
	private String receiver;

	/** The amount of coins they own */
	private double value;
	
	/** The id of the transaction where this output was created. */
	private String parentTransactionId;

	/**
	 * Constructor.
	 * @param receiver
	 * 			The address of the wallet's receiver
	 * @param value
	 * 			The amount of coins they own
	 * @param parentTransactionId
	 * 			The id of the transaction where this output was created.
	 */
	public TransactionOutput(String receiver, double value, String parentTransactionId) {
		this.receiver = receiver;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = calculateHash();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param dto
	 * 			The dto to be converted into a transaction.
	 */
	public TransactionOutput(TransactionOutputDto dto) {
		this.id = dto.id;
		this.receiver = dto.receiver;
		this.value = dto.value;
		this.parentTransactionId = dto.parentTransactionId;
		this.timeStamp = dto.timeStamp;
	}
	
	/**
	 * Says if the value belongs to a publicKey or not.
	 * 
	 * @param receiver
	 * 			The address of the transaction's receiver to check.
	 * @return
	 * 			True if it belongs to. False if not.
	 */
	public boolean belongsTo(String receiver) {
		return this.receiver.equals(receiver);
	}
	
	/**
	 * Gets the hash of the output. It is used as its id.
	 * 
	 * @return String
	 * 			the hash of the transaction
	 */
	@Override
	public String calculateHash() {
		return CryptoUtil.getSha256Hash(
				this.receiver +
				this.value +
				this.timeStamp);
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public TransactionOutputDto toDto() {
		TransactionOutputDto dto = new TransactionOutputDto();
		dto.id = this.id;
		dto.receiver = this.receiver;
		dto.value = this.value;
		dto.parentTransactionId = this.parentTransactionId;
		dto.timeStamp = this.timeStamp;
		return dto;
	}
	
}
