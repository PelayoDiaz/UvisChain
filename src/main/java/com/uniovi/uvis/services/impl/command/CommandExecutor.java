package com.uniovi.uvis.services.impl.command;

/**
 * Executor for commands.
 * 
 * @author Pelayo Díaz Soto
 *
 */
public class CommandExecutor {

	public <T> T execute(Command<T> command) {
		return command.execute();
	}
}
