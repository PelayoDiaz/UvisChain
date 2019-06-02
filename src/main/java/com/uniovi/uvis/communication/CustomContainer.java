package com.uniovi.uvis.communication;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import com.uniovi.uvis.UvisServerApplication;

@Component
public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.setPort(Integer.valueOf(UvisServerApplication.port));
	}

}
