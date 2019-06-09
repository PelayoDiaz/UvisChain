package com.uniovi.uvis.entities.wallet;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.dto.UserDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.transactions.TransactionOutput;
import com.uniovi.uvis.util.CryptoUtil;

public class Wallet extends AbstractHasheable implements Serializable, Sendable<WalletDto> {
	
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
	
	/** The user the wallet belongs to */
	private UserDto user;
	
	private Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
		this.utxos = new HashMap<String, TransactionOutput>();
		this.user = new UserDto();
	}
	
	public Wallet(String username, String password, String name, String surname1, String surname2) {
		this();
		this.user.username = username;
		this.user.password = password;
		this.user.name = name;
		this.user.surname1 = surname1;
		this.user.surname2 = surname2;
		this.id = this.calculateHash();
	}
	
	public Wallet(WalletDto dto) {
		this();
		this.id = dto.id;
		this.user = dto.user;
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
	
	@Override
	public WalletDto toDto() {
		WalletDto dto = new WalletDto();
		dto.id = this.id;
		dto.user = this.user;
		return dto;
	}

	/**
	 * @return the user
	 */
	public UserDto getUser() {
		return user;
	}

	@Override
	public String calculateHash() {
		return CryptoUtil.getSha256Hash(
				this.user.username +
				this.user.password +
				this.user.name + 
				this.user.surname1 + 
				this.user.surname2 +
				this.timeStamp);
	}

}
