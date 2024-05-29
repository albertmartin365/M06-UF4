package com.albert_martin.gestioBegudes.backend.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albert_martin.gestioBegudes.backend.business.model.Beguda;
import com.albert_martin.gestioBegudes.backend.business.model.Familia;
import com.albert_martin.gestioBegudes.backend.business.service.BegudaServices;
import com.albert_martin.gestioBegudes.backend.integration.repositores.BegudaRepository;


@Service
public class BegudaServicesImpl implements BegudaServices {

	@Autowired
	private BegudaRepository begudaRepository;
	
	@Override
	@Transactional
	public Long create(Beguda beguda) {
		
		if(beguda.getId() != null) {
			throw new IllegalStateException("No se puede crear un producto con código not null");
		}
		
		Long id = System.currentTimeMillis();
		beguda.setId(id);
		
		begudaRepository.save(beguda);
		
		return id;
	}

	@Override
	public Optional<Beguda> read(Long id) {	
		return begudaRepository.findById(id);
	}

	@Override
	@Transactional
	public void update(Beguda beguda) {
		
		Long id = beguda.getId();
		
		if(id == null) {
			throw new IllegalStateException("No se puede actualizar un producto con código not null");
		}
		
		boolean existe = begudaRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException("El producto con código " + id + " no existe. No se puede actualizar.");
		}
		
		begudaRepository.save(beguda);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		begudaRepository.deleteById(id);
	}

	@Override
	public List<Beguda> getAll() {
		return begudaRepository.findAll();
	}

	@Override
	public List<Beguda> getBetweenPriceRange(double min, double max) {
		return begudaRepository.findByPrecioBetweenOrderByPrecioAsc(min, max);
	}

}
