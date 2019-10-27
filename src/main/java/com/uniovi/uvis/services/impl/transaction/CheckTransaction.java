package com.uniovi.uvis.services.impl.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.services.impl.command.Command;

public class CheckTransaction implements Command<Boolean> {

	private Logger logger = LogManager.getLogger(CheckTransaction.class);
	
	private TransactionDto transactionDto;
	
	/**
	 * Checks if a transaction is valid. It will return true if the sender and
	 * receiver addresses exists and if the amount of money is greater or equals
	 * to the minimum allowed.
	 *   
	 * @param transactionDto 
	 * 			The transactionDto of the transaction to check
	 * @return True if the transaction is valid, False if not.
	 */
	public CheckTransaction(TransactionDto transactionDto) {
		this.transactionDto = transactionDto;
	}

	@Override
	public Boolean execute() {
		if (BlockChain.getInstance().getWallets().get(this.transactionDto.senderAddress)==null) {
			logger.error("There is no sender address like this contained in the chain.");
			return Boolean.FALSE;
		}
		if (BlockChain.getInstance().getWallets().get(this.transactionDto.receiver)==null) {
			logger.error("There is no receiver address like this contained in the chain.");
			return Boolean.FALSE;
		}
		if (this.transactionDto.amount<BlockChain.MINIMUM_TRANSACTION) {
			logger.error("The amount of funds to be sent are less than the minimum allowed.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
