package com.uniovi.uvis.services.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.BlockChainService;
import com.uniovi.uvis.services.impl.block.Mine;
import com.uniovi.uvis.services.impl.blockchain.AddTransaction;
import com.uniovi.uvis.services.impl.blockchain.AddWallet;
import com.uniovi.uvis.services.impl.blockchain.IsChainValid;
import com.uniovi.uvis.services.impl.blockchain.RegisterNode;
import com.uniovi.uvis.services.impl.blockchain.UpdateChain;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;
import com.uniovi.uvis.services.impl.wallet.GetBalance;
import com.uniovi.uvis.services.impl.wallet.SendPrize;

@Service
public class BlockChainServiceImpl implements BlockChainService{
	
	private Logger logger = LogManager.getLogger(BlockChainServiceImpl.class);

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
	
	@Override
	public List<Transaction> sendPrizeTo(String receiverAddress) {
		if (BlockChain.getInstance().getWallets().get(receiverAddress)==null) {
			logger.error("There is no receiver address like the given contained in the chain.");
			return null;
		}
		Wallet sender = BlockChain.getInstance().getCoinBase();
		executor.execute(executor.execute(new GetBalance(sender))>=BlockChain.PRIZE, new SendPrize(sender, receiverAddress, BlockChain.PRIZE));
		return BlockChain.getInstance().getTransactions();
	}

}
