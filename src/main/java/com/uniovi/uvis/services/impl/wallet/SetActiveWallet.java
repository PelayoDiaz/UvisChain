package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.Command;
import com.uniovi.uvis.util.CryptoUtil;

public class SetActiveWallet implements Command<Boolean> {

	private String username;
	private String password;

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
	public SetActiveWallet(String username, String password) {
		this.username = username;
		this.password = CryptoUtil.getSha256Hash(password);
	}

	@Override
	public Boolean execute() {
		if (this.username.equals(BlockChain.COIN_BASE)) {
			return false;
		}
		WalletDto dto = BlockChain.getInstance().getWallets().get(this.username);
		if (dto != null && dto.user.password.equals(this.password)) {
			BlockChain.getInstance().setActiveWallet(new Wallet(dto));
			return true;
		}
		return false;
	}

}
