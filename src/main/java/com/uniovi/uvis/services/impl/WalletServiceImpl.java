package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.WalletService;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;
import com.uniovi.uvis.services.impl.wallet.CreateWallet;
import com.uniovi.uvis.services.impl.wallet.GetBalance;
import com.uniovi.uvis.services.impl.wallet.GetWallet;
import com.uniovi.uvis.services.impl.wallet.SendFunds;

@Service
public class WalletServiceImpl implements WalletService {

	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	public WalletServiceImpl() {
		this.executor = new CommandExecutorIf();
	}
	
	@Override
	public Wallet createWallet(String address) {
		Wallet createdWallet = executor.execute(new CreateWallet(address));
		Wallet sender = BlockChain.getInstance().getCoinBase();
		executor.execute(executor.execute(new GetBalance(sender))>5, new SendFunds(sender, createdWallet.getAddress(), 5));
		return createdWallet;
	}

	@Override
	public Transaction sendFunds(String sender, String receiver, double amount) {
		Wallet senderWallet = executor.execute(new GetWallet(sender));
		Transaction transaction = executor.execute(executor.execute(new GetBalance(senderWallet))>amount, new SendFunds(senderWallet, receiver, amount));
		return transaction;
	}

}
