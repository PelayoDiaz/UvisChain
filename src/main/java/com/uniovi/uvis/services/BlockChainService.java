package com.uniovi.uvis.services;

import java.util.List;

import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;

public interface BlockChainService {
	
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
	
	/**
	 * Adds a new node to the chain and stores it to be used in communications.
	 * 
	 * @param node
	 * 			The Node to be added to the net
	 * @return BlockChainDto
	 * 			A copy of the actual's node chain.
	 */
	public BlockChainDto addNode(Node node);
	
	/**
	 * Updates the chain from the node for another send by other node.
	 * 
	 * @param dto
	 * 			The new chain.
	 * @return BlockChainDto
	 * 			The updated chain.
	 * 		
	 */
	public BlockChainDto updateChain(BlockChainDto dto);
	
	/**
	 * It sends funds to a wallet given by address from the coinbase.
	 * 
	 * @param receiverAddress
	 * 			The wallet's address which receives the funds.
	 * @return List<Transaction>
	 * 			The list of pending transactions contained in the chain.
	 */
	public List<Transaction> sendPrizeTo(String receiverAddress);

}
