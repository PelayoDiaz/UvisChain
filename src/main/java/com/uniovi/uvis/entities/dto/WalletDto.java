package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

public class WalletDto implements Serializable{

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 7621705334565136287L;
	
	public byte[] publicKey;
	public String address;
}
