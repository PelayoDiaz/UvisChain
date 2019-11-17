package com.uniovi.uvis.entities.abst;

import com.uniovi.uvis.entities.dto.AbstractDto;

/**
 * Interface for those entities which must turn into DTOs.
 * 
 * @author Pelayo Díaz Soto
 *
 * @param <T> A DTO which extends from AbstractDto.
 */
public interface Sendable<T extends AbstractDto> {
	
	/**
	 * Convert an entity into its DTO equivalent.
	 * 
	 * @return Dto
	 * 			The dto.
	 */
	public T toDto();

}
