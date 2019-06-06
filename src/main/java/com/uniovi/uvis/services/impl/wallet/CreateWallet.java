package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.communication.handlers.SendWalletSessionHandler;
import com.uniovi.uvis.entities.abst.AbstractSender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.Command;

public class CreateWallet extends AbstractSender<Wallet, WalletDto> implements Command<Wallet> {
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/addWallet";
	
	private String address;
	
	public CreateWallet(String address) {
		this.address = address;
	}

	@Override
	public Wallet execute() {
		Wallet wallet = new Wallet(address);
		BlockChain.getInstance().putWallet(wallet.getAddress(), wallet.toDto());
		this.send(wallet, new SendWalletSessionHandler(), LISTENER);
		return wallet;
	}

}
