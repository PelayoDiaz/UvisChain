package com.uniovi.uvis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.services.BlockChainService;
import com.uniovi.uvis.services.impl.block.Miner;

/**
 * It contains the methods to control the blockchain object
 * 
 * @author Pelayo Díaz Soto
 *
 */
@Controller
public class BlockChainController {
	
	@Autowired
	private BlockChainService blockChainService;

	@MessageMapping("/chain/registerNode")
	@SendTo("/topic/blockchain")
	public BlockChainDto registerNode(Node node) {
		return this.blockChainService.registerNode(node);
	}
	
	@MessageMapping("/chain/sendChain")
	@SendTo("/topic/blockchainClient")
	public BlockChainDto sendChain(BlockChainDto dto) {
		return BlockChain.getInstance().toDto();
	}
	
	@MessageMapping("/chain/updateChain")
	@SendTo("/topic/blockchain")
	public BlockChainDto updateChain(BlockChainDto dto) {
		Miner.keepMining(false);
		return this.blockChainService.updateChain(dto);
	}
	
	@MessageMapping("/chain/addWallet")
	@SendTo("/topic/blockchain")
	public BlockChainDto addWallet(WalletDto dto) {
		return this.blockChainService.addWallet(dto);
	}
	
	@MessageMapping("/chain/addTransaction")
	@SendTo("/topic/blockchain")
	public BlockChainDto addTransaction(TransactionDto dto) {
		return this.blockChainService.addTransaction(dto);
	}
}
