package com.uniovi.uvis.services;

import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.WalletDto;
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
	 * Adds a wallet to the blockchain.
	 * 
	 * @param dto
	 * 			The walletDto with the information to be stored.
	 * 
	 * return BlockChainDto
	 * 			The information of the chain.
	 */
	public BlockChainDto addWallet(WalletDto dto);

}
