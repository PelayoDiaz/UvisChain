package com.uniovi.uvis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
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
	public String createWallet(@RequestParam String username, String password, String name, String surname1, String surname2) {
		Wallet wallet = walletService.createWallet(username, password, name, surname1, surname2);
		return wallet.getId() + " - Blockchain: " + BlockChain.getInstance().getWallets();
	}
	
	@RequestMapping("/transaction")
	public String sendFunds(@RequestParam String to, String amount) {
		Transaction transaction = walletService.sendFunds(to, Double.valueOf(amount));
		return "Transaction: " + transaction.getId() + " - To: " + transaction.getReceiver();
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam String username, String password) {
		boolean success = this.walletService.setActiveWallet(username, password);
		return "Correct login?: " + success;
	}

	
}
