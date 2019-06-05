package com.uniovi.uvis.services.impl.wallet;

import com.google.common.util.concurrent.AtomicDouble;
import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.impl.Command;


/**
 * Returns the total balance of the wallet and stores the UTXOs in it.
 * 
 * @author pdiaz
 *
 */
public class GetBalance implements Command<Double>{
	
	private Wallet wallet;
	
	public GetBalance(Wallet wallet) {
		this.wallet = wallet;
	}

	@Override
	public Double execute() {
		AtomicDouble total = new AtomicDouble(0);
		BlockChain.getInstance().getUTXOMap().forEach((k, v) -> {
			if (v.belongsTo(wallet.getAddress())) {
				wallet.putUTXO(v.getId(), v);
				total.addAndGet(v.getValue());
			}
		});
		return total.get();
	}

}
