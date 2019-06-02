package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

public class Node implements Serializable {
	
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -6785201912005020161L;

	/** The ip address and the port where the node server is running */
	private String url;
	
	public Node() {}
	
	public Node(String url) {
		super();
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	
}
