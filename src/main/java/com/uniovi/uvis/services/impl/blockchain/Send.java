package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.entities.abst.AbstractSender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.services.impl.command.Command;

public class Send extends AbstractSender<BlockChain, BlockChainDto> implements Command<Void>{
		
	private BlockChain chain;

	public Send(BlockChain chain) {
		this.chain = chain;
	}

	@Override
	public Void execute() {//TODO: indicar handler y listener
		this.send(chain, handler, listener);
	}

}
