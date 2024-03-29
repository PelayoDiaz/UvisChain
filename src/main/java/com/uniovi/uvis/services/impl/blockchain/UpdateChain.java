package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * It updates the content of the chain and returns the previous information
 * stored in it.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class UpdateChain implements Command<BlockChainDto>{

	private BlockChainDto dto;

	/**
	 * Constructor.
	 * 
	 * @param dto
	 * 			The dto of the chain with the changes to be included.
	 */
	public UpdateChain(BlockChainDto dto) {
		this.dto = dto;
	}

	@Override
	public BlockChainDto execute() {
		BlockChainDto previousDto = BlockChain.getInstance().toDto();
		if (this.dto != null) {
			BlockChain.getInstance().update(dto);
		}
		return previousDto;
	}

}
