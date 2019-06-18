package com.uniovi.uvis.entities.abst;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.UvisServerApplication;
import com.uniovi.uvis.communication.Sender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.AbstractDto;
import com.uniovi.uvis.entities.dto.Node;

public abstract class AbstractSender<T extends Sendable<E>, E extends AbstractDto> {
	
	public void send(T sendable, StompSessionHandlerAdapter handler, String listener) {
		List<Node> originalNodes = new ArrayList<Node>(BlockChain.getInstance().getNodes());
		for (Node node : originalNodes) {
			if (!node.equals(UvisServerApplication.node)) {
				doSend(sendable, node.getUrl(), handler, listener);
			}
		}
	}
	
	private void doSend(T sendable, String url, StompSessionHandlerAdapter handler, String listener) {	
		Sender sender = new Sender(sendable.toDto(), url, handler, listener);
		sender.start();
		
//		try {
//			StompSession session = Connection.initialize(url, handler);
//			session.send(listener, sendable.toDto());
//		} catch (IllegalStateException e) { //The node is not listening anymore, gets the next node and retries.
//			logger.info("Connection with actual node lost: Searching a new node to communicate with");
//			
//		}
	}
}
