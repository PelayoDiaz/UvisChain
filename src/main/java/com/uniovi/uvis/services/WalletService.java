package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;

public interface WalletService {
	
	/**
	 * Method to create a new wallet to be stored in the blockchain.
	 * 
	 * @param dto
	 * 			The wallet to be created 
	 */
	public Wallet createWallet(WalletDto dto);
	
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

}
