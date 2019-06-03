package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	/** Code executor for services. */
	private CommandExecutor executor;
	
	public TransactionServiceImpl() {
		this.executor = new CommandExecutor();
	}

	@Override
	public Transaction sendFunds() {
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		TransactionOutput output1 = new TransactionOutput(walletA.getPublicKey(), 3, null);
		TransactionOutput output2 = new TransactionOutput(walletA.getPublicKey(), 0.5, null);
		BlockChain.getInstance().putUTXO(output1.getId(), output1);
		BlockChain.getInstance().putUTXO(output2.getId(), output2);
		
		Transaction transaction = walletA.sendFunds(walletB.getPublicKey(), 3.0);
		BlockChain.getInstance().addTransaction(transaction);
		
		return transaction;
	}

	@Override
	public double getBalance(Wallet wallet) {
		AtomicDouble total = new AtomicDouble(0);
		BlockChain.getInstance().getUTXOMap().forEach((k, v) -> {
			if (v.belongsTo(wallet.getPublicKey())) {
				wallet.putUTXO(v.getId(), v);
				total.addAndGet(v.getValue());
			}
		});
		return total.get();
	}

}
