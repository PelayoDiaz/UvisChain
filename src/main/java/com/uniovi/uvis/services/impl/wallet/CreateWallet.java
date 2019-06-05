package com.uniovi.uvis.services.impl.wallet;

import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompSession;

import com.uniovi.uvis.UvisServerApplication;
import com.uniovi.uvis.communication.Connection;
import com.uniovi.uvis.communication.handlers.SendWalletSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.Command;

public class CreateWallet implements Command<Wallet> {
	
	private String address;
	
	public CreateWallet(String address) {
		this.address = address;
	}

	@Override
	public Wallet execute() {
		Wallet wallet = new Wallet(address);
		BlockChain.getInstance().putWallet(wallet.getAddress(), wallet.toDto());
		
		for (Node node : BlockChain.getInstance().getNodes()) {
			if (!node.equals(UvisServerApplication.node)) {
				sendWallet(wallet, node.getUrl());
			}
		}
		return wallet;
	}
	
	/**
	 * It sends the created wallet to all the nodes for them to incorporate it to their chains.
	 * 
	 * @param wallet
	 * 			The wallet to be sent.
	 */
	private void sendWallet(Wallet wallet, String url) {
		try {
			StompSession session = Connection.initialize(url, new SendWalletSessionHandler());
			session.send("/app/chain/addWallet", wallet.toDto());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
