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
	
	/** The name of the wallet given by the user. */
	private String name;
	
	/** The address of the wallet. It is used to be referenced when sending or receiving funds. */
	private String address;
	
	/** The username of wallet's owner. */
	private String username;
	
	private Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
		this.utxos = new HashMap<String, TransactionOutput>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 * 			The name of the wallet.
	 * @param address
	 * 			The address of the wallet.
	 * @param username
	 * 			The username of the wallet.
	 */
	public Wallet(String name, String address, String username) {
		this();
		this.name = name;
		this.address = address;
		this.username = username;
	}
	
	/**
	 * The dto to be converted into a wallet.
	 * 
	 * @param dto
	 * 			The DTO.
	 */
	public Wallet(WalletDto dto) {
		this();
		this.name = dto.name;
		this.address = dto.address;
		this.username = dto.username;
	}

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
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	@Override
	public WalletDto toDto() {
		WalletDto dto = new WalletDto();
		dto.name = this.name;
		dto.address = this.address;
		dto.username = this.username;
		return dto;
	}

}
