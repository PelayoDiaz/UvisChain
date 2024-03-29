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

/**
 * The transactions to be included into a block. The represent the money movement.
 * 
 * @author Pelayo Díaz Soto
 *
 */
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
	
	/**
	 * Constructor
	 * 
	 * @param sender
	 * 			The sender Wallet.
	 * @param receiver
	 * 			The receiver address.
	 * @param amount
	 * 			The amount of funds to be sent.
	 * @param inputs
	 * 			The inputs to fill the transaction.
	 */
	public Transaction(Wallet sender, String receiver, double amount, ArrayList<TransactionInput> inputs) {
		super();
		this.sender = sender.getPublicKey();
		this.senderAddress = sender.getAddress();
		this.receiver = receiver;
		this.amount = amount;
		this.inputs = inputs;
		this.outputs = new ArrayList<TransactionOutput>();
		this.id = calculateHash();
	}
	
	/**
	 * The DTO to be converted.
	 * 
	 * @param dto
	 * 			DTO.
	 */
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

		if (!this.isValid(this.getInputsValue())) {
			return false;
		}
		TransactionOutput leftOver = this.generateOutputs();
		this.removeOutputs(leftOver);
		return true;
	}
	
	/**
	 * Generate the different outputs to be sent. It generates an output with the amount to be
	 * sent to the receiver, and another with the left over of the sender.
	 * 
	 * @return TransactionOutput
	 * 			The left over of the transaction. It will be used to change the UTXO reference
	 * 			for those transactions which are pending of being processed.
	 */
	private TransactionOutput generateOutputs() {
		double leftOver = this.getInputsValue() - this.amount;
		// The amount to be send to the receiver
		this.outputs.add(new TransactionOutput(this.receiver, this.amount, this.id));
		
		//The left over to be send back to the sender.
		TransactionOutput trLeftOver = null;
		if (leftOver>0) {
			trLeftOver = new TransactionOutput(this.senderAddress, leftOver, this.id); 
			this.outputs.add(trLeftOver);
		}
				
		//Add outputs to the unspent map
		this.outputs.forEach(x -> BlockChain.getInstance().putUTXO(x.getId(), x));
		
		return trLeftOver;
	}
	
	/**
	 * Removes all the utxos that has been used in the transaction for not being 
	 * used in future transactions
	 * 
	 * @param leftOver
	 * 			The transaction output which is the leftover resulted from generating
	 * 			outputs.
	 */
	private void removeOutputs(TransactionOutput leftOver) {
		this.inputs.stream().filter(y -> y.getUtxo()!=null)
								.forEach(x -> BlockChain.getInstance().removeUTXO(x.getUtxo().getId(), leftOver));
	}
	
	/**
	 * Checks if a value is enough to make a transaction valid.
	 * 
	 * @param valueToCheck
	 * 			The value to check
	 * 
	 * @return true if it is enough, false if not.
	 */
	private boolean isValid(double valueToCheck) {
		return this.amount >= BlockChain.MINIMUM_TRANSACTION && 
				valueToCheck >= BlockChain.MINIMUM_TRANSACTION && valueToCheck >= this.amount;
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
	 * Returns an input by searching it by its outputId.
	 * 
	 * @param outputId
	 * 			The id of the output which the input references to.
	 * @return TransactionInput
	 * 			The input to search or null if it doesn't exists.
	 */
	public TransactionInput getInput(String outputId) {
		return this.inputs.stream().filter(x -> x.getOutputId().equals(outputId)).findFirst().orElse(null);
	}
	
	/**
	 * Says if a transaction is valid to be included in a block. It checks its signature
	 * and if its amount of money is enough.
	 * 
	 * @return True if it is valid, false if not.
	 */
	public boolean isValid() {
		if (!this.verifySignature()) {
			return false;
		}
		// Collects all the unspent outputs
		List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
		this.inputs.forEach(x -> outputs.add(BlockChain.getInstance().getUTXO(x.getOutputId())));
		double value = outputs.stream().mapToDouble(x -> x.getValue()).sum();
		
		if (!this.isValid(value)) {
			return false;
		}
		return true;
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
		//TODO: AÑADIR ID?
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
		return new ArrayList<TransactionOutput>(outputs);
	}

	/**
	 * @return the inputs
	 */
	public List<TransactionInput> getInputs() {
		return new ArrayList<TransactionInput>(inputs);
	}
	
	/**
	 * Adds an input to the list of inputs.
	 * @param input
	 */
	public void addInput(TransactionInput input) {
		if (input!=null) {
			this.inputs.add(input);
		}
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
