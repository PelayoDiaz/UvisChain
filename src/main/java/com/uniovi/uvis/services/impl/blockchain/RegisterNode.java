package com.uniovi.uvis.services.impl.blockchain;

import com.uniovi.uvis.communication.handlers.BlockChainSessionHandler;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.BlockChainDto;
import com.uniovi.uvis.entities.dto.Node;
import com.uniovi.uvis.services.impl.abst.AbstractSender;
import com.uniovi.uvis.services.impl.command.Command;

public class RegisterNode extends AbstractSender<Node, Node> implements Command<BlockChainDto>{
	
	/** Url where the other nodes are listening. */
	public static final String LISTENER = "/app/chain/addNode";
	
	private Node node;
	private boolean hasToSend;

	/**
	 * Includes a new node to the chain.
	 * 
	 * @param node
	 * 			The node to be included	 
	 * @param send
	 * 			If it is necessary to send the node to the rest into
	 * 			the net.
	 */
	public RegisterNode(Node node, boolean hasToSend) {
		this.node = node;
		this.hasToSend = hasToSend;
	}

	@Override
	public BlockChainDto execute() {
		if (node!=null) {
			if (this.hasToSend) {
				this.send(this.node, new BlockChainSessionHandler(), LISTENER);
			}
			BlockChain.getInstance().registerNode(node);
		}
		return BlockChain.getInstance().toDto();
	}

}
