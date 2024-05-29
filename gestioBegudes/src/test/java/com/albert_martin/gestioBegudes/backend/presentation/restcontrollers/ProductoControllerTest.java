package com.albert_martin.gestioBegudes.backend.presentation.restcontrollers;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.albert_martin.gestioBegudes.backend.business.model.Familia;
import com.albert_martin.gestioBegudes.backend.business.model.Beguda;
import com.albert_martin.gestioBegudes.backend.business.service.BegudaServices;

import com.albert_martin.gestioBegudes.backend.presentation.config.RespuestaError;

@WebMvcTest(controllers=BegudaController.class)
public class ProductoControllerTest {
	
	@Autowired
	private MockMvc miniPostman;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private BegudaServices begudaServices;
	
	private Beguda producto1;
	private Beguda producto2;
	
	@BeforeEach
	void init() {
		initObjects();
	}
	
	@Test
	void pedimos_todos_los_productos() throws Exception {
		
		// Arrange
		
		List<Beguda> productos = Arrays.asList(producto1, producto2);
		when(begudaServices.getAll()).thenReturn(productos);
		
		// Act
		
		MvcResult respuesta = miniPostman.perform(get("/begudes").contentType("application/json"))
											.andExpect(status().isOk())
											.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(productos);
		
		// Assert
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	@Test
	void pedimos_todos_los_productos_entre_rango_precios() throws Exception {
				
		List<Beguda> productos = Arrays.asList(producto1, producto2);
		when(begudaServices.getBetweenPriceRange(10, 500)).thenReturn(productos);
			
		MvcResult respuesta = miniPostman.perform(get("/productos").param("min", "10")
																   .param("max","500")
																   .contentType("application/json"))
											.andExpect(status().isOk())
											.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(productos);
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	@Test
	void obtenemos_producto_a_partir_de_su_id() throws Exception{
		
		when(begudaServices.read(100L)).thenReturn(Optional.of(producto1));
		
		MvcResult respuesta = miniPostman.perform(get("/productos/100").contentType("application/json"))
									.andExpect(status().isOk())
									.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(producto1);
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	
	}
	
	@Test
	void solicitamos_producto_a_partir_de_un_id_inexistente() throws Exception {
		
		when(begudaServices.read(100L)).thenReturn(Optional.empty());
		
		MvcResult respuesta = miniPostman.perform(get("/productos/100").contentType("application/json"))
									.andExpect(status().isNotFound())
									.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("No se encuentra el producto con id 100"));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	}
	
	@Test
	void crea_producto_ok() throws Exception {
		
		producto1.setId(null);
		
		when(begudaServices.create(producto1)).thenReturn(1033L);
		
		String requestBody = objectMapper.writeValueAsString(producto1);
		
		miniPostman.perform(post("/productos").content(requestBody).contentType("application/json"))
						.andExpect(status().isCreated())
						.andExpect(header().string("Location","http://localhost/productos/1033"));
	}
	
	@Test
	void crear_producto_con_id_NO_NULL() throws Exception{
		
		when(begudaServices.create(producto1)).thenThrow(new IllegalStateException("Problema con el id..."));
		
		String requestBody = objectMapper.writeValueAsString(producto1);
		
		MvcResult respuesta = miniPostman.perform(post("/productos").content(requestBody).contentType("application/json"))
						.andExpect(status().isBadRequest())
						.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("Problema con el id..."));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	}
	
	@Test
	void eliminamos_producto_ok() throws Exception{
		
		miniPostman.perform(delete("/productos/789")).andExpect(status().isNoContent());
		
		verify(begudaServices, times(1)).delete(789L);
	}
	
	@Test
	void eliminamos_producto_no_existente() throws Exception{
		
		Mockito.doThrow(new IllegalStateException("xxxx")).when(begudaServices).delete(789L);
		
		MvcResult respuesta = miniPostman.perform(delete("/productos/789"))
								.andExpect(status().isNotFound())
								.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("No se encuentra el producto con id [789]. No se ha podido eliminar."));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	// **************************************************************
	//
	// Private Methods
	//
	// **************************************************************
	
	private void initObjects() {
		
		producto1 = new Beguda();
		producto1.setId(10L);
		producto1.setNombre("aigua");
		producto1.setPrecio(0.60);
		producto1.setDescripcion("aigua mineral");
		producto1.setDescatalogado(true);
		producto1.setFamilia(Familia.senseGas);
		producto1.setFechaAlta(new Date(14082025));
		
		producto2 = new Beguda();
		producto2.setId(11L);
		producto2.setNombre("Monster");
		producto2.setPrecio(1.80);
		producto2.setDescripcion("beguda energètica");
		producto2.setDescatalogado(false);
		producto2.setFamilia(Familia.ambGas);
		producto2.setFechaAlta(new Date(23062025));
		
	}
	
}
