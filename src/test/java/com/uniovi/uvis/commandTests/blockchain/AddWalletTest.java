package com.uniovi.uvis.commandTests.blockchain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uniovi.uvis.AbstractTest;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.blockchain.AddWallet;

public class AddWalletTest extends AbstractTest {
	
	@Test
	/**
	 * Checks that a transaction can be added to the blockchain.
	 */
	public void addWalletCorrectTest() {
		assertEquals(1, BlockChain.getInstance().getWallets().size());
		executor.execute(new AddWallet(this.createWalletDto()));
		assertEquals(2, BlockChain.getInstance().getWallets().size());
	}
	
	@Test
	/**
	 * Checks that a wallet is not added if the method receives a null value
	 */
	public void addNullWalletTest() {
		assertEquals(1, BlockChain.getInstance().getWallets().size());
		executor.execute(new AddWallet(null));
		assertEquals(1, BlockChain.getInstance().getWallets().size());
	}
	
	/**
	 * Returns a WalletDto to make tests.
	 * 
	 * @return WalletDto
	 * 			The walletDto
	 */
	private WalletDto createWalletDto() {
		Wallet wallet = new Wallet("walletA", "direccion1", "userA");
		return wallet.toDto();
	}

}
