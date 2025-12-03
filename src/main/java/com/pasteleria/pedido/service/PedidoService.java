package com.pasteleria.pedido.service;

import com.pasteleria.pedido.model.EstadoPedido;
import com.pasteleria.pedido.model.Pedido;
import com.pasteleria.pedido.repository.EstadoPedidoRepository;
import com.pasteleria.pedido.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final EstadoPedidoRepository estadoRepository;
    public PedidoService(PedidoRepository pedidoRepository, EstadoPedidoRepository estadoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.estadoRepository = estadoRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    //get pedidos entregados
    public List<Pedido> findAllByClienteId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException( "Id invalido");
        }
        return pedidoRepository.findAllByUsuario_IdUsuario(id);
    }

    public List<Pedido> findAllByEstadoNombre(String nombre_estado) {
        // validar que no venga nulo o vacío
        if (nombre_estado == null || nombre_estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado es obligatorio.");
        }
        String estadoNormalizado = nombre_estado.trim().toUpperCase();
        // validar que sea "ENTREGADO", "PREPARACION", "PENDIENTE", "RECHAZADO"
        if (!("PENDIENTE".equals(estadoNormalizado) ||
                "PREPARACION".equals(estadoNormalizado) ||
                "ENTREGADO".equals(estadoNormalizado) ||
                "RECHAZADO".equals(estadoNormalizado))) {
            throw new IllegalArgumentException("Estado no válido");
        }
        return pedidoRepository.findAllByEstado_NombreEstado(nombre_estado);
    }
    public List<Pedido> findAllByClienteIdAndEstadoNombre(Integer id, String nombre_estado) {
        if (nombre_estado == null || id == null) {
            throw new IllegalArgumentException("El nombre del estado o id es obligatorio.");
        }
        String estadoNormalizado = nombre_estado.trim().toUpperCase();
        if (!("PENDIENTE".equals(estadoNormalizado) ||
                "PREPARACION".equals(estadoNormalizado) ||
                "ENTREGADO".equals(estadoNormalizado) ||
                "RECHAZADO".equals(estadoNormalizado))) {
            throw new IllegalArgumentException("Estado no válido");
        }
        return pedidoRepository.findAllByUsuario_IdUsuarioAndEstado_NombreEstado(id, nombre_estado);
    }
    
    // Obtener pedidos por fecha de entrega
    public List<Pedido> findAllByFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        return pedidoRepository.findAllByFechaEntrega(fecha);
    }
    
    // Obtener pedidos por rango de fechas
    public List<Pedido> findAllByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return pedidoRepository.findAllByFechaEntregaBetween(fechaInicio, fechaFin);
    }
    
    // Obtener total de ventas por fecha
    public Double getTotalVentasByFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        return pedidoRepository.sumTotalByFecha(fecha);
    }
    
    // Obtener total de ventas por rango de fechas
    public Double getTotalVentasByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        return pedidoRepository.sumTotalByFechaRange(fechaInicio, fechaFin);
    }
    
    // ============ VENTAS POR FECHA DE CREACIÓN (fecha de compra) ============
    
    // Obtener pedidos por fecha de creación (cuando se compró)
    public List<Pedido> findAllByFechaCreacion(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        return pedidoRepository.findAllByFechaCreacion(fecha);
    }
    
    // Obtener pedidos por rango de fechas de creación
    public List<Pedido> findAllByFechaCreacionRange(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return pedidoRepository.findAllByFechaCreacionBetween(fechaInicio, fechaFin);
    }
    
    // Obtener total de ventas por fecha de creación
    public Double getTotalVentasByFechaCreacion(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        return pedidoRepository.sumTotalByFechaCreacion(fecha);
    }
    
    // Obtener total de ventas por rango de fechas de creación
    public Double getTotalVentasByFechaCreacionRange(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        return pedidoRepository.sumTotalByFechaCreacionRange(fechaInicio, fechaFin);
    }

    public Pedido savePedido(Pedido pedido) {
        if (pedido.getUsuario() == null || pedido.getUsuario().getIdUsuario() == null)
            throw new IllegalArgumentException("El usuario es obligatorio.");
        
        // Si no se proporciona estado, asignar PENDIENTE por defecto
        if (pedido.getEstado() == null) {
            EstadoPedido estadoPendiente = estadoRepository.findByNombreEstado("PENDIENTE")
                    .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado en la base de datos."));
            pedido.setEstado(estadoPendiente);
        }
        
        // Si no se proporciona fecha de creación, usar la actual
        if (pedido.getFecha_creacion() == null) {
            pedido.setFecha_creacion(java.time.LocalDateTime.now());
        }
        
        return pedidoRepository.save(pedido);
    }
    public Pedido updatePedido(Pedido pedido, Integer id) {
        if (pedido.getUsuario() == null) {
            // Lanza una excepción
            throw new IllegalArgumentException("El usuario asociado al pedido es requerido.");
        }
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("El pedido con id: " + id + " no existe."));
        if(pedido.getFecha_creacion() != null) pedidoExistente.setFecha_creacion(pedido.getFecha_creacion());
        if (pedido.getFecha_entrega() != null) pedidoExistente.setFecha_entrega(pedido.getFecha_entrega());
        if (pedido.getHora_entrega() != null) pedidoExistente.setHora_entrega(pedido.getHora_entrega());

        return pedidoRepository.save(pedidoExistente);
    }

    public Pedido updateEstado(int id, String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado o id es obligatorio.");
        }
        Pedido pedidoExistente = pedidoRepository.findById(id).orElseThrow(()-> new RuntimeException("El pedido con id: " + id + " no existe."));
        String estadoNormalizado = estado.trim().toUpperCase();
        EstadoPedido nuevoEstado = estadoRepository.findByNombreEstado(estadoNormalizado)
                .orElseThrow(() -> new RuntimeException("El estado '" + estadoNormalizado + "' no es válido o no existe."));

        pedidoExistente.setEstado(nuevoEstado);
        return pedidoRepository.save(pedidoExistente);
    }
}
