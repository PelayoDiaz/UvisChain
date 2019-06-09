package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.communication.handlers.SendWalletSessionHandler;
import com.uniovi.uvis.entities.abst.AbstractSender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.Command;
import com.uniovi.uvis.util.CryptoUtil;

public class CreateWallet extends AbstractSender<Wallet, WalletDto> implements Command<Wallet> {
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/addWallet";
	
	private String username;
	private String password;	
	private String name;
	private String surname1;
	private String surname2;

	
	public CreateWallet(String username, String password, String name, String surname1, String surname2) {
		this.username = username;
		this.password = CryptoUtil.getSha256Hash(password);
		this.name = name;
		this.surname1 = surname1;
		this.surname2 = surname2;
	}

	@Override
	public Wallet execute() {
		Wallet wallet = new Wallet(this.username, this.password, this.name, this.surname1, this.surname2);
		BlockChain.getInstance().putWallet(wallet.getUser().username, wallet.toDto());
		this.send(wallet, new SendWalletSessionHandler(), LISTENER);
		return wallet;
	}

}
