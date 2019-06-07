package com.uniovi.uvis.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.BlockChainServiceImpl;
import com.uniovi.uvis.services.impl.BlockServiceImpl;

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
	
	@Autowired
	private BlockServiceImpl blockService;

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
	public BlockChainDto updateChain(BlockChainDto dto) {
		return this.blockChainService.updateChain(dto);
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
	
	@RequestMapping("/mine") //No se muestra en pantalla porque es un controller no un restcontroller, pero funciona.
	public String mine() {
		List<Transaction> originalTransactions = new ArrayList<Transaction>(BlockChain.getInstance().getTransactions());
		Block newBlock = this.blockService.createBlock();
		BlockDto dto = this.blockChainService.mine(newBlock, originalTransactions);
		this.blockChainService.send();
		return "Block id: "+ dto.id+ " - PreviousHash: "+ dto.previousHash;
	}
	
	

}
