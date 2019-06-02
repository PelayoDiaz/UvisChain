package com.uniovi.uvis.communication.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.services.impl.blockchain.GetAllChains;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

	private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/blockchain", this);
        logger.info("Subscribed to /topic/blockchain");        
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return BlockChainDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	
    	BlockChainDto chain = (BlockChainDto) payload;
    	GetAllChains.chainsList.add(chain);
//        logger.info("Received : " + msg.getContent() + " from : ");
    }

}
