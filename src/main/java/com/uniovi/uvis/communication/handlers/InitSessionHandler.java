package com.uniovi.uvis.communication.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompHeaders;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;

/**
 * Handler to manage the communication when a new node is stored in the chain.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class InitSessionHandler extends BlockChainSessionHandler {

	private Logger logger = LogManager.getLogger(InitSessionHandler.class);

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	BlockChainDto chain = (BlockChainDto) payload;
    	logger.info("Updating chain...");
    	BlockChain.getInstance().update(chain);
    	logger.info("Communication completed successfully. Keep going!");
    }

}
