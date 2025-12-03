package com.pasteleria.pedido.controller;

import com.pasteleria.pedido.model.Pedido;
import com.pasteleria.pedido.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    // obtiene todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }
    //obtiene pedidos por cliente
    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> findAllByClienteId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pedidoService.findAllByClienteId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //obtiene pedidos por estado (para admin)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> findAllByEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(pedidoService.findAllByEstadoNombre(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //obtiene pedidos por cliente y estado (para admin o cliente)
    @GetMapping("/cliente/{id}/estado/{estado}")
    public ResponseEntity<?> findAllByClienteAndEstado(@PathVariable Integer id, @PathVariable String estado) {
        try {
            return ResponseEntity.ok(pedidoService.findAllByClienteIdAndEstadoNombre(id, estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Obtiene pedidos por fecha de entrega
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<?> findAllByFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            return ResponseEntity.ok(pedidoService.findAllByFecha(fecha));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Obtiene pedidos por rango de fechas
    @GetMapping("/fecha-rango")
    public ResponseEntity<?> findAllByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            return ResponseEntity.ok(pedidoService.findAllByFechaRange(fechaInicio, fechaFin));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Obtiene estadísticas de ventas por fecha (fecha de compra/creación)
    @GetMapping("/ventas/fecha/{fecha}")
    public ResponseEntity<?> getVentasByFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<Pedido> pedidos = pedidoService.findAllByFechaCreacion(fecha);
            Double total = pedidoService.getTotalVentasByFechaCreacion(fecha);
            
            Map<String, Object> response = new HashMap<>();
            response.put("fecha", fecha);
            response.put("pedidos", pedidos);
            response.put("totalVentas", total);
            response.put("cantidadPedidos", pedidos.size());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Obtiene estadísticas de ventas por rango de fechas (fecha de compra/creación)
    @GetMapping("/ventas/rango")
    public ResponseEntity<?> getVentasByRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Pedido> pedidos = pedidoService.findAllByFechaCreacionRange(fechaInicio, fechaFin);
            Double total = pedidoService.getTotalVentasByFechaCreacionRange(fechaInicio, fechaFin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("fechaInicio", fechaInicio);
            response.put("fechaFin", fechaFin);
            response.put("pedidos", pedidos);
            response.put("totalVentas", total);
            response.put("cantidadPedidos", pedidos.size());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    //guarda pedido
    @PostMapping
    public ResponseEntity<?> savePedido(@RequestBody Pedido pedido) {
        try {
            Pedido nuevo = pedidoService.savePedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //edita pedido por id_pedido
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePedido( @RequestBody Pedido pedido, @PathVariable Integer id) {
        try {
            Pedido actualizado = pedidoService.updatePedido(pedido, id);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // pedido no encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //edita estado pedido (para admin)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id, @RequestParam String estado) {
        try {
            Pedido actualizado = pedidoService.updateEstado(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) { // estado nulo o vacío
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // pedido o estado no existen
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


