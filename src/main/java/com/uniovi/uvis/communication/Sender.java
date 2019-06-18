package com.uniovi.uvis.communication;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.AbstractDto;

public class Sender extends Thread {
	
	private Logger logger = LogManager.getLogger(Sender.class);
		
	private AbstractDto dto;
	private String listener;

	private String url;
	
	public static Map<String, StompSession> sessions = new HashMap<String, StompSession>();

	/**
	 * Class to communicate with the others nodes where the chain is stored.
	 * 
	 * @param dto
	 * 			The information in a dto to send
	 * @param url
	 * 			The url of the node to connect
	 * @param handler
	 * 			The handler of the sessions established
	 * @param listener
	 * 			The listener where the node is listening
	 */
	public Sender(AbstractDto dto, String url, StompSessionHandlerAdapter handler, String listener) {
		this.dto = dto;
		this.listener = listener;
		this.url = url;
		initSession(url, handler);
	}
	
	private void initSession(String url, StompSessionHandlerAdapter handler) {
		if (!sessions.containsKey(url)) {
			sessions.put(url, Connection.initialize(url, handler));
		}
	}
	
	/**
	 * Communicates with the blockchain's nodes by sending a dto.
	 */
	public void run() {
		try {
			StompSession session = sessions.get(this.url); 
			synchronized (session) {
				session.send(listener, dto);
			}
		} catch (IllegalStateException e) { //The node is not listening anymore, removes it from the list of nodes.
			logger.info(String.format("Connection with node with url '%s' lost. Deleting from list of nodes. ", this.url));
			sessions.remove(this.url);
			BlockChain.getInstance().removeNode(this.url);
		}
	}
	

}
