package com.uniovi.uvis.services.impl.block;

import java.util.List;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.blockchain.IsChainValid;
import com.uniovi.uvis.services.impl.blockchain.Send;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;

public class Miner extends Thread {
	
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
			BlockChain.getInstance().addBlock(this.block, this.originalTransactions);
			executor.execute(executor.execute(new IsChainValid(BlockChain.getInstance())), new Send(BlockChain.getInstance()));
		}
		System.out.println("=====================TERMINE======================");
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
