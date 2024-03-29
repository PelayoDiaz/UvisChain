package com.uniovi.uvis.services.impl.blockchain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.command.Command;

/**
 * Returns true if the chain is valid.  False if not
 * The chain's validity depends on different conditions.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class IsChainValid implements Command<Boolean>{
	
	private Logger logger = LogManager.getLogger(IsChainValid.class);
	
	private BlockChain chain;

	/**
	 * Constructor.
	 * 
	 * @param chain
	 * 			The chain to be validated.
	 */
	public IsChainValid(BlockChain chain) {
		this.chain = chain;
	}

	@Override
	public Boolean execute() {
		try {
			processChain();
		} catch (IllegalStateException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Process all the blocks of the chain and checks the different
	 * conditions for a block to be valid.
	 */
	private void processChain() {
		Block currentBlock;
		Block previousBlock;
		
		for (int i=1; i<this.chain.getChain().size(); i++) {
			currentBlock = this.chain.getChain().get(i);
			previousBlock = this.chain.getChain().get(i-1);
			
			checkPreviousBlockHash(currentBlock, previousBlock);
			checkActualBlockProofOfWork(currentBlock);
			checkIfActualIsMined(currentBlock);
			checkTransactions(currentBlock);
		}
	}
	
	/**
	 * Checks that the current block hasn't been modified. For this purpose
	 * it checks if the saved hash is equals to the resultant calculated hash.
	 * It is also used by nodes when a new block is added to check that, as said, 
	 * it has been generated.
	 * 
	 * @param currentBlock
	 * 		the block to check.
	 */
	private void checkActualBlockProofOfWork(Block currentBlock) {
		if (!currentBlock.getId().equals(currentBlock.calculateHash())) {
			logger.error("The block has been modified!");
			throw new IllegalStateException("The block has been modified!");
		}
	}
	
	/**
	 * Checks that the chain has not been altered or the blocks into modified.
	 * It checks the saved hash from the current block with the calculated hash
	 * from the previous block.
	 * 
	 * @param currentBlock
	 * 			the actual block int the chain
	 * @param previousBlock
	 * 			the previous block in the chain
	 */
	private void checkPreviousBlockHash(Block currentBlock, Block previousBlock) {
		if (!currentBlock.getPreviousHash().equals(previousBlock.getId())) {
			logger.error("The previous block has been modified!");
			throw new IllegalStateException("The previous block has been modified!");
		}
	}
	
	/**
	 * Checks if the actual block is mined or not.
	 * 
	 * @param currentBlock
	 * 			the block to check.
	 */
	private void checkIfActualIsMined(Block currentBlock) {
		if (!currentBlock.isMined()) {
			logger.error("The block is not Mined!");
			throw new IllegalStateException("The block is not Mined!");
		}
	}
	
	/**
	 * Checks that the transactions of a block has not been modified.
	 * 
	 * @param currentBlock
	 * 			The block to check.
	 */
	private void checkTransactions(Block currentBlock) {
		for (Transaction transaction : currentBlock.getTransactions()) {
			//Checks if the Transaction is verified
			if (!transaction.verifySignature()) {
				logger.error("Signature in transaction is invalid!");
				throw new IllegalStateException("Signature in transaction is invalid!");
			}
			//Checks if the inputs or outputs have been modified
			if (transaction.getInputsValue() != transaction.getOutputsValue()) {
				logger.error("Inputs are not equals to outputs in transaction");
				throw new IllegalStateException("Inputs are not equals to outputs in transaction");
			}
			if (!transaction.getOutputs().get(0).belongsTo(transaction.getReceiver())) {
				logger.error("Output receiver is not who it should be");
				throw new IllegalStateException("Output receiver is not who it should be");
			}
			if (transaction.getOutputs().size()>1 && transaction.getOutputs().get(1)!=null 
					&& !transaction.getOutputs().get(1).belongsTo(transaction.getSenderAddress())) {
				logger.error("The left over of the transaction is not for the sender");
				throw new IllegalStateException("The left over of the transaction is not for the sender");
			}
		}
	}

}
