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

public class SendWalletSessionHandler extends StompSessionHandlerAdapter {

	private Logger logger = LogManager.getLogger(SendWalletSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/wallet", this);
        logger.info("Subscribed to /topic/wallet");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Ups. This is embarrasing. Something went wrong while sending the wallet!", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return BlockChainDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	
    	BlockChainDto chain = (BlockChainDto) payload;
    	if (BlockChain.getInstance().getWallets().size() == chain.wallets.size()) {
    		logger.info("Wallet sent succesfully! Keep going!");
    	} else {
    		logger.error("Seems like the receiver doesn't have the same number of wallets.");
    	}
    	
    }

}
