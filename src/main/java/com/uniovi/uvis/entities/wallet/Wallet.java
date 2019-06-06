package com.uniovi.uvis.entities.wallet;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.util.CryptoUtil;

public class Wallet implements Serializable, Sendable<WalletDto> {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1687361974319098902L;
	
	/** The private key to keep in secret to decode the information */
	private PrivateKey privateKey;
	
	/** The public key to share with people which send you information */
	private PublicKey publicKey;
	
	/** A hashMap which contains all the unspent outputs of the wallet that can be used as inputs. */
	private Map<String, TransactionOutput> utxos;
	
	/** The address of the wallet. It will be used to reference the wallet when it is the receiver of the sent funds. */
	private String address;
	
	public Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
		this.utxos = new HashMap<String, TransactionOutput>();
	}
	
	public Wallet(String address) {
		this();
		this.address = address;
	}
	
//	/**
//	 * Returns the total balance of the wallet and stores the UTXOs in it.
//	 * 
//	 * @return double
//	 * 				the total balance of the utxos in the wallet
//	 */
//	public double getBalance() {
//		AtomicDouble total = new AtomicDouble(0);
//		BlockChain.getInstance().getUTXOMap().forEach((k, v) -> {
//			if (v.belongsTo(address)) {
//				utxos.put(v.getId(), v);
//				total.addAndGet(v.getValue());
//			}
//		});
//		return total.get();
//	}
	
//	/**
//	 * Creates a Transaction with an amount of funds to be send to a receiver.
//	 * 
//	 * @param receiver
//	 * 				the receiver of the transaction
//	 * @param amount
//	 * 				the amount of funds to be send
//	 * @return Transaction
//	 * 				the transaction created
//	 */
//	public Transaction sendFunds(String receiver, double amount) {
//		if (this.getBalance() < amount) { //Checks if there is money enough. 
//			return null;
//		}
//		ArrayList<TransactionInput> inputs = getTransactionInputs(amount);	
//		return createTransaction(receiver, amount, inputs);
//	}
//	
//	/**
//	 * It process all the utxos of the wallet and turns them into Transaction inputs until
//	 * the value of the utxos is enough to the amount required.
//	 * 
//	 * @param amount
//	 * 				the amount to be send
//	 * @return ArrayList<TransactionInput>
//	 * 				the inputs for the transaction.
//	 */
//	private ArrayList<TransactionInput> getTransactionInputs(double amount) {
//		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
//
//		// Calculates the total utxos that can be send with the transaction
//		AtomicDouble total = new AtomicDouble(0);
//		this.utxos.forEach((k, v) -> {
//			if (total.get() < amount) {
//				total.addAndGet(v.getValue());
//				inputs.add(new TransactionInput(v.getId()));
//			}
//		});
//		
//		return inputs;
//	}
//	
//	/**
//	 * Creates the transaction to be executed.
//	 * 
//	 * @param receiver
//	 * 				The receiver of the funds
//	 * @param amount
//	 * 				The total funds to be sent.
//	 * @param inputs
//	 * 				The outputs of the wallet which contains the funds to be sent.
//	 * @return Transaction
//	 * 				The new transaction to be processed.
//	 */
//	private Transaction createTransaction(String receiver, double amount, ArrayList<TransactionInput> inputs) {
//		Transaction transaction = new Transaction(this.publicKey, this.address, receiver, amount, inputs);
//		this.signTransaction(transaction);
//		inputs.forEach(x -> this.utxos.remove(x.getOutputId()));
//		
//		return transaction;
//	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
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
	 * Removes a TransactionOutput from the map.
	 * 
	 * @param outputId the id the TransactionOutput was stored with.
	 */
	public void removeUTXO(String outputId) {
		this.utxos.remove(outputId);
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
	 * Signs a transaction with the private key of the wallet.
	 * 
	 * @param transaction
	 * 			The transaction to be signed.
	 */
	public void signTransaction(Transaction transaction) {
		if (transaction.getSender().equals(this.publicKey)) {
			transaction.generateSignature(this.privateKey);
		}
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public WalletDto toDto() {
		WalletDto dto = new WalletDto();
		dto.publicKey = this.publicKey.getEncoded();
		dto.address = this.address;
		return dto;
	}

}
