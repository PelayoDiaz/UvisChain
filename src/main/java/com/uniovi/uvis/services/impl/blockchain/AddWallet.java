package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.services.impl.command.Command;

public class AddWallet implements Command<BlockChainDto>{

	private WalletDto walletDto;
	
	public AddWallet(WalletDto dto) {
		this.walletDto = dto;
	}

	@Override
	public BlockChainDto execute() {
		if (this.walletDto != null) {
			BlockChain.getInstance().putWallet(walletDto.address, walletDto);
		}
		return BlockChain.getInstance().toDto();
	}

}
