package com.uniovi.uvis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.wallet.Wallet;
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

	@RequestMapping("/wallet")
	public String createWallet(@RequestParam String address) {
		Wallet wallet = walletService.createWallet(address);
		return wallet.getAddress() + " - Blockchain: " + BlockChain.getInstance().getWallets();
	}
	
	@RequestMapping("/transaction")
	public String sendFunds(@RequestParam String sender, String receiver, String amount) {
		Transaction transaction = walletService.createWallet(address);
		return wallet.getAddress() + " - Blockchain: " + BlockChain.getInstance().getWallets();
	}

	
}
