package com.pasteleria.inventario.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pasteleria.inventario.model.PastelInventario;

import java.util.List;
import java.util.Optional;

public interface PastelInventarioRepository extends JpaRepository<PastelInventario, Integer> {
    public List<PastelInventario> findAllByEstatus(boolean estatus);
    Optional<PastelInventario> findByIdPastelInventarioAndEstatus(Integer id, boolean estatus);
}
