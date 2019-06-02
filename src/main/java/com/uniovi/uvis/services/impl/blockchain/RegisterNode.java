package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.services.impl.Command;

public class RegisterNode implements Command<BlockChainDto>{
	
	private Node node;

	public RegisterNode(Node node) {
		this.node = node;
	}

	@Override
	public BlockChainDto execute() {
		BlockChain.getInstance().registerNode(node);
		return BlockChain.getInstance().toDto();
	}

}
