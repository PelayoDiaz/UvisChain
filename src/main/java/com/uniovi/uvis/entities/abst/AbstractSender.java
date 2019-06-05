package com.uniovi.uvis.entities.abst;

import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.UvisServerApplication;
import com.uniovi.uvis.communication.Connection;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.Node;

public abstract class AbstractSender<T extends Sendable<E>, E> {

	public void send(T sendable, StompSessionHandlerAdapter handler, String listener) {
		for (Node node : BlockChain.getInstance().getNodes()) {
			if (!node.equals(UvisServerApplication.node)) {
				doSend(sendable, node.getUrl(), handler, listener);
			}
		}
	}
	
	private void doSend(T sendable, String url, StompSessionHandlerAdapter handler, String listener) {	
		try {
			StompSession session = Connection.initialize(url, handler);
			session.send(listener, sendable.toDto());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
