package com.uniovi.uvis.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.uvis.entities.block.Block;
import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.entities.transactions.Transaction;
import com.uniovi.uvis.services.BlockChainService;
import com.uniovi.uvis.services.BlockService;
import com.uniovi.uvis.services.impl.block.Miner;
import com.uniovi.uvis.validator.MineFormValidator;

@Controller
public class HomeController implements ErrorController {
	
	private Logger logger = LogManager.getLogger(HomeController.class);
	
	@Autowired
	private BlockChainService blockChainService;
	
	@Autowired
	private BlockService blockService;
	
	@Autowired
	private MineFormValidator mineFormValidator;
	
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		if (!Miner.isMining()) {
			model.addAttribute("walletDto", new WalletDto());
			return "homeNotMining";
		}
		return "homeMining";
	}
	
	@RequestMapping("/")
	public String getIndex(Model model) {
		return "redirect:home";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String mine(@Validated WalletDto walletDto, BindingResult result, Model model) {
		logger.info("Sending prize in case of success to: " + walletDto.address);
		if (Miner.isMining()) {
			return "redirect:home";
		} else {
			this.mineFormValidator.validate(walletDto, result);
			if (result.hasErrors()) {
				return "homeNotMining";
			}
			List<Transaction> originalTransactions = this.blockChainService.sendPrizeTo(walletDto.address);
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
	
	@RequestMapping("/error")
    public String handleError() {
        return "error";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
