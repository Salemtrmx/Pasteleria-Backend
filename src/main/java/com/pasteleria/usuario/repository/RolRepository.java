package com.pasteleria.usuario.repository;

import com.pasteleria.usuario.model.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Integer> {
    Optional<RolEntity> findByNombreRol(String nombreRol);
}