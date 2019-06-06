package com.uniovi.uvis.services.impl;

import java.util.ArrayList;
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
import com.uniovi.uvis.services.impl.blockchain.AddTransaction;
import com.uniovi.uvis.services.impl.blockchain.AddWallet;
import com.uniovi.uvis.services.impl.blockchain.GetAllChains;
import com.uniovi.uvis.services.impl.blockchain.RegisterNode;
import com.uniovi.uvis.services.impl.command.CommandExecutor;

@Service
public class BlockChainServiceImpl implements BlockChainService{

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public BlockChainServiceImpl() {
		this.executor = new CommandExecutor();
	}
	
	@Override
	public List<BlockChain> getAllChains() {
		return executor.execute(new GetAllChains());
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
	public BlockDto mine() {
		List<Transaction> transactions = new ArrayList<Transaction>(BlockChain.getInstance().getTransactions());
		Block block = new Block(BlockChain.getInstance().getLastBlock().getId());
		
		//1- Executor: Minar Bloque
		//2- Executor: Añadir bloque a cadena y limpiar transacciones
		return executor.execute(new Mine());
	}

}
