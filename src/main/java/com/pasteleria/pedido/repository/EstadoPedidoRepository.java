package com.pasteleria.pedido.repository;

import com.pasteleria.pedido.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {
    Optional<EstadoPedido> findByNombreEstado (String nombreEstado);
}
