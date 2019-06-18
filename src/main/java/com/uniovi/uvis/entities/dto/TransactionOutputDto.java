package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

public class TransactionOutputDto extends AbstractDto implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 3339534868048031036L;
	
	public String id;
	public String receiver;
	public double value;
	public String parentTransactionId;
	public long timeStamp;

}
