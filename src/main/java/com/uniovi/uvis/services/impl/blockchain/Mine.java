package com.uniovi.uvis.services.impl.blockchain;

import java.util.ArrayList;
import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

public class Mine implements Command<BlockDto> {
	
	

	public Mine() {
	}

	@Override
	public BlockDto execute() {
		List<Transaction> transactions = new ArrayList<Transaction>(BlockChain.getInstance().getTransactions());
		Block block = new Block(BlockChain.getInstance().getLastBlock().getId());
		block.ad
		return null;
	}

}
