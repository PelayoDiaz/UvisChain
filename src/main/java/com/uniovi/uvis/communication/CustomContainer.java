package com.uniovi.uvis.communication;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import com.uniovi.uvis.UvisChainApplication;

/**
 * Class to change the port where the program is running.
 * 
 * @author Pelayo Díaz Soto
 *
 */
@Component
public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.setPort(Integer.valueOf(UvisChainApplication.port));
	}

}
