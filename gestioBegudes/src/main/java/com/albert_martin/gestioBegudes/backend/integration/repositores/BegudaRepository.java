package com.albert_martin.gestioBegudes.backend.integration.repositores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.albert_martin.gestioBegudes.backend.business.model.Familia;
import com.albert_martin.gestioBegudes.backend.business.model.Beguda;
import com.albert_martin.gestioBegudes.backend.business.model.dtos.Beguda1DTO;

public interface BegudaRepository extends JpaRepository<Beguda, Long>{

	List<Beguda> findByPrecioBetweenOrderByPrecioAsc(double min, double max);
	
	@Query("SELECT p FROM Producto p WHERE p.descatalogado = true AND p.familia = :familia")
	List<Beguda> findDescatalogadosByFamilia(Familia familia);
	
	@Query("SELECT new com.rauxasoft.gestionproductos.backend.business.model.dtos.Producto1DTO"
		   + "(CONCAT(p.nombre, ' - ', "
		   + "        p.familia), "
		   + "        p.precio) "
		   + "FROM Producto p")
	List<Beguda1DTO> getAllBeguda1DTO();
	
	
}
