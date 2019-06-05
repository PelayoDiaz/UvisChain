package com.uniovi.uvis.services.impl;

public class CommandExecutorIf extends CommandExecutor {
	
	public <T> T execute(boolean condition, Command<T> command) {
		if (condition) {
			return super.execute(command);
		}
		return null;
	}

}
