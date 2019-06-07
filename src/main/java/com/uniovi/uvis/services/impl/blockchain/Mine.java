package com.uniovi.uvis.services.impl.blockchain;

import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

public class Mine implements Command<BlockDto> {

	private Block block;
	private List<Transaction> originalTransactions;

	public Mine(Block block, List<Transaction> originalTransactions) {
		this.block = block;
		this.originalTransactions = originalTransactions;
	}

	@Override
	public BlockDto execute() {
		this.block.mine(BlockChain.DIFFICULTY);
		BlockChain.getInstance().addBlock(this.block, this.originalTransactions);
		return this.block.toDto();
	}

}
