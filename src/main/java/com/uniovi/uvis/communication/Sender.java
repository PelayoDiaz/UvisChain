package com.uniovi.uvis.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.AbstractDto;

/**
 * Class to communicate with the others nodes where the chain is stored.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class Sender extends Thread {
	
	/** Logger. */
	private Logger logger = LogManager.getLogger(Sender.class);
	
	/** The dto to be sent in the communication. */
	private AbstractDto dto;
	
	/** The listener where the dto will be received. */
	private String listener;

	/** The url of the node to communicate with. */
	private String url;
	
	/** HashMap with all the opened sessions. */
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
	 * @throws ExecutionException 
	 */
	public Sender(AbstractDto dto, String url, StompSessionHandlerAdapter handler, String listener) throws ExecutionException {
		this.dto = dto;
		this.listener = listener;
		this.url = url;
		initSession(url, handler);
	}
	
	/**
	 * Initializes a connection with a node of the list contained into the chain.
	 * 
	 * @param url
	 * 			The url of the node.
	 * @param handler
	 * 			The handler.
	 * @throws ExecutionException
	 */
	private void initSession(String url, StompSessionHandlerAdapter handler) throws ExecutionException {
		if (!sessions.containsKey(url)) {
			try {
				StompSession session = Connection.initialize(url, handler); 
				if (session!=null) {
					sessions.put(url, session);
				}
			} catch (ExecutionException  e) {
				logger.info(String.format("The node with url '%s' was desconected. Not stablishing connection ", this.url));
				BlockChain.getInstance().removeNode(this.url);
			} catch (Exception  e) {
				logger.info(String.format("por aqui The node with url '%s' was desconected. Not stablishing connection ", this.url));
				BlockChain.getInstance().removeNode(this.url);
			}
		}
	}
	
	/**
	 * Communicates with the blockchain's nodes by sending a dto.
	 */
	public void run() {
		try {
			StompSession session = sessions.get(this.url);
			if (session!=null) {
				session.send(listener, dto);
			}
		} catch (IllegalStateException e) { //The node is not listening anymore, removes it from the list of nodes.
			logger.info(String.format("Connection with node with url '%s' lost. Deleting from list of nodes. ", this.url));
			sessions.remove(this.url);
			BlockChain.getInstance().removeNode(this.url);
		}
	}
}
