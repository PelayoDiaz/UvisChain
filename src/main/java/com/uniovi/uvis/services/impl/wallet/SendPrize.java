package com.uniovi.uvis.services.impl.wallet;

import java.util.ArrayList;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.wallet.Wallet;

public class SendPrize extends SendFunds {	

	/**
	 * Creates a Transaction with an amount of funds to be send to a receiver but without sending it
	 * to the rest of nodes.
	 * 
	 * @param receiver
	 * 				the receiver's address of the transaction. Is used to obtain the wallet id.
	 * @param amount
	 * 				the amount of funds to be send
	 * @return Transaction
	 * 				the transaction created
	 */
	public SendPrize(Wallet sender, String receiver, double amount) {
		super(sender, receiver, amount);
	}

	@Override
	public Transaction execute() {
		ArrayList<TransactionInput> inputs = getTransactionInputs();
		Transaction transaction = createTransaction(inputs);
		BlockChain.getInstance().addTransaction(transaction);
		return transaction;
	}
	
	
}
