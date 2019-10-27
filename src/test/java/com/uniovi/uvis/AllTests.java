package com.uniovi.uvis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.uniovi.uvis.commandTests.block.CreateBlockTest;
import com.uniovi.uvis.commandTests.blockchain.AddTransactionTest;
import com.uniovi.uvis.commandTests.blockchain.AddWalletTest;
import com.uniovi.uvis.commandTests.blockchain.IsChainValidTest;
import com.uniovi.uvis.commandTests.transaction.CheckTransactionTest;
import com.uniovi.uvis.commandTests.transaction.GetValidTransactionTest;
import com.uniovi.uvis.commandTests.wallet.GetBalanceTest;
import com.uniovi.uvis.commandTests.wallet.SendFundsAndPrizeTest;
import com.uniovi.uvis.model.BlockTest;
import com.uniovi.uvis.model.TransactionTest;
import com.uniovi.uvis.services.WalletServiceTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	CreateBlockTest.class,
	AddTransactionTest.class,
	AddWalletTest.class,
	IsChainValidTest.class,
	CheckTransactionTest.class,
	GetValidTransactionTest.class,
	GetBalanceTest.class,
	SendFundsAndPrizeTest.class,
	BlockTest.class,
	TransactionTest.class,
	WalletServiceTest.class
})

public class AllTests {

}
