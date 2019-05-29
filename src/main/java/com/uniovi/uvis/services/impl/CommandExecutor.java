package com.uniovi.uvis.services.impl;

public class CommandExecutor {

	public <T> T execute(Command<T> command) {
		return command.execute();
	}
}
