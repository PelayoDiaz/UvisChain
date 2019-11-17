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

/**
 * Starts the process of mining and it is the responsible of controlling
 * the thread that makes the process.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class Miner extends Thread {
	
	private Logger logger = LogManager.getLogger(Miner.class);
	
	/** Tells if the Miner is mining or not. */
	private static boolean mining = false;
	
	/** Tells if the Miner must keep mining or not. */
	private static boolean keepMining = true;
	
	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	/** The block to be mined. */
	private Block block;
	
	/** The original transactions that are pending to be processed. */
	private List<Transaction> originalTransactions;
	
	/**
	 * Constructor.
	 * 
	 * @param block
	 * 			The block to be mined.
	 * @param originalTransactions
	 * 			The original transactions that are pending to be processed.
	 */
	public Miner(Block block, List<Transaction> originalTransactions) {
		this.block = block;
		this.originalTransactions = originalTransactions;
		this.executor = new CommandExecutorIf();
	}
	
	/**
	 * Starts the process of mining and process all the pending transactions included in
	 * the block if it gets mined successfully.
	 */
	public void run() {
		mining = true;
		this.block.mine(BlockChain.DIFFICULTY);
		if (this.block.isMined()) {
			this.block.getTransactions().forEach(x -> {
				if (!x.processTransaction()) {
					this.block.removeTransaction(x);
				}
			});
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
