package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.util.CryptoUtil;

public class Transaction implements Serializable {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -4482748116683610567L;

	/** The id of the transaction */
	private String id;
	
	/** The public key of the transaction's sender */
	private PublicKey sender;
	
	/** The public key of the transaction's receiver */
	private PublicKey receiver;
	
	/** The amount of money to send */
	private double amount;
	
	/** To prevent anybody else from spending funds in our wallet */
	private byte[] signature;
	
	/** The actual time at the moment of the creation of the Transaction */
	private long timeStamp;
	
	/** It will be used to reference the outputs sent by the sender of the transaction */
	private ArrayList<TransactionInput> inputs;
	
	/** The outputs to be sent in the transaction */
	private ArrayList<TransactionOutput> outputs;
	
	public Transaction(PublicKey sender, PublicKey receiver, double amount, ArrayList<TransactionInput> inputs) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		this.outputs = new ArrayList<TransactionOutput>();
		this.timeStamp = new Date().getTime();
		this.id = calculateHash();
	}
	
	/**
	 * Process the transaction. It creates new utxos for the receiver and eliminates 
	 * the inputs for the sender.
	 * 
	 * @return Boolean.
	 * 				True if everything is correct. False if the transaction is not verified.
	 */
	public boolean processTransaction() {
		if (!this.verifySignature()) {
			return false;
		}
		//Collects all the unspent outputs
		this.inputs.forEach(x -> x.setUtxo(UTXOs.getInstance().get(x.getOutputId())));
	
		if (!this.isValid()) {
			return false;
		}
		this.generateOutputs();
		this.removeOutputs();
		return true;
	}
	
	/**
	 * Generate the different outputs to be sent. It generates an output with the amount to be
	 * sent to the receiver, and another with the left over of the sender.
	 */
	private void generateOutputs() {
		double leftOver = this.getInputsValue() - this.amount;
		// The amount to be send to the receiver
		this.outputs.add(new TransactionOutput(this.receiver, this.amount, this.id));
		
		//The left over to be send back to the sender.
		this.outputs.add(new TransactionOutput(this.sender, leftOver, this.id));
		
		//Add outputs to the unspent map
		this.outputs.forEach(x -> UTXOs.getInstance().put(x.getId(), x));
	}
	
	/**
	 * Removes all the utxos that has been used in the transaction for not being 
	 * used in future transactions
	 */
	private void removeOutputs() {
		this.inputs.stream().filter(y -> y.getUtxo()!=null)
								.forEach(x -> UTXOs.getInstance().remove(x.getUtxo().getId()));
	}
	
	/**
	 * Sum all the outputs value and says if it is enough to be a valid transaction.
	 * 
	 * @return true if it is enough, false if not.
	 */
	private boolean isValid() {
		double total = this.getInputsValue();
		return this.amount >= BlockChain.MINIMUM_TRANSACTION && 
				total >= BlockChain.MINIMUM_TRANSACTION && total >= this.amount;
	}
	
	/**
	 * Sum all the outputs of the inputs value.
	 * 
	 * @return the outputs value.
	 */
	private double getInputsValue() {
		//Sum all the values of the outputs
		return this.inputs.stream().filter(y -> y.getUtxo()!=null)
								.mapToDouble(x -> x.getUtxo().getValue()).sum();
	}
	
	/**
	 * Returns the sum of the outputs.
	 * 
	 * @return the sum of the outputs.
	 */
	public double getOutputsValue() {
		return this.outputs.stream().mapToDouble(x -> x.getValue()).sum();
	}
	
	/**
	 * Sign all the data that can not be modified.
	 * 
	 * @param privateKey
	 * 			The private key to sign with.
	 */
	public void generateSignature(PrivateKey privateKey) {
		this.signature = CryptoUtil.getECDSASignature(privateKey, getData());
	}
	
	/**
	 * Verifies if the transaction has been modified.
	 * A transaction is valid if the data to be signed has not changed.
	 * 
	 * @return Boolean
	 * 			True if the signature is valid. False if not
	 */
	public boolean verifySignature() {
		return (signature == null) ? false : 
			CryptoUtil.verifyECDSASignature(this.sender, getData(), this.signature);
	}
	
	/**
	 * Get the data that can not be modified.
	 * 
	 * @return String
	 * 			The String of the data
	 */
	private String getData() {
		return CryptoUtil.getStringFromKey(this.sender) +
				CryptoUtil.getStringFromKey(this.receiver) +
				String.valueOf(this.amount);
	}

	/**
	 * @return the sender
	 */
	public PublicKey getSender() {
		return sender;
	}

	/**
	 * @return the receiver
	 */
	public PublicKey getReceiver() {
		return receiver;
	}
	
	
	/**
	 * Gets the hash of the transaction. It is used as its id.
	 * 
	 * @return String
	 * 			the hash of the transaction
	 */
	private String calculateHash() {
		return CryptoUtil.getSha256Hash(
				CryptoUtil.getStringFromKey(this.sender) +
				CryptoUtil.getStringFromKey(this.receiver) +
				this.amount +
				this.timeStamp);
	}	
}
