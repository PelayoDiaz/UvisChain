package com.uniovi.uvis.services;

import java.util.List;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;

public interface BlockChainService {
	
	/**
	 * Recollects all the chains of the connected nodes.
	 * 
	 * @return List<BlockChain>
	 * 			All the chains of the connected nodes.
	 */
	public List<BlockChain> getAllChains();
	
	/**
	 * Registers a new node into the chain for communication.
	 * 
	 * @param node
	 * 			The node to be registered.
	 * @return BlockChainDto
	 * 			The updated blockchain with the new node.
	 */
	public BlockChainDto registerNode(Node node);
	
	/**
	 * Adds a new transaction to the chain and stores it until the transaction
	 * is included into a block.
	 * 
	 * @param dto
	 * 			The TransactionDto which contains the information of the transaction
	 * @return BlockChainDto
	 * 			A copy of the actual's node chain.
	 */
	public BlockChainDto addTransaction(TransactionDto dto);
	
	/**
	 * Adds a new wallet to the chain and stores it for the users to use it.
	 * 
	 * @param dto
	 * 			The WalletDto which contains the information of the Wallet
	 * @return BlockChainDto
	 * 			A copy of the actual's node chain.
	 */
	public BlockChainDto addWallet(WalletDto dto);

}
