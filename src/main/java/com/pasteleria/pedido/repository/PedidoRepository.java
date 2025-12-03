package com.pasteleria.pedido.repository;

import com.pasteleria.pedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findAllByUsuario_IdUsuario(Integer id);
    List<Pedido> findAllByUsuario_IdUsuarioAndEstado_NombreEstado(Integer id, String nombreEstado);
    List<Pedido> findAllByEstado_NombreEstado(String nombreEstado);
    
    // Pedidos por fecha de entrega - usando @Query porque el campo tiene guion bajo
    @Query("SELECT p FROM Pedido p WHERE p.fecha_entrega = :fecha")
    List<Pedido> findAllByFechaEntrega(@Param("fecha") LocalDate fecha);
    
    // Pedidos por rango de fechas
    @Query("SELECT p FROM Pedido p WHERE p.fecha_entrega BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findAllByFechaEntregaBetween(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    // Total de ventas por fecha
    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Pedido p WHERE p.fecha_entrega = :fecha")
    Double sumTotalByFecha(@Param("fecha") LocalDate fecha);
    
    // Total de ventas por rango de fechas
    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Pedido p WHERE p.fecha_entrega BETWEEN :fechaInicio AND :fechaFin")
    Double sumTotalByFechaRange(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    // ============ VENTAS POR FECHA DE CREACIÓN (fecha de compra) ============
    
    // Pedidos por fecha de creación (día en que se realizó la compra)
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fecha_creacion AS LocalDate) = :fecha")
    List<Pedido> findAllByFechaCreacion(@Param("fecha") LocalDate fecha);
    
    // Pedidos por rango de fechas de creación
    @Query("SELECT p FROM Pedido p WHERE CAST(p.fecha_creacion AS LocalDate) BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findAllByFechaCreacionBetween(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    // Total de ventas por fecha de creación
    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Pedido p WHERE CAST(p.fecha_creacion AS LocalDate) = :fecha")
    Double sumTotalByFechaCreacion(@Param("fecha") LocalDate fecha);
    
    // Total de ventas por rango de fechas de creación
    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Pedido p WHERE CAST(p.fecha_creacion AS LocalDate) BETWEEN :fechaInicio AND :fechaFin")
    Double sumTotalByFechaCreacionRange(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}
