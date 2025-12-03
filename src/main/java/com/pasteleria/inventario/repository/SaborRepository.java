package com.pasteleria.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.inventario.model.Sabor;

public interface SaborRepository extends JpaRepository<Sabor, Integer> {

}
