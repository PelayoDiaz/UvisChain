package com.uniovi.uvis.entities.dto;

import java.io.Serializable;

import com.uniovi.uvis.entities.abst.Sendable;

public class Node extends AbstractDto implements Serializable, Sendable<Node> {
	
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public Node toDto() {
		return this;
	}

	
}
