package com.albert_martin.gestioBegudes.backend.business.service;

import java.util.List;
import java.util.Optional;

import com.albert_martin.gestioBegudes.backend.business.model.Beguda;

public interface BegudaServices {

	/**
	 * Devuelve el código que ha otorgado el sistema
	 * 
	 * Lanza una IllegalStateException si el código del producto no es null
	 * 
	 */
	Long create(Beguda beguda);	    // C
	
	Optional<Beguda> read(Long id);   // R
	
	/**
	 * 
	 * Lanza una IllegalStateException si el código del producto es null o no existe en el sistema
	 * 
	 */
	void update(Beguda beguda);		// U
	
	/**
	 * Lanza una IllegalStateException si no existe la id en el sistema
	 * 
	 */
	void delete(Long id);				// D
	
	List<Beguda> getAll();
	List<Beguda> getBetweenPriceRange(double min, double max);
	
}