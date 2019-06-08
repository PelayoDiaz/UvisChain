package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.Command;

public class GetWallet implements Command<Wallet>{

	private String address;

	/**
	 * It searches a wallet into the blockchain and returns and instance to make
	 * operations with it.
	 * 
	 * @param address
	 * 			The address of the wallet
	 * 
	 * @return Wallet
	 * 			The wallet stored into the chain, null if no exists.
	 */
	public GetWallet(String address) {
		this.address = address;
	}

	@Override
	public Wallet execute() {
		WalletDto dto = BlockChain.getInstance().getWallets().get(address);
		return (dto == null) ? null : new Wallet(dto);
	}

}
