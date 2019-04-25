package com.uniovi.uvis.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class CryptoUtil {

	public static final String HASH_ALGORITHM = "SHA-256";
	
	/**
	 * It uses Google Guava Library to obtain a hash representation of the String
	 * given by parameter.
	 * 
	 * @param input 
	 * 			the String to be transformed to a hash.
	 * @return String 
	 * 			the hash representation of the input.
	 */
	public static String getSha256Hash(String input) {
		HashCode sha256 = Hashing.sha256().hashString(input, StandardCharsets.UTF_8);
		return sha256.toString();
	}
	
	/**
	 * Returns a KeyPair generated with Elliptic curve cryptography.
	 * 
	 * @return KeyPair
	 * 			the KeyPair with the public and private keys.
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			
			keyGen.initialize(ecSpec, random);
			return keyGen.generateKeyPair();
			
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}
}
