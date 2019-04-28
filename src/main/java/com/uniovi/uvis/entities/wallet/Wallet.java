package com.uniovi.uvis.entities.wallet;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.util.CryptoUtil;

public class Wallet implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1687361974319098902L;
	
	/** The private key to keep in secret to decode the information */
	private PrivateKey privateKey;
	
	/** The public key to share with people which send you information */
	private PublicKey publicKey;
	
	public Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
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

}
