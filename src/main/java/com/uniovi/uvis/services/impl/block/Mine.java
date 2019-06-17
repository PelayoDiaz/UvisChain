package com.uniovi.uvis.services.impl.block;

import java.util.List;

import com.uniovi.uvis.entities.abst.AbstractSender;
import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

public class Mine extends AbstractSender<BlockChain, BlockChainDto> implements Command<BlockDto> {

	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/updateChain";
	
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
