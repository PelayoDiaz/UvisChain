package com.uniovi.uvis;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uniovi.uvis.communication.Sender;
import com.uniovi.uvis.communication.handlers.BlockChainSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.Node;

@SpringBootApplication
public class UvisChainApplication {
	
	private static Logger logger = LogManager.getLogger(UvisChainApplication.class);
	
	public static String port;
	public static Node node;

	public static void main(String[] args) {
		if (args.length>=2) {
			boolean start = true;
			port = args[0];
			String url = "ws://localhost:"+port+"/uvischain";
			String rootPort = args[1];
			node = new Node(url);
			if (port.equals(rootPort)) { //If both ports are equal, it is the genesis node.
				BlockChain.getInstance().registerNode(node);
			} else { //If not, then connect with the second port and initialize the node.
				String rootUrl = "ws://localhost:"+rootPort+"/uvischain";
				start = initialize(rootUrl, node);
			}
			if (start) {
				SpringApplication.run(UvisChainApplication.class, args);
			}
		}		
	}
	
	/**
	 * It connects with a node to register the actual node and to obtain the blockchain.
	 * 
	 * @param url
	 * 			The url of the node to connect with.
	 * @param node
	 * 			The node to be registered.
	 */
	private static boolean initialize(String url, Node node) {
		Sender sender;
		try {
			sender = new Sender(node, url, new BlockChainSessionHandler(), "/app/chain/registerNode");
			sender.start();
			return true;
		} catch (ExecutionException e) {
			logger.error("Can not connect with the indicated node. Please try it again with another address.");
			return false;
		}
	}

}
