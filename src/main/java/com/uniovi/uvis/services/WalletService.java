package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.wallet.Wallet;

public interface WalletService {
	
	/**
	 * Method to create a new wallet to be stored in the blockchain
	 * 
	 * @param address
	 * 			The address which will be used for other wallets to send funds to this wallet
	 * @return Wallet
	 * 			The wallet
	 */
	public Wallet createWallet(String address);

}
