package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.Command;

public class CreateWallet implements Command<Wallet> {
	
	private String address;
	
	public CreateWallet(String address) {
		this.address = address;
	}

	@Override
	public Wallet execute() {
		Wallet wallet = new Wallet(address);
		BlockChain.getInstance().putWallet(wallet.getAddress(), wallet);
		
		return wallet;
	}

}
