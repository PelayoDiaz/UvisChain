package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionInput;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.entities.wallet.Wallet;

/**
 * The chain. Class with a singleton pattern which makes that it only can be instantiated once.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class BlockChain implements Serializable, Sendable<BlockChainDto> {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1713493104111662876L;
	
	/** The total amount of money available in  the blockchain */
	private static final double QUARRY = 256000;
	
	/** The minimum value allowed to be sent */
	public static final double MINIMUM_TRANSACTION = 0.01;
	
	/** The amount of funds send by coinbase when mining a block or wallet has been created. */
	public static final double PRIZE = 5;
	
	/** The default difficulty for mining. */
	public static final int DIFFICULTY = 6;
	
	/** Coinbase. It represents the original transaction and wallet. */
	public static final String COIN_BASE = "coinBase";
	
	/** The unique Blockchain to be instantiated. */
	private static BlockChain singleChain;

	/** The BlockChain which contains all the blocks. */
	private List<Block> chain;
	
	/** The list of transactions to be added into the next mined block*/
	private List<Transaction> pendingTransactions;
	
	/** The list of the nodes using the blockchain. */
	private List<Node> nodes;
	
	/** A Map which contains all the unspent outputs that can be used as inputs. */
	private Map<String, TransactionOutput> utxos;
	
	/** A map which contains all the wallets in the chain */
	private Map<String, WalletDto> wallets;
	
	/**
	 * Method to access the blockchain. It creates a new blockchain if it
	 * hasn't been initialized or returns the existing one.
	 * 
	 * @return BlockChain
	 * 			The blockchain of the sistem
	 */
	public static BlockChain getInstance() {
		if (singleChain == null) {
			singleChain = new BlockChain();
		}
		return singleChain;
	}
	
	/**
	 * Private Constructor. It doesn't allow default constructor to be generated. 
	 */
	private BlockChain() {
		this.chain = new ArrayList<Block>();
		this.pendingTransactions = new ArrayList<Transaction>();
		this.nodes = new ArrayList<Node>();
		this.utxos = new HashMap<String, TransactionOutput>();
		this.wallets = new HashMap<String, WalletDto>();
		
		//The wallet and the total amount of coins that can be send in the chain.
		Wallet coinbase = new Wallet(COIN_BASE, COIN_BASE, COIN_BASE);
		this.wallets.put(COIN_BASE, coinbase.toDto());
		TransactionOutput output = new TransactionOutput(coinbase.getAddress(), QUARRY, null);
		this.utxos.put(output.getId(), output);
		
		//The original transaction
		TransactionInput input = new TransactionInput(output.getId());
		input.setUtxo(output);
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input);
		
		Transaction genesisTransaction = new Transaction(coinbase, coinbase.getAddress(), QUARRY, inputs);
		coinbase.signTransaction(genesisTransaction);
		
		//The first block of the chain
		Block genesisBlock = new Block("0");
		genesisBlock.addTransactions(Arrays.asList(genesisTransaction));
		this.chain.add(genesisBlock); //Genesis Block
		this.getLastBlock().mine(1);
	}
	
	/**
	 * Returns the last block in the chain.
	 * 
	 * @return Block
	 * 		the last block in the chain.
	 */
	public Block getLastBlock() {
		return this.chain.get(this.chain.size()-1);
	}
	
	/**
	 * Returns the length of the chain.
	 * 
	 * @return int
	 * 		the length of the chain.
	 */
	public int length() {
		return this.chain.size();
	}
	
	/**
	 * Registers the new node to the chain.
	 * 
	 * @param node
	 * 			The new node to be registered.
	 */
	public void registerNode(Node node) {
		this.nodes.add(node);
	}
	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return new ArrayList<Node>(this.nodes);
	}
	
	/**
	 * Returns the Output from its id.
	 * 
	 * @param outputId 
	 * 			the output id to be found
	 * 
	 * @return the output.
	 */
	public TransactionOutput getUTXO(String outputId) {
		return this.utxos.get(outputId);
	}
	
	/**
	 * Returns the hashMap contained in the class.
	 * 
	 * @return Map<String, TransactionOutput> the hashMap.
	 */
	public Map<String, TransactionOutput> getUTXOMap() {
		return new HashMap<String, TransactionOutput>(this.utxos);
	}
	
	/**
	 * Puts a TransactionOutput into the map. It will be stored with its id.
	 * 
	 * @param outputId 
	 * 				the id which will be the key in the map.
	 * @param output
	 * 				the output to be stored.
	 */
	public void putUTXO(String outputId, TransactionOutput output) {
		this.utxos.put(outputId, output);
	}
	
	/**
	 * Puts a Wallet into the map. It will be stored by its address.
	 * 
	 * @param walletAddres 
	 * 				the address which will be the key in the map.
	 * @param wallet
	 * 				the wallet to be stored.
	 */
	public void putWallet(String walletAddres, WalletDto wallet) {
		if (!this.wallets.containsKey(walletAddres)) {
			this.wallets.put(walletAddres, wallet);
		}
	}
	
	/**
	 * Returns the hashMap of wallet contained in the class.
	 * 
	 * @return Map<String, Wallet> the hashMap.
	 */
	public Map<String, WalletDto> getWallets() {
		return new HashMap<String, WalletDto>(this.wallets);
	}
	
	/**
	 * Removes an utxo updating its transactions to the new leftover.
	 * 
	 * @param outputId
	 * 			The id the TransactionOutput was stored with.
	 * 
	 * @param leftOver
	 * 			The new utxo for the transactions which use the utxo that
	 * 			is going to be deleted.
	 */
	public void removeUTXO(String outputId, TransactionOutput leftOver) {
		if (leftOver!=null) {
			this.updateTransactionsOutput(outputId, leftOver);
		}
		this.utxos.remove(outputId);
	}
	
	/**
	 * Removes a node from the list of nodes conected to the chain.
	 * 
	 * @param url
	 * 			The url of the node to be removed.
	 */
	public void removeNode(String url) {
		this.nodes.remove(new Node(url));
	}
	
	/**
	 * It checks if any input of the pending transactions depends on the output
	 * that is going to be deleted. If it does, then updates the input's reference
	 * to the new leftover output.
	 * 
	 * @param outputId
	 * 			The output that is going to be deleted.
	 * 
	 * @param leftOver
	 * 			The new transaction output stored.
	 */
	private void updateTransactionsOutput(String outputId, TransactionOutput leftOver) {
		this.pendingTransactions.forEach(x -> x.getInputs()
				.stream().filter(y -> y.getOutputId().equals(outputId) && y.getUtxo()==null)
				.forEach(a -> checkTransactionInputs(x, a, leftOver)));
	}
	
	/**
	 * Adds inputs to those transactions whose outputs has been used.
	 * 
	 * @param pendingTransaction
	 * 			The transaction to update.
	 * 
	 * @param inputToUpdate
	 * 			The input who lost the output.
	 * @param leftOver
	 * 			The leftover from the output used.
	 */
	private void checkTransactionInputs(Transaction pendingTransaction, TransactionInput inputToUpdate, TransactionOutput leftOver) {
		String previousOutputId = inputToUpdate.getOutputId();
		inputToUpdate.setOutputId(leftOver.getId());
		double suma = pendingTransaction.getInputs().stream().mapToDouble(x -> this.utxos.get(x.getOutputId()).getValue()).sum();
		while (suma<pendingTransaction.getAmount()) {
			if (!this.searchNextOutput(pendingTransaction, previousOutputId)) {
				return;
			}
			suma = pendingTransaction.getInputs().stream().mapToDouble(x -> this.utxos.get(x.getOutputId()).getValue()).sum();
		}
	}
	
	/**
	 * Searches a new output for a pending transaction
	 * 
	 * @param pendingTransaction
	 * 			The transaction to update.
	 * @param 
	 * @return true if exists more outputs available for the transaction, false if not.
	 */
	private boolean searchNextOutput(Transaction pendingTransaction, String previousOutputId) {
		int previousSize = pendingTransaction.getInputs().size();
		this.utxos.forEach((k, v) -> {
			if (pendingTransaction.getInput(v.getId()) == null
					&& !v.getId().equals(previousOutputId)
					&& v.belongsTo(pendingTransaction.getSenderAddress())) {
				pendingTransaction.addInput(new TransactionInput(v.getId()));
				return;
			}
		});
		return (previousSize+1 == pendingTransaction.getInputs().size());
	}
	
	@Override
	public BlockChainDto toDto() {
		BlockChainDto dto = new BlockChainDto();
		dto.chain = this.chain.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.transactions = this.pendingTransactions.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.nodes = this.nodes;
		dto.utxos = this.utxos.values().stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.wallets = this.wallets.values().stream().collect(Collectors.toList());
		return dto;
	}
	
	/**
	 * Updates the content of the chain from a dto received.
	 * 
	 * @param dto
	 * 			The dto with the new information of the chain.
	 */
	public void update(BlockChainDto dto) {
		if (dto == null) {
			return;
		}
		this.chain = dto.chain.stream().map(x -> new Block(x)).collect(Collectors.toList());
		this.pendingTransactions = dto.transactions.stream().map(x -> new Transaction(x)).collect(Collectors.toList());
		this.nodes = dto.nodes;
		this.utxos = new HashMap<String, TransactionOutput>();
		dto.utxos.forEach(x -> putUTXO(x.id, new TransactionOutput(x)));
		this.wallets = new HashMap<String, WalletDto>();
		dto.wallets.forEach(x -> putWallet(x.address, x));
	}
	
	/**
	 * Returns an instance of the coin base to make transactions when a wallet is created
	 * or a block is mined
	 * 
	 * @return Wallet
	 * 			The coinbase
	 */
	public Wallet getCoinBase() {
		return new Wallet(this.wallets.get(COIN_BASE));
	}
	
	/**
	 * Adds a transaction to the list of transactions which have not been 
	 * added to a block yet.
	 * 
	 * @param transaction
	 * 			The transaction to be added.
	 */
	public void addPendingTransaction(Transaction transaction) {
		this.pendingTransactions.add(transaction);
	}
	
	/**
	 * Removes a transaction if it is send by the coinBase.
	 * 
	 * @param transaction
	 * 			The transaction to be removed.
	 */
	public void removePrize(Transaction transaction) {
		if (transaction.getSenderAddress().equals(COIN_BASE)) {
			this.pendingTransactions.remove(transaction);
		}
	}

	/**
	 * @return the transactions
	 */
	public List<Transaction> getPendingTransactions() {
		return new ArrayList<Transaction>(pendingTransactions);
	}
	
	/**
	 * Adds a new Block to the chain. The new blocks must be mined.
	 * And the last Block into the chain has to be mined.
	 *  
	 * @param block
	 * 			The new block to be added.
	 * 
	 * @param originalTransactions
	 * 			The transactions to be deleted. If a transaction is not valid
	 * 			must be deleted because it can't be processed.
	 * 
	 * @return Boolean
	 * 			True if the block has been added. False if not.
	 */
	public boolean addBlock(Block block, List<Transaction> originalTransactions) {
		if(this.getLastBlock().isMined() && block != null && block.isMined()) {
			this.chain.add(block);
			if (originalTransactions != null) {
				this.pendingTransactions.removeAll(originalTransactions);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the blocks list.
	 * @return List<Block>
	 * 			The list of the blocks in the blockchain.
	 */
	public List<Block> getChain() {
		return new ArrayList<Block>(this.chain);
	}
	
}
