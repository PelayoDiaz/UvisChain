package com.uniovi.uvis.services.impl.command;

/**
 * Executor for commands. It executes the command only if the condition is true.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class CommandExecutorIf extends CommandExecutor {
	
	public <T> T execute(boolean condition, Command<T> command) {
		if (condition) {
			return super.execute(command);
		}
		return null;
	}

}
