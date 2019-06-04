package com.uniovi.uvis.services.impl.wallet;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.services.impl.Command;

public class AddWallet implements Command<BlockChainDto>{

	private WalletDto walletDto;
	
	public AddWallet(WalletDto dto) {
		this.walletDto = dto;
	}

	@Override
	public BlockChainDto execute() {
		BlockChain.getInstance().putWallet(walletDto.address, walletDto);
		return BlockChain.getInstance().toDto();
	}

}
