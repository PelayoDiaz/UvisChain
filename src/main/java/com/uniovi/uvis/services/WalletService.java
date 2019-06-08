package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.transactions.Transaction;
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
	
	/**
	 * It creates a transaction to send funds to receiver.
	 * 
	 * @param sender
	 * 			The address of the transaction's creator
	 * @param receiver
	 * 			The receiver's address of the transaction
	 * @param amount
	 * 			The amount of funds to be sent
	 * @return 	Transaction
	 * 			The created transaction.
	 */
	public Transaction sendFunds(String sender, String receiver, double amount);

}
