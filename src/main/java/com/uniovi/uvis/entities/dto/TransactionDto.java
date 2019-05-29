package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

public class TransactionDto implements Serializable {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -2161764576648183045L;
	
	private String content;

	public TransactionDto() {}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
