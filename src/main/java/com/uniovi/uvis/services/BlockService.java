package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.block.Block;

/**
 * Interface for all the operations related to manipulate a block
 * 
 * @author pdiaz
 *
 */
public interface BlockService {

	/**
	 * It creates a block to be mined when stored into the blockchain
	 * 
	 * @return Block
	 * 			The new created block
	 */
	public Block createBlock();
}
