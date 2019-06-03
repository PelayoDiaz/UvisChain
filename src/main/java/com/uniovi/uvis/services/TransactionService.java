package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;

public interface TransactionService {
	
	public Transaction sendFunds();
	
	/**
	 * Returns the total balance of the wallet and stores the UTXOs in it.
	 * 
	 * @param wallet
	 * 			The wallet to check the balance for.
	 * @return double
	 * 			The total balance of the utxos in the wallet
	 */
	public double getBalance(Wallet wallet);

}
