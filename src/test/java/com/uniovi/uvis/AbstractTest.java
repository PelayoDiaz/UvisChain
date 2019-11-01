package com.uniovi.uvis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.command.CommandExecutor;

public abstract class AbstractTest {

	/** Code executor for commands. */
	protected static CommandExecutor executor;
	
	/** The data of the original blockchain */
	protected static BlockChainDto originalBlockchain;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		executor = new CommandExecutor();
		originalBlockchain = BlockChain.getInstance().toDto();
	}
	
	@After
	/**
	 * We restore the original blockchain after every test.
	 * 
	 * @throws Exception
	 */
	public void tearDown() throws Exception {
		BlockChain.getInstance().update(originalBlockchain);
	}
	
	/**
	 * Creates a list of transactions.
	 * 
	 * @return List<Transaction>
	 * 			The list of created Transactions.
	 */
	public List<Transaction> createTransactionsList() {
		List<Transaction> transactions = new ArrayList<Transaction>(Arrays.asList(createValidTransaction(1)));
		return transactions;
	}
	
	/**
	 * It creates a valid transaction to be used while testing
	 * 
	 * @param the amount of money to create the transaction
	 * 
	 * @return Transaction
	 * 			The created Transaction.
	 */
	public Transaction createValidTransaction(int amount) {
		Wallet coinbase = new Wallet("coinBase", "coinBase", "coinBase");
		TransactionOutput output = new TransactionOutput(coinbase.getAddress(), 10, null);
		BlockChain.getInstance().putUTXO(output.getId(), output);
		
		TransactionInput input = new TransactionInput(output.getId());
		input.setUtxo(output);
		
		Transaction genesisTransaction = new Transaction(coinbase, coinbase.getAddress(), amount, new ArrayList<TransactionInput>(Arrays.asList(input)));
		coinbase.signTransaction(genesisTransaction);
		
		return genesisTransaction;
	}
	
	/**
	 * Creates and includes a valid wallet into the blockchain.
	 * @param name
	 * 			The name of the wallet.
	 * @param address
	 * 			The address of the wallet.
	 * @param username
	 * 			The username of the wallet.
	 * @return Wallet
	 * 			The created wallet.
	 */
	public Wallet createValidWallet(String name, String address, String username) {
		Wallet wallet = new Wallet(name, address, username);
		BlockChain.getInstance().putWallet(wallet.getAddress(), wallet.toDto());
		return wallet;
	}

}
