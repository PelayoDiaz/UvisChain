package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.communication.handlers.BlockChainSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.abst.AbstractSender;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * It creates a new wallet, stores it into the blockchain and send it to other nodes.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class CreateWallet extends AbstractSender<Wallet, WalletDto> implements Command<Wallet> {
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/addWallet";
	
	private WalletDto dto;

	/**
	 * Constructor.
	 * 
	 * @param dto 
	 * 			The wallet to be created
	 */
	public CreateWallet(WalletDto dto) {
		this.dto = dto;
	}

	@Override
	public Wallet execute() {
		Wallet wallet = new Wallet(this.dto);
		BlockChain.getInstance().putWallet(wallet.getAddress(), wallet.toDto());
		this.send(wallet, new BlockChainSessionHandler(), LISTENER);
		return wallet;
	}

}
