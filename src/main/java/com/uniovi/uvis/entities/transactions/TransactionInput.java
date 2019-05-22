package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;

/**
 * It will be used to reference TransactionOutputs which haven't been spent yet.
 * 
 * @author Pelayo Díaz Soto
 */
public class TransactionInput implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -1567223847398968361L;
	
	/**
	 * Reference to the unspent transactionOutput.
	 */
	private TransactionOutput utxo;
	
	public TransactionInput(TransactionOutput utxo) {
		this.utxo = utxo;
	}
	
}
