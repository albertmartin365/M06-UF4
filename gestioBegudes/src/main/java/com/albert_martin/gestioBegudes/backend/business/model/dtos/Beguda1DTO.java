package com.albert_martin.gestioBegudes.backend.business.model.dtos;

import java.io.Serializable;

public class Beguda1DTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double precio;
	
	public Beguda1DTO(String name, double precio) {
		this.name = name;
		this.precio = precio;
	}

	public String getName() {
		return name;
	}

	public double getPrecio() {
		return precio;
	}

	@Override
	public String toString() {
		return "Beguda1DTO [name=" + name + ", precio=" + precio + "]";
	}
	

}
