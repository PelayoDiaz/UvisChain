package com.uniovi.uvis.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.WalletService;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;
import com.uniovi.uvis.services.impl.wallet.CreateWallet;
import com.uniovi.uvis.services.impl.wallet.GetBalance;
import com.uniovi.uvis.services.impl.wallet.SendFunds;

@Service
public class WalletServiceImpl implements WalletService {
	
	private Logger logger = LogManager.getLogger(WalletServiceImpl.class);

	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	public WalletServiceImpl() {
		this.executor = new CommandExecutorIf();
	}
	
	@Override
	public Wallet createWallet(WalletDto dto) {
		Wallet createdWallet = executor.execute(new CreateWallet(dto));
		Wallet sender = BlockChain.getInstance().getCoinBase();
		executor.execute(executor.execute(new GetBalance(sender))>=BlockChain.PRIZE, new SendFunds(sender, createdWallet.getAddress(), BlockChain.PRIZE));
		return createdWallet;
	}

	@Override
	public Transaction sendFunds(TransactionDto dto) {
		if (!checkTransaction(dto)) {			
			return null;
		}
		Wallet senderWallet = new Wallet(BlockChain.getInstance().getWallets().get(dto.senderAddress));
		Transaction transaction = executor.execute(executor.execute(
				new GetBalance(senderWallet))>=dto.amount, 
				new SendFunds(senderWallet, dto.receiver, dto.amount));
		return transaction;
	}
	
	/**
	 * Checks if a transaction is valid. It will return true if the sender and
	 * receiver addresses exists and if the amount of money is greater or equals
	 * to the minimum allowed.
	 *   
	 * @param dto 
	 * 			The dto of the transaction to check
	 * @return True if the transaction is valid, False if not.
	 */
	private boolean checkTransaction(TransactionDto dto) {
		if (BlockChain.getInstance().getWallets().get(dto.senderAddress)==null) {
			logger.error("There is no sender address like this contained in the chain.");
			return false;
		}
		if (BlockChain.getInstance().getWallets().get(dto.receiver)==null) {
			logger.error("There is no receiver address like this contained in the chain.");
			return false;
		}
		if (dto.amount<BlockChain.MINIMUM_TRANSACTION) {
			logger.error("The amount of funds to be sent are less than the minimum allowed.");
			return false;
		}
		return true;
	}

	@Override
	public Wallet findByAddress(String address) {
		WalletDto dto = BlockChain.getInstance().getWallets().get(address);
		return (dto != null) ? new Wallet(dto) : null;
	}
}
