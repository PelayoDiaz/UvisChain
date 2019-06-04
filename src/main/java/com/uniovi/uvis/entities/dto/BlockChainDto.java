package com.uniovi.uvis.entities.dto;

import java.io.Serializable;
import java.util.List;

public class BlockChainDto implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -1995465231416720510L;
	
	public List<BlockDto> chain;
	public List<TransactionDto> transactions;
	public List<Node> nodes;
	public List<TransactionOutputDto> utxos;
	public List<WalletDto> wallets;
	

}
