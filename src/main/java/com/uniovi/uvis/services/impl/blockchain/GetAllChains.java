package com.uniovi.uvis.services.impl.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompSession;

import com.uniovi.uvis.communication.Connection;
import com.uniovi.uvis.communication.handlers.MyStompSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.services.impl.command.Command;

public class GetAllChains implements Command<List<BlockChain>> {
	
	public static List<BlockChainDto> chainsList = Collections.synchronizedList(new ArrayList<BlockChainDto>());
	

	@Override
	public List<BlockChain> execute() {
		for (Node node : BlockChain.getInstance().getNodes()) {
			getChain(node.getUrl());
		}
		while (true) {
			if (chainsList.size()==BlockChain.getInstance().getNodes().size()) {
				break;
			}
		}
		
		return null;
	}
	
	private void getChain(String url) {
		try {
			StompSession session = Connection.initialize(url, new MyStompSessionHandler());
			session.send("/app/chain.sendTransaction", null);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
