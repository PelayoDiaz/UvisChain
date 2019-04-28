package com.uniovi.uvis.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.bouncycastle.*;

public class CryptoUtil {
	
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
			
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns a String representation of the String given by parameter.
	 * 
	 * @param key
	 * 			the key to obtain the String representation from.
	 * @return String
	 * 			the String representation
	 */
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	/**
	 * Returns an Elliptic Curve Digital Signature from an input an its privateKey.
	 * 
	 * @param privateKey
	 * 			The private key to sign with.
	 * @param input
	 * 			The input to sign.
	 * @return byte[]
	 * 			The sign.
	 */
	public static byte[] getECDSASignature(PrivateKey privateKey, String input) {
		try {
			Signature ecdsaSign = Signature.getInstance("ECDSA", "BC");
			ecdsaSign.initSign(privateKey);
			ecdsaSign.update(input.getBytes("UTF-8"));
			return ecdsaSign.sign();
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Verifies if a given signature is valid or it's been modified.
	 * 
	 * @param publicKey 
	 * 			The publicKey to check with.
	 * @param data
	 * 			The original data
	 * @param signature
	 * 			The signature to verify.
	 * @return Boolean
	 * 			Returns true if the signature is valid. False if not.
	 */
	public static boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes("UTF-8"));
			return ecdsaVerify.verify(signature);
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
