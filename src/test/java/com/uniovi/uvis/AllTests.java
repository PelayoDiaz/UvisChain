package com.uniovi.uvis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	BlockChainTest.class,
	BlockTest.class,
	TransactionTest.class,
	WalletTest.class
})

public class AllTests {

}
