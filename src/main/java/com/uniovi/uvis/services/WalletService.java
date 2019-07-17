package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.dto.TransactionDto;
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
	 * @param dto
	 * 			The transaction dto. It contains the sender and receiver address,
	 * 			and the amount of funds to be sent.
	 * 
	 * @return 	Transaction
	 * 			The created transaction.
	 */
	public Transaction sendFunds(TransactionDto dto);
	
	/**
	 * It returns a wallet by address if it is contained in the chain.
	 * 
	 * @param address
	 * 			The address of the wallet to search.
	 * 
	 * @return Wallet
	 * 			The wallet searched or null if it doesn't exist
	 */
	public Wallet findByAddress(String address);

}
