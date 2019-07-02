package com.uniovi.uvis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.services.impl.WalletServiceImpl;

/**
 * It controlls all the methods related to create wallets.
 * 
 * @author Pelayo Díaz Soto
 *
 */
@RestController
public class WalletController {
	
	@Autowired
	private WalletServiceImpl walletService;

	@MessageMapping("/chain/createWallet")
	@SendTo("/topic/blockchainClient")
	public BlockChainDto createWallet(WalletDto dto) {
		walletService.createWallet(dto);
		return BlockChain.getInstance().toDto();
	}
	
	@MessageMapping("/chain/createTransaction")
	@SendTo("/topic/blockchainClient")
	public BlockChainDto sendFunds(TransactionDto dto) {
		walletService.sendFunds(dto);
		return BlockChain.getInstance().toDto();
	}
	
}
