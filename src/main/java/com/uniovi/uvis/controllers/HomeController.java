package com.uniovi.uvis.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.impl.BlockChainServiceImpl;
import com.uniovi.uvis.services.impl.BlockServiceImpl;
import com.uniovi.uvis.services.impl.block.Miner;
import com.uniovi.uvis.validator.MineFormValidator;

@Controller
public class HomeController {
	
	private Logger logger = LogManager.getLogger(HomeController.class);
	
	@Autowired
	private BlockChainServiceImpl blockChainService;
	
	@Autowired
	private BlockServiceImpl blockService;
	
	@Autowired
	private MineFormValidator mineFormValidator;
	
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		if (!Miner.isMining()) {
			model.addAttribute("wallet", new WalletDto());
			return "homeNotMining";
		}
		return "homeMining";
	}
	
	@RequestMapping("/")
	public String getIndex(Model model) {
		return "redirect:home";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String mine(@Validated WalletDto wallet, BindingResult result, Model model) {
		logger.info("Sending prize in case of success to: " + wallet.address);
		if (Miner.isMining()) {
			return "redirect:home";
		} else {
			this.mineFormValidator.validate(wallet, result);
			if (result.hasErrors()) {
				model.addAttribute("wallet", wallet);
				return "homeNotMining";
			}
			List<Transaction> originalTransactions = this.blockChainService.sendPrizeTo(wallet.address);
			Block newBlock = this.blockService.createBlock();
			this.blockService.mine(newBlock, originalTransactions);
			return "redirect:home";
		}
	}
	
	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public String mine(Model model) {
		Miner.keepMining(false);
		return "redirect:home";
	}
	
}
