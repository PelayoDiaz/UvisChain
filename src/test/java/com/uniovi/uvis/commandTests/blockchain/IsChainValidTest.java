package com.uniovi.uvis.commandTests.blockchain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.services.impl.blockchain.IsChainValid;
import com.uniovi.uvis.util.CryptoUtil;

public class IsChainValidTest extends AbstractTest {
	
	//---------------------------Block Tests---------------------------------------

	@Test
	/**
	 * Checks that the chain is detected as invalid because the proof of
	 * work of a block has been modified.
	 */
	public void chainInvalidBlockModifiedNonceTest() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		//We modify the content of a block so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithAlteredBlockNonce());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because the merkle root of
	 * a block has been modified.
	 */
	public void chainInvalidBlockModifiedMerkleRootTest() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		//We modify the content of a block so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithAlteredBlockMerkleRoot());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because the time stamp of
	 * a block has been modified.
	 */
	public void chainInvalidBlockModifiedTimeStampTest() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		//We modify the content of a block so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithAlteredBlockTimeStamp());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because the previous
	 * hash of a block has been modified
	 */
	public void chainInvalidBlockWithPreviousHashModifiedTest() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a block so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithAlteredBlockPreviousHash());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a block is not mined.
	 */
	public void chainInvalidBlockNotMinedTest() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a block so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithNotMinedBlock());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	//---------------------------Transaction Tests---------------------------------------
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction sender address
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionSenderAddress() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionSenderAddressAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction public key
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionSenderPK() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionPublicKeyAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction receiver address
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionReceiverAddress() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionReceiverAddressAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction amount
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionAmount() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionAmountAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction inputs
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionInputsValue() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionInputsAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction outputs owner
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionOutputsOwner() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionOutputsOwnerAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	@Test
	/**
	 * Checks that the chain is detected as invalid because a transaction leftOver owner
	 * has been modified.
	 */
	public void chainInvalidDataModifiedFromTransactionLeftOverOwner() {
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		BlockChain.getInstance().addBlock(getBlockDtoToModify(), null);
		assertTrue(executor.execute(new IsChainValid(BlockChain.getInstance())));
		
		//We modify the content of a transaction so the chain is no valid anymore.
		BlockChain.getInstance().update(getModifiedBlockChainWithTransactionLeftOverAltered());
		assertFalse(executor.execute(new IsChainValid(BlockChain.getInstance())));
	}
	
	
	//------------------------------Private Methods-----------------------------------
	
	/**
	 * It returns a modified BlockChainDto in which the nonce property of a block
	 * has been modified which changes the hash of the block and makes it invalid.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithAlteredBlockNonce() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//We modify the value of a the proof of work.
		chainDto.chain.get(1).nonce=-1;
		return chainDto;
	}
	
	/**
	 * It returns a modified BlockChainDto in which the merkle root property of a block
	 * has been modified which changes the hash of the block and makes it invalid.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithAlteredBlockMerkleRoot() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		chainDto.chain.get(1).merkleRoot="modificado";
		return chainDto;
	}
	
	/**
	 * It returns a modified BlockChainDto in which the merkle root property of a block
	 * has been modified which changes the hash of the block and makes it invalid.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithAlteredBlockTimeStamp() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		chainDto.chain.get(1).timeStamp=new Date().getTime();
		return chainDto;
	}
	
	/**
	 * It returns a modified BlockChainDto in which the value of 
	 * the hash of a previous block has been modified which changes 
	 * the hash of the block and makes it invalid.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithAlteredBlockPreviousHash() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//We modify the value of a the hash of the previous block in the chain
		chainDto.chain.get(1).previousHash="modificado";
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a not mined block which
	 * makes the chain invalid.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithNotMinedBlock() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The block now is not mined.
		chainDto.chain.get(1).mined = false;
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose sender Address has been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionSenderAddressAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The sender address of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).senderAddress = "Alterado"; 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose public key has been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionPublicKeyAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).sender = CryptoUtil.generateKeyPair().getPublic().getEncoded(); 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose public key has been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionReceiverAddressAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).receiver = "modificado"; 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose public key has been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionAmountAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).amount = 100; 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose inputs value have been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionInputsAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).inputs.get(0).utxo.value=-5; 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose outputs owner have been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionOutputsOwnerAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).outputs.get(0).receiver = "modificado"; 
		return chainDto;
	}
	
	/**
	 * It returns a a modified BlockChainDto with a processed transaction
	 * whose leftOver owner have been modified.
	 * 
	 * @return BlockChainDto
	 * 			The modified Dto.
	 */
	private BlockChainDto getModifiedBlockChainWithTransactionLeftOverAltered() {
		BlockChainDto chainDto = BlockChain.getInstance().toDto();
		//The public key of the transaction is now different.
		
		chainDto.chain.get(1).transactions.get(0).outputs.get(1).receiver = "modificado"; 
		return chainDto;
	}
	
	/**
	 * Returns a valid block to be included in the chain.
	 * @return
	 */
	private Block getBlockDtoToModify() {
		Block newBlock = new Block(BlockChain.getInstance().getLastBlock().getId());
		newBlock.addTransactions(this.createTransactionsList());
		newBlock.mine(3);
		newBlock.getTransactions().forEach(x -> x.processTransaction());
		return newBlock;
	}

}
