package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.WalletService;
import com.uniovi.uvis.services.impl.wallet.CreateWallet;

@Service
public class WalletServiceImpl implements WalletService {

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public WalletServiceImpl() {
		this.executor = new CommandExecutor();
	}
	
	@Override
	public Wallet createWallet(String address) {
		return executor.execute(new CreateWallet(address));
	}

}
