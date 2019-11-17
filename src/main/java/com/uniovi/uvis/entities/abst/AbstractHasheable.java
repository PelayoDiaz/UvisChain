package com.uniovi.uvis.entities.abst;

import java.util.Date;

/**
 * Abstract class to stores all the attributes that the classes which generate hashes need.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public abstract class AbstractHasheable {
	
	/** The hash of the current object */
	protected String id;

	/** The actual time at the moment of the creation of the Block */
	protected long timeStamp;
	
	public AbstractHasheable() {
		this.timeStamp = new Date().getTime();
	}

	/**
	 * Returns the timeStamp.
	 * 
	 * @return Long
	 * 			the timeStamp.
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns the hash.
	 * 
	 * @return String
	 * 			the hash of the entity.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns a hash representation of the actual information contained
	 * into the object. This hash will stay inmutable through the time that
	 * the object exists.
	 * 
	 * @return String
	 * 			the hash representation of the object.
	 */
	public abstract String calculateHash();
}
