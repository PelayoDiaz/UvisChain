package com.uniovi.uvis.communication.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;

import java.lang.reflect.Type;

public class UpdateChainSessionHandler extends StompSessionHandlerAdapter {

	private Logger logger = LogManager.getLogger(UpdateChainSessionHandler.class);
	
	private StompSession session;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/blockchain", this);
        logger.info("Subscribed to /topic/blockchain");
        this.session = session;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Ups. This is embarrasing. Something went wrong while sending the chain!", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return BlockChainDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	
    	BlockChainDto chainDto = (BlockChainDto) payload;
    	if (chainDto.chain!= null && 
    			BlockChain.getInstance().getChain().size() == chainDto.chain.size()) {
    		logger.info("Chain sent succesfully! We'll live another day!");
    	} else {
    		logger.error("Well... How to say this...? It seems that there was a problem while sending the chain.");
    	}
    	this.session.disconnect();
    	
    }

}
