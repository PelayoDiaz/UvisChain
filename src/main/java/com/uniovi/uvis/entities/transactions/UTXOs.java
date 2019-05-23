package com.uniovi.uvis.entities.transactions;

import java.util.HashMap;
import java.util.Map;


/**
 * Class with a singleton pattern which makes that it only can be instantiated once.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class UTXOs {
	
	/** The unique UTXOs to be instantiated. */
	private static UTXOs singleUTXOs;
	
	/** A hashMap which contains all the unspent outputs that can be used as inputs. */
	private Map<String, TransactionOutput> utxos;
	
	/**
	 * Gets the single UTXOs reference.
	 * 
	 * @return UTXOs reference.
	 */
	public static UTXOs getInstance() {
		if (singleUTXOs==null) {
			singleUTXOs = new UTXOs();
		}
		return singleUTXOs;
	}
	
	/**
	 * Returns the Output from its id.
	 * 
	 * @param outputId 
	 * 			the output id to be found
	 * 
	 * @return the output.
	 */
	public TransactionOutput get(String outputId) {
		return this.utxos.get(outputId);
	}
	
	/**
	 * Puts a TransactionOutput into the map. It will be stored with its id.
	 * 
	 * @param outputId 
	 * 				the id which will be the key in the map.
	 * @param output
	 * 				the output to be stored.
	 */
	public void put(String outputId, TransactionOutput output) {
		this.utxos.put(outputId, output);
	}
	
	/**
	 * Removes a TransactionOutput from the map.
	 * 
	 * @param outputId the id the TransactionOutput was stored with.
	 */
	public void remove(String outputId) {
		this.utxos.remove(outputId);
	}
	
	/**
	 * Private Constructor. It doesn't allow default constructor to be generated. 
	 */
	private UTXOs() {
		this.utxos = new HashMap<String, TransactionOutput>();
	}


}
