package com.uniovi.uvis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.services.BlockChainService;
import com.uniovi.uvis.services.impl.blockchain.GetAllChains;
import com.uniovi.uvis.services.impl.blockchain.RegisterNode;

@Service
public class BlockChainServiceImpl implements BlockChainService{

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public BlockChainServiceImpl() {
		this.executor = new CommandExecutor();
	}
	
	@Override
	public List<BlockChain> getAllChains() {
		return executor.execute(new GetAllChains());
	}

	@Override
	public BlockChainDto registerNode(Node node) {
		return executor.execute(new RegisterNode(node));
	}

}
