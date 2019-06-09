package com.uniovi.uvis.entities.transactions;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uniovi.uvis.entities.abst.AbstractHasheable;
import com.uniovi.uvis.entities.abst.Sendable;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.util.CryptoUtil;

public class Transaction extends AbstractHasheable implements Serializable, Sendable<TransactionDto> {

	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -4482748116683610567L;
	
	/** The public key of the transaction's sender */
	private PublicKey sender;
	
	/** The address of the transaction's sender */
	private String senderAddress;
	
	/** The address of the transaction's receiver */
	private String receiver;
	
	/** The amount of money to send */
	private double amount;
	
	/** To prevent anybody else from spending funds in our wallet */
	private byte[] signature;
	
	/** It will be used to reference the outputs sent by the sender of the transaction */
	private List<TransactionInput> inputs;
	
	/** The outputs to be sent in the transaction */
	private List<TransactionOutput> outputs;
	
	public Transaction(Wallet sender, String receiver, double amount, ArrayList<TransactionInput> inputs) {
		super();
		this.sender = sender.getPublicKey();
		this.senderAddress = sender.getId();
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		this.outputs = new ArrayList<TransactionOutput>();
		this.id = calculateHash();
	}
	
	public Transaction(TransactionDto dto) {
		this.id = dto.id;
		this.sender = CryptoUtil.fromByteToPublicKey(dto.sender);
		this.senderAddress = dto.senderAddress;
		this.receiver = dto.receiver;
		this.amount = dto.amount;
		this.signature = dto.signature;
		this.timeStamp = dto.timeStamp;
		this.inputs = dto.inputs.stream().map(x -> new TransactionInput(x)).collect(Collectors.toList());
		this.outputs = dto.outputs.stream().map(x -> new TransactionOutput(x)).collect(Collectors.toList());;
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
		this.inputs.forEach(x -> x.setUtxo(BlockChain.getInstance().getUTXO(x.getOutputId())));
	
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
		if (leftOver>0) {
			this.outputs.add(new TransactionOutput(this.senderAddress, leftOver, this.id));
		}
				
		//Add outputs to the unspent map
		this.outputs.forEach(x -> BlockChain.getInstance().putUTXO(x.getId(), x));
	}
	
	/**
	 * Removes all the utxos that has been used in the transaction for not being 
	 * used in future transactions
	 */
	private void removeOutputs() {
		this.inputs.stream().filter(y -> y.getUtxo()!=null)
								.forEach(x -> BlockChain.getInstance().removeUTXO(x.getUtxo().getId()));
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
	public double getInputsValue() {
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
				this.senderAddress +
				this.receiver +
				String.valueOf(this.amount);
	}

	/**
	 * @return the sender
	 */
	public PublicKey getSender() {
		return sender;
	}

	/**
	 * @return the senderAddress
	 */
	public String getSenderAddress() {
		return senderAddress;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}	
	
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}


	@Override
	public String calculateHash() {
		return CryptoUtil.getSha256Hash(
				CryptoUtil.getStringFromKey(this.sender) +
				this.senderAddress +
				this.receiver + 
				this.amount +
				this.timeStamp);
	}

	/**
	 * @return the outputs
	 */
	public List<TransactionOutput> getOutputs() {
		return outputs;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	@Override
	public TransactionDto toDto() {
		TransactionDto dto = new TransactionDto();
		dto.id = this.id;
		dto.sender = this.sender.getEncoded();
		dto.senderAddress = this.senderAddress;
		dto.receiver = this.receiver;
		dto.amount = this.amount;
		dto.signature = this.signature;
		dto.timeStamp = this.timeStamp;
		dto.inputs = this.inputs.stream().map(x -> x.toDto()).collect(Collectors.toList());
		dto.outputs = this.outputs.stream().map(x -> x.toDto()).collect(Collectors.toList());
		return dto;
	}
	
}
