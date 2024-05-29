package com.albert_martin.gestioBegudes.backend.presentation.restcontrollers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.albert_martin.gestioBegudes.backend.business.model.Beguda;
import com.albert_martin.gestioBegudes.backend.business.service.BegudaServices;
import com.albert_martin.gestioBegudes.backend.presentation.config.RespuestaError;

@RestController
public class BegudaController {

	@Autowired
	private BegudaServices begudaServices;
	
	@GetMapping("/beguda")
	public List<Beguda> getAll(){
		return begudaServices.getAll();
	}

	@GetMapping("/beguda/{id}")
	public ResponseEntity<?> read(@PathVariable Long id) {
		
		if(id > 500) {
			throw new RuntimeException("El número " + id + " no es válido.");
		}
		
		Optional<Beguda> optional = begudaServices.read(id);
		
		if (optional.isEmpty()) {
			RespuestaError respuestaError = new RespuestaError("No se encuentra el producto con id " + id);
			return new ResponseEntity<>(respuestaError, HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(optional.get());
	}
	
	// ****************************************************
	//
	// Gestión de excepciones
	//
	// ****************************************************
	
	@ExceptionHandler({IllegalArgumentException.class, ClassCastException.class})
	public ResponseEntity<?> gestor1(Exception e){
		return ResponseEntity.badRequest().body(new RespuestaError(e.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> gestor2(Exception e){
		return ResponseEntity.badRequest().body(new RespuestaError(e.getMessage()));
	}
	
}
