package com.uniovi.uvis.entities.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
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
public class BlockChain implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 1713493104111662876L;
	
	/** The minimum value allowed to be sent */
	public static final double MINIMUM_TRANSACTION = 0.1;
	
	public static final int DIFFICULTY = 4;
	
	public static final String COIN_BASE = "coinBase";
	
	/** The unique Blockchain to be instantiated. */
	private static BlockChain singleChain;

	/** The BlockChain which contains all the blocks. */
	private List<Block> chain;
	
	/** The list of transactions to be added into the next mined block*/
	private List<Transaction> transactions;
	
	/** The list of the nodes using the blockchain. */
	private List<Node> nodes;
	
	/** A Map which contains all the unspent outputs that can be used as inputs. */
	private Map<String, TransactionOutput> utxos;
	
	/** A map which contains all the wallets in the chain */
	private Map<String, WalletDto> wallets;

	
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
		this.transactions = new ArrayList<Transaction>();
		this.nodes = new ArrayList<Node>();
		this.utxos = new HashMap<String, TransactionOutput>();
		this.wallets = new HashMap<String, WalletDto>();
		
		//The wallet and the total amount of coins that can be send in the chain.
		Wallet coinbase = new Wallet(COIN_BASE);
		wallets.put(COIN_BASE, coinbase.toDto());
		TransactionOutput output = new TransactionOutput(coinbase.getAddress(), 100, null);
		utxos.put(output.getId(), output);
		
		//The original transaction
		TransactionInput input = new TransactionInput(output.getId());
		input.setUtxo(output);
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		inputs.add(input);
		
		Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), coinbase.getAddress(), coinbase.getAddress(), 100, inputs);
		coinbase.signTransaction(genesisTransaction);
		
		//The first block of the chain
		Block genesisBlock = new Block("0");
		genesisBlock.addTransaction(genesisTransaction);
		this.chain.add(genesisBlock); //Genesis Block
		this.getLastBlock().mine(1);
	}
	
	/**
	 * Adds a new block to the chain. To do this, the last Block
	 * into the chain has to be mined.
	 * 
	 * @param block
	 * 			the block to be added.
	 */
	public void addBlock(Block block) { //Pasarle las transacciones actuales al nodo
		block.addTransactions(this.transactions);
		block.mine(DIFFICULTY);
		if (getLastBlock().isMined() && block.isMined()) { 
			this.chain.add(block);
		}
	}
	
	/**
	 * Returns true if the chain is valid.  False if not
	 * The chain's validity depends on different conditions.
	 * 
	 * @return Boolean
	 * 		true if the chain is valid.  False if not.
	 */
	public boolean isChainValid() {
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
		
		for (int i=1; i<chain.size(); i++) {
			currentBlock = chain.get(i);
			previousBlock = chain.get(i-1);
			
			checkActualBlockHash(currentBlock);
			checkPreviousBlockHash(currentBlock, previousBlock);
			checkIfActualIsMined(currentBlock);
			checkTransactions(currentBlock);
		}
	}
	
	private void checkTransactions(Block currentBlock) {
		for (Transaction transaction : currentBlock.getTransactions()) {
			//Checks if the Transaction is verified
			if (!transaction.verifySignature()) {
				throw new IllegalStateException("Signature in transaction is invalid!");
			}
			//Checks if the inputs or outputs have been modified
			if (transaction.getInputsValue() != transaction.getOutputsValue()) {
				throw new IllegalStateException("Inputs are not equals to outputs in transaction");
			}
			if (!transaction.getOutputs().get(0).belongsTo(transaction.getReceiver())) {
				throw new IllegalStateException("Output receiver is not who it should be");
			}
			if (transaction.getOutputs().get(1)!=null && !transaction.getOutputs().get(1).belongsTo(transaction.getReceiver())) {
				throw new IllegalStateException("The left over of the transaction is not for the sender");
			}
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
			throw new IllegalStateException("The block is not Mined!");
		}
	}

	/**
	 * Checks that the current block hasn't been modified. For this purpose
	 * it checks if the saved hash is equals to the resultant calculated hash.
	 * 
	 * @param currentBlock
	 * 		the block to check.
	 */
	private void checkActualBlockHash(Block currentBlock) {
		if (!currentBlock.getId().equals(currentBlock.calculateHash())) {
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
			throw new IllegalStateException("The previous block has been modified!");
		}
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
		this.wallets.put(walletAddres, wallet);
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
	 * Removes a TransactionOutput from the map.
	 * 
	 * @param outputId the id the TransactionOutput was stored with.
	 */
	public void removeUTXO(String outputId) {
		this.utxos.remove(outputId);
	}
	
	public BlockChainDto toDto() {
		BlockChainDto dto = new BlockChainDto();
		dto.chain = this.chain.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.transactions = this.transactions.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.nodes = this.nodes;
		dto.utxos = this.utxos.values().stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.wallets = this.wallets.values().stream().collect(Collectors.toList());
		return dto;
	}
	
	public void update(BlockChainDto dto) {
		this.chain = dto.chain.stream().map(x -> new Block(x)).collect(Collectors.toList());
		this.transactions = dto.transactions.stream().map(x -> new Transaction(x)).collect(Collectors.toList());
		this.nodes = dto.nodes;
		this.utxos = new HashMap<String, TransactionOutput>();
		dto.utxos.forEach(x -> putUTXO(x.id, new TransactionOutput(x)));
	}
	
	/**
	 * Adds a transaction to the list of transactions which have not been 
	 * added to a block yet.
	 * 
	 * @param transaction
	 * 			The transaction to be added.
	 */
	public void addTransaction(Transaction transaction) {
		this.transactions.add(transaction);
	}

	@Override
	public String toString() {
		String gsonChain = new GsonBuilder().setPrettyPrinting().create().toJson(this.chain);
		return gsonChain;
	}
	
}
