package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;

public interface WalletService {
	
	/**
	 * Method to create a new wallet to be stored in the blockchain.
	 * 
	 * @param username
	 * 			The username of the new wallet.
	 * @param password
	 * 			The password of the new wallet.
	 * @param name
	 * 			The real name of the user.
	 * @param surname1
	 * 			The first surname of the user.
	 * @param surname2
	 * 			The second surname of the user.
	 * @return Wallet
	 * 			The new wallet
	 */
	public Wallet createWallet(String username, String password, String name, String surname1, String surname2);
	
	/**
	 * It creates a transaction to send funds to receiver.
	 * 
	 * @param receiver
	 * 			The receiver's address of the transaction
	 * @param amount
	 * 			The amount of funds to be sent
	 * 
	 * @return 	Transaction
	 * 			The created transaction.
	 */
	public Transaction sendFunds(String receiver, double amount);
	
	/**
	 * Sets a wallet as active to make operations with the chain. 
	 * For a wallet to be activated has to be previously registered.
	 * 
	 * @param username
	 * 			The username of the wallet's owner
	 * @param password
	 * 			The password of the wallet's owner
	 * 
	 * @return Boolean
	 * 			True if the wallet has been activated, false if not.
	 */
	public boolean setActiveWallet(String username, String password);

}
