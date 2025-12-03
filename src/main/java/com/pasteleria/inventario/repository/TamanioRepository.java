package com.pasteleria.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.inventario.model.Tamanio;

public interface TamanioRepository extends JpaRepository<Tamanio, Integer> {

}
