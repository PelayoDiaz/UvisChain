package com.uniovi.uvis;

import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.stomp.StompSession;

import com.uniovi.uvis.communication.Connection;
import com.uniovi.uvis.communication.handlers.RegisterNodeSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.Node;


@SpringBootApplication
public class UvisServerApplication {
	
	public static String port;

	public static void main(String[] args) {
		if (args.length>=2) {
			port = args[0];
			String url = "ws://localhost:"+port+"/uvischain";
			String rootPort = args[1];
			if (port.equals(rootPort)) { //If both of the ports are equal, it is the genesis node.
				BlockChain.getInstance().registerNode(new Node(url));
			} else { //If not, then connect with the second port and initialize the node.
				String rootUrl = "ws://localhost:"+rootPort+"/uvischain";
				initialize(rootUrl, new Node(url));
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
		try {
			StompSession session = Connection.initialize(url, new RegisterNodeSessionHandler());
			session.send("/app/chain/registerNode", node);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
