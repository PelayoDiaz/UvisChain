package com.uniovi.uvis.entities.abst;

import com.uniovi.uvis.entities.dto.AbstractDto;

public interface Sendable<T extends AbstractDto> {
	
	/**
	 * Convert an entity into its DTO equivalent.
	 * 
	 * @return Dto
	 * 			The dto.
	 */
	public T toDto();

}
