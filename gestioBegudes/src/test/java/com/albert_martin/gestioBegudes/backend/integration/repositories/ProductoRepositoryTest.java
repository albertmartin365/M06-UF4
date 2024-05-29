package com.albert_martin.gestioBegudes.backend.integration.repositories;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.albert_martin.gestioBegudes.backend.business.model.dtos.Beguda1DTO;
import com.albert_martin.gestioBegudes.backend.integration.repositores.BegudaRepository;
import com.albert_martin.gestioBegudes.backend.business.model.Familia;
import com.albert_martin.gestioBegudes.backend.business.model.Beguda;

@DataJpaTest
@Sql(scripts= {"/data/h2/schema_test.sql","/data/h2/data_test.sql"})
public class ProductoRepositoryTest {

	@Autowired
	private BegudaRepository productoRepository;
	
	private Beguda producto1;
	private Beguda producto2;
	private Beguda producto3;
	private Beguda producto4;
	
	@BeforeEach
	void init() {
		initObjects();
	}
	
	@Test
	void obtenemos_productos_entre_rango_de_precios_en_orden_ascendente() {
		
		List<Beguda> begudes = productoRepository.findByPrecioBetweenOrderByPrecioAsc(20.0, 500.0);
		
		assertEquals(2, begudes.size());
			
		assertTrue(producto4.equals(begudes.get(0)));
		assertTrue(producto1.equals(begudes.get(1)));
		
	}
	
	@Test
	void obtenermos_productos_descatalogados_por_familia() {
		
		List<Beguda> begudes = productoRepository.findDescatalogadosByFamilia(Familia.senseGas);
		
		assertEquals(1, begudes.size());
		
		assertTrue(producto2.equals(begudes.get(0)));
	}
	
	@Test
	void obtenemos_todos_los_Producto1DTO() {
		
		List<Beguda1DTO> begudes1DTO = productoRepository.getAllBeguda1DTO();
		
		for(Beguda1DTO beguda1DTO: begudes1DTO) {
			System.err.println(beguda1DTO);
		}
	}
	
	// **************************************************************
	//
	// Private Methods
	//
	// **************************************************************
	
	private void initObjects() {
		
		producto1 = new Beguda();
		producto2 = new Beguda();
		producto3 = new Beguda();
		producto4 = new Beguda();
		
		producto1.setId(100L);
		producto2.setId(101L);
		producto3.setId(102L);
		producto4.setId(103L);
		
	}
	
}
