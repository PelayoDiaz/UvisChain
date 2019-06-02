package com.uniovi.uvis.services;

import java.util.List;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;

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

}
