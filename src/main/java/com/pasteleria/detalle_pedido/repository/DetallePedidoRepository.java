package com.pasteleria.detalle_pedido.repository;

import com.pasteleria.detalle_pedido.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedido_Id(Integer id);
}

