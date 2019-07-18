package com.uniovi.uvis.services.impl.block;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.blockchain.IsChainValid;
import com.uniovi.uvis.services.impl.blockchain.Send;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;

public class Miner extends Thread {
	
	private Logger logger = LogManager.getLogger(Miner.class);
	
	private static boolean mining = false;
	private static boolean keepMining = true;
	
	/** Code executor for services. */
	private CommandExecutorIf executor;
	private Block block;
	private List<Transaction> originalTransactions;
	
	public Miner(Block block, List<Transaction> originalTransactions) {
		this.block = block;
		this.originalTransactions = originalTransactions;
		this.executor = new CommandExecutorIf();
	}
	
	public void run() {
		mining = true;
		this.block.mine(BlockChain.DIFFICULTY);
		if (this.block.isMined()) {
			this.block.getTransactions().forEach(x -> x.processTransaction());
			BlockChain.getInstance().addBlock(this.block, this.originalTransactions);
			executor.execute(executor.execute(new IsChainValid(BlockChain.getInstance())), new Send(BlockChain.getInstance()));
			logger.info("Sending your chain to the rest of nodes.");
		} else {
			BlockChain.getInstance().removePrize(this.originalTransactions.get(this.originalTransactions.size()-1));
			logger.info("Restoring your chain.");
		}
		mining = false;
	}
	
	public static boolean isMining() {
		return mining;
	}

	/**
	 * @return the keepMining
	 */
	public static boolean keepMining() {
		return keepMining;
	}

	/**
	 * @param keepMining the keepMining to set
	 */
	public static void keepMining(boolean keepMining) {
		Miner.keepMining = keepMining;
	}

}
