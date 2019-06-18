package com.uniovi.uvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uniovi.uvis.communication.Sender;
import com.uniovi.uvis.communication.handlers.RegisterNodeSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.Node;

@SpringBootApplication
public class UvisServerApplication {
	
	public static String port;
	public static Node node;

	public static void main(String[] args) {
		if (args.length>=2) {
			port = args[0];
			String url = "ws://localhost:"+port+"/uvischain";
			String rootPort = args[1];
			node = new Node(url);
			if (port.equals(rootPort)) { //If both ports are equal, it is the genesis node.
				BlockChain.getInstance().registerNode(node);
			} else { //If not, then connect with the second port and initialize the node.
				String rootUrl = "ws://localhost:"+rootPort+"/uvischain";
				initialize(rootUrl, node);
			}
			SpringApplication.run(UvisServerApplication.class, args);
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
	private static void initialize(String url, Node node) {
		Sender sender = new Sender(node, url, new RegisterNodeSessionHandler(), "/app/chain/registerNode");
		sender.start();
//			StompSession session = Connection.initialize(url, new RegisterNodeSessionHandler());
//			session.send("/app/chain/registerNode", node);
	}

}
