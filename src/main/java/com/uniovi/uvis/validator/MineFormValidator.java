package com.uniovi.uvis.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.uniovi.uvis.entities.dto.WalletDto;
import com.uniovi.uvis.services.WalletService;

@Component
public class MineFormValidator implements Validator {

	@Autowired
	private WalletService walletService;

	@Override
	public boolean supports(Class<?> aClass) {
		return WalletDto.class.equals(aClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		WalletDto wallet = (WalletDto) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.empty");
		
		if (this.walletService.findByAddress(wallet.address)==null) {
			errors.rejectValue("address", "error.address");
		}
	}

}
