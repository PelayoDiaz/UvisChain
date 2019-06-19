package com.uniovi.uvis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.BlockDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.BlockChainService;
import com.uniovi.uvis.services.impl.block.Mine;
import com.uniovi.uvis.services.impl.blockchain.AddTransaction;
import com.uniovi.uvis.services.impl.blockchain.AddWallet;
import com.uniovi.uvis.services.impl.blockchain.IsChainValid;
import com.uniovi.uvis.services.impl.blockchain.RegisterNode;
import com.uniovi.uvis.services.impl.blockchain.Send;
import com.uniovi.uvis.services.impl.blockchain.UpdateChain;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;

@Service
public class BlockChainServiceImpl implements BlockChainService{

	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	public BlockChainServiceImpl() {
		this.executor = new CommandExecutorIf();
	}

	@Override
	public BlockChainDto registerNode(Node node) {
		return executor.execute(new RegisterNode(node));
	}

	@Override
	public BlockChainDto addTransaction(TransactionDto dto) {
		return executor.execute(new AddTransaction(dto));
	}

	@Override
	public BlockChainDto addWallet(WalletDto dto) {
		return executor.execute(new AddWallet(dto));
	}

	@Override
	public void mine(Block block, List<Transaction> originalTransactions) {
		executor.execute(new Mine(block, originalTransactions));
	}

	@Override
	public BlockChainDto updateChain(BlockChainDto dto) {
		//Updates with the new content and stores the previous one.
		BlockChainDto previousDto = executor.execute(new UpdateChain(dto));
		//If the new chain is not valid, restores the previous content
		boolean isValid = executor.execute(new IsChainValid(BlockChain.getInstance()));
		executor.execute(!isValid, new UpdateChain(previousDto));
		//If the chain is valid, returns the new one, if not, returns an empty Dto
		return (isValid) ? BlockChain.getInstance().toDto() : new BlockChainDto();
	}

}
