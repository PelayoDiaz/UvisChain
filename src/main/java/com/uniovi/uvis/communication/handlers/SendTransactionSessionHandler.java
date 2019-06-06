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

public class SendTransactionSessionHandler extends StompSessionHandlerAdapter {

	private Logger logger = LogManager.getLogger(SendTransactionSessionHandler.class);
	
	private StompSession session;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/transaction", this);
        logger.info("Subscribed to /topic/transaction");
        this.session = session;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Ups. This is embarrasing. Something went wrong while sending the Transaction!", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return BlockChainDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	
    	BlockChainDto chain = (BlockChainDto) payload;
    	if (BlockChain.getInstance().getTransactions().size() == chain.transactions.size()) {
    		logger.info("Transaction sent succesfully! It's time to recover that money kid!");
    	} else {
    		logger.error("Seems like the receiver doesn't have the same number of transactions.");
    	}
    	this.session.disconnect();
    	
    }

}