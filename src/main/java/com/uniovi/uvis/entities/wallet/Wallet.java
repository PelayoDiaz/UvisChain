package com.uniovi.uvis.entities.wallet;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.uniovi.uvis.util.CryptoUtil;

public class Wallet implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1687361974319098902L;
	
	/** The private key to keep in secret to decode the information */
	public PrivateKey privateKey;
	
	/** The public key to share with people which send you information */
	public PublicKey publicKey;
	
	public Wallet() {
		KeyPair keyPair = CryptoUtil.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}

}
