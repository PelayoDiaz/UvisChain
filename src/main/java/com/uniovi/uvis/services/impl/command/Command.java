package com.uniovi.uvis.services.impl.command;

/**
 * Interface that every command must implements.
 * 
 * @author Pelayo Díaz Soto
 *
 * @param <T>
 * 			The type of the element to be returned with the execute method.
 */
public interface Command<T> {

	/**
	 * Method which contains the code to be executed from every command.
	 * 
	 * @return T
	 */
	T execute();
}
