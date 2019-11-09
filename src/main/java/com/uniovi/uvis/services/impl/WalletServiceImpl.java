package com.uniovi.uvis.services.impl;

import org.springframework.stereotype.Service;

import com.uniovi.uvis.entities.block.BlockChain;
import com.uniovi.uvis.entities.dto.TransactionDto;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.entities.wallet.Wallet;
import com.uniovi.uvis.services.WalletService;
import com.uniovi.uvis.services.impl.command.CommandExecutorIf;
import com.uniovi.uvis.services.impl.transaction.CheckTransaction;
import com.uniovi.uvis.services.impl.wallet.CreateWallet;
import com.uniovi.uvis.services.impl.wallet.GetBalance;
import com.uniovi.uvis.services.impl.wallet.SendFunds;

@Service
public class WalletServiceImpl implements WalletService {

	/** Code executor for services. */
	private CommandExecutorIf executor;
	
	public WalletServiceImpl() {
		this.executor = new CommandExecutorIf();
	}
	
	@Override
	public Wallet createWallet(WalletDto dto) {
		Wallet createdWallet = executor.execute(new CreateWallet(dto));
		Wallet sender = BlockChain.getInstance().getCoinBase();
		executor.execute(executor.execute(new GetBalance(sender))>=BlockChain.PRIZE, new SendFunds(sender, createdWallet.getAddress(), BlockChain.PRIZE));
		return createdWallet;
	}

	@Override
	public Transaction sendFunds(TransactionDto dto) {
		if (!executor.execute(new CheckTransaction(dto))) {			
			return null;
		}
		Wallet senderWallet = new Wallet(BlockChain.getInstance().getWallets().get(dto.senderAddress));
		Transaction transaction = executor.execute(executor.execute(
				new GetBalance(senderWallet))>=dto.amount, 
				new SendFunds(senderWallet, dto.receiver, dto.amount));
		return transaction;
	}
	
	@Override
	public Wallet findByAddress(String address) {
		WalletDto dto = BlockChain.getInstance().getWallets().get(address);
		return (dto != null) ? new Wallet(dto) : null;
	}
}
