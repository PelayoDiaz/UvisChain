package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.WalletService;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;
import com.uniovi.uvis.services.impl.wallet.CreateWallet;
import com.uniovi.uvis.services.impl.wallet.GetBalance;
import com.uniovi.uvis.services.impl.wallet.SetActiveWallet;
import com.uniovi.uvis.services.impl.wallet.SendFunds;

@Service
public class WalletServiceImpl implements WalletService {

	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	public WalletServiceImpl() {
		this.executor = new CommandExecutorIf();
	}
	
	@Override
	public Wallet createWallet(String username, String password, String name, String surname1, String surname2) {
		Wallet createdWallet = executor.execute(new CreateWallet(username, password, name, surname1, surname2));
		Wallet sender = BlockChain.getInstance().getCoinBase();
		executor.execute(executor.execute(new GetBalance(sender))>5, new SendFunds(sender, createdWallet.getId(), 5));
		return createdWallet;
	}

	@Override
	public Transaction sendFunds(String receiver, double amount) {
		Wallet senderWallet = BlockChain.getInstance().getActiveWallet();
		Transaction transaction = executor.execute(executor.execute(new GetBalance(senderWallet))>amount, new SendFunds(senderWallet, receiver, amount));
		return transaction;
	}

	@Override
	public boolean setActiveWallet(String username, String password) {
		return executor.execute(new SetActiveWallet(username, password));
	}

}
