package com.uniovi.uvis.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class Connection {

	public static StompSession initialize(String url, StompSessionHandler sessionHandler) { 
		
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
	    container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024);
	    container.setDefaultMaxTextMessageBufferSize(1024 * 1024);
		WebSocketClient simpleWebSocketClient = new StandardWebSocketClient(container);
		
		List<Transport> transports = new ArrayList<Transport>(1);
		transports.add(new WebSocketTransport(simpleWebSocketClient));

		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		try {
			return stompClient.connect(url, sessionHandler).get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}


}
