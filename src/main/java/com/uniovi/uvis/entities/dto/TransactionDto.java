package com.uniovi.uvis.entities.dto;

import java.io.Serializable;
import java.util.List;

public class TransactionDto implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -2161764576648183045L;
	
	public String id;
	public byte[] sender;
	public byte[] receiver;
	public double amount;
	public byte[] signature;
	public long timeStamp;
	public List<TransactionInputDto> inputs;
	public List<TransactionOutputDto> outputs;

}
