package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;

import com.uniovi.uvis.entities.dto.TransactionInputDto;

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
	 * Reference to the unspent transactionOutput id.
	 */
	private String outputId;
	
	/**
	 * Reference to the unspent transactionOutput.
	 */
	private TransactionOutput utxo;
	
	public TransactionInput(String outputId) {
		this.outputId = outputId;
	}
	
	public TransactionInput(TransactionInputDto dto) {
		this.outputId = dto.outputId;
		this.utxo = new TransactionOutput(dto.utxo);
	}

	/**
	 * @return the outputId
	 */
	public String getOutputId() {
		return outputId;
	}

	/**
	 * @param utxo the utxo to set
	 */
	public void setUtxo(TransactionOutput utxo) {
		this.utxo = utxo;
	}

	/**
	 * @return the utxo
	 */
	public TransactionOutput getUtxo() {
		return utxo;
	}
	
	public TransactionInputDto toDto() {
		TransactionInputDto dto = new TransactionInputDto();
		dto.outputId = this.outputId;
		dto.utxo = this.utxo.toDto();
		return dto;
	}
		
}
