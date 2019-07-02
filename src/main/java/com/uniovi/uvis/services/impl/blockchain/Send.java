package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.communication.handlers.BlockChainSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.services.impl.abst.AbstractSender;
import com.uniovi.uvis.services.impl.command.Command;

public class Send extends AbstractSender<BlockChain, BlockChainDto> implements Command<Void>{
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/updateChain";
		
	private BlockChain chain;

	public Send(BlockChain chain) {
		this.chain = chain;
	}

	@Override
	public Void execute() {
		this.send(chain, new BlockChainSessionHandler(), LISTENER);
		return null;
	}

}