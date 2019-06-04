package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

public class TransactionInputDto implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 253296259224901033L;
	
	public String outputId;
	public TransactionOutputDto utxo;

}
