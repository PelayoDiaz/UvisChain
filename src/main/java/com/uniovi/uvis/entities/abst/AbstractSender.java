package com.uniovi.uvis.entities.abst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.UvisServerApplication;
import com.uniovi.uvis.communication.Sender;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.AbstractDto;
import com.uniovi.uvis.entities.dto.Node;

public abstract class AbstractSender<T extends Sendable<E>, E extends AbstractDto> {
	
	private Logger logger = LogManager.getLogger(AbstractSender.class);
	
	public void send(T sendable, StompSessionHandlerAdapter handler, String listener) {
		List<Node> originalNodes = new ArrayList<Node>(BlockChain.getInstance().getNodes());
		for (Node node : originalNodes) {
			if (!node.equals(UvisServerApplication.node)) {
				doSend(sendable, node.getUrl(), handler, listener);
			}
		}
	}
	
	private void doSend(T sendable, String url, StompSessionHandlerAdapter handler, String listener) {	
		Sender sender;
		try {
			sender = new Sender(sendable.toDto(), url, handler, listener);
			sender.start();
		} catch (ExecutionException e) {
			logger.error(String.format("Something went wrong while trying to communicate with node %s", url));
		}
	}
}
