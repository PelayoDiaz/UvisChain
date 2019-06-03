package com.uniovi.uvis.entities.dto;

import java.io.Serializable;
import java.util.List;


public class BlockDto implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -5523304275335621145L;
	
	public String id;
	public String previousHash;
	public String merkleRoot;
	public List<TransactionDto> transactions;
	public int nonce;
	public long timeStamp;
	public boolean mined;
}
