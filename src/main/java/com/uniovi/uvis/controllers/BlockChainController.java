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
import com.uniovi.uvis.services.impl.BlockChainServiceImpl;

/**
 * It contains the methods to control the blockchain object
 * 
 * @author Pelayo Díaz Soto
 *
 */
@Controller
public class BlockChainController {
	
	@Autowired
	private BlockChainServiceImpl blockChainService;

	@MessageMapping("/chain/registerNode")
	@SendTo("/topic/blockchain")
	public BlockChainDto registerNode(Node node) {
		return this.blockChainService.registerNode(node);
	}
	
	@MessageMapping("/chain/sendChain")
	@SendTo("/topic/blockchain")
	public BlockChainDto sendChain() {
		return BlockChain.getInstance().toDto();
	}
	
	@MessageMapping("/chain/updateChain")
	@SendTo("/topic/blockchain")
	public BlockChainDto updateChain() { //hacer el consenso
		blockChainService.getAllChains();
		BlockChainDto chain = new BlockChainDto();
//		chain.setCadena(String.valueOf(new Date().getTime()));
		return chain;
	}
	
	@MessageMapping("/chain/addWallet")
	@SendTo("/topic/wallet")
	public BlockChainDto addWallet(WalletDto dto) {
		return this.blockChainService.addWallet(dto);
	}
	
	@MessageMapping("/chain/addTransaction")
	@SendTo("/topic/transaction")
	public BlockChainDto addTransaction(TransactionDto dto) {
		return this.blockChainService.addTransaction(dto);
	}
	
	

}
