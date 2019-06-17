package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;

import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.TransactionInputDto;

/**
 * It will be used to reference TransactionOutputs which haven't been spent yet.
 * 
 * @author Pelayo Díaz Soto
 */
public class TransactionInput implements Serializable, Sendable<TransactionInputDto> {
	
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
		this.utxo = (dto.utxo == null) ? null : new TransactionOutput(dto.utxo);
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
	 * @param outputId the outputId to set
	 */
	public void setOutputId(String outputId) {
		this.outputId = outputId;
	}

	/**
	 * @return the utxo
	 */
	public TransactionOutput getUtxo() {
		return utxo;
	}
	
	@Override
	public TransactionInputDto toDto() {
		TransactionInputDto dto = new TransactionInputDto();
		dto.outputId = this.outputId;
		dto.utxo = (this.utxo == null) ? null : this.utxo.toDto();
		return dto;
	}
		
}
