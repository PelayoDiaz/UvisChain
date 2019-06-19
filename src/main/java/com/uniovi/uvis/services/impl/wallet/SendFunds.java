package com.uniovi.uvis.services.impl.wallet;

import java.util.ArrayList;

import com.google.common.util.concurrent.AtomicDouble;
import com.uniovi.uvis.communication.handlers.BlockChainSessionHandler;
import com.uniovi.uvis.entities.abst.AbstractSender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.Command;

public class SendFunds extends AbstractSender<Transaction, TransactionDto> implements Command<Transaction>{
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/addTransaction";
	
	private Wallet sender;
	private String receiverAddress;
	private double amount;
	
	/**
	 * Creates a Transaction with an amount of funds to be send to a receiver.
	 * 
	 * @param receiver
	 * 				the receiver's address of the transaction. Is used to obtain the wallet id.
	 * @param amount
	 * 				the amount of funds to be send
	 * @return Transaction
	 * 				the transaction created
	 */
	public SendFunds(Wallet sender, String receiver, double amount) {
		this.sender = sender;
		this.receiverAddress = receiver;
		this.amount = amount;
	}

	@Override
	public Transaction execute() {
		ArrayList<TransactionInput> inputs = getTransactionInputs();
		Transaction transaction = createTransaction(inputs);
		BlockChain.getInstance().addTransaction(transaction);
		this.send(transaction, new BlockChainSessionHandler(), LISTENER);
		return transaction;
	}
	
	/**
	 * It process all the utxos of the wallet and turns them into Transaction inputs until
	 * the value of the utxos is enough to the amount required.
	 * 
	 * @return ArrayList<TransactionInput>
	 * 				the inputs for the transaction.
	 */
	private ArrayList<TransactionInput> getTransactionInputs() {
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		// Calculates the total utxos that can be send with the transaction
		AtomicDouble total = new AtomicDouble(0);
		sender.getUTXOMap().forEach((k, v) -> {
			if (total.get() < amount) {
				total.addAndGet(v.getValue());
				inputs.add(new TransactionInput(v.getId()));
			}
		});
		
		return inputs;
	}
	
	/**
	 * Creates the transaction to be executed.
	 * 
	 * @param receiver
	 * 				The receiver of the funds
	 * @param amount
	 * 				The total funds to be sent.
	 * @param inputs
	 * 				The outputs of the wallet which contains the funds to be sent.
	 * @return Transaction
	 * 				The new transaction to be processed.
	 */
	private Transaction createTransaction(ArrayList<TransactionInput> inputs) {
		Transaction transaction = new Transaction(this.sender, this.receiverAddress, amount, inputs);
		this.sender.signTransaction(transaction);
		inputs.forEach(x -> this.sender.removeUTXO(x.getOutputId()));
		
		return transaction;
	}

}
