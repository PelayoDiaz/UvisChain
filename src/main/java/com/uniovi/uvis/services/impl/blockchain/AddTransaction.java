package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

public class AddTransaction implements Command<BlockChainDto> {
	
	private TransactionDto transactionDto;
	
	public AddTransaction(TransactionDto dto) {
		this.transactionDto = dto;
	}

	@Override
	public BlockChainDto execute() {
		if (this.transactionDto != null) {
			BlockChain.getInstance().addPendingTransaction(new Transaction(transactionDto));
		}
		return BlockChain.getInstance().toDto();
	}

}
