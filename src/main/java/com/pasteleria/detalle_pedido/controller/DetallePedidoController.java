package com.pasteleria.detalle_pedido.controller;

import com.pasteleria.detalle_pedido.model.DetallePedido;
import com.pasteleria.detalle_pedido.service.DetallePedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // Obtener todos los detalles
    @GetMapping
    public ResponseEntity<List<DetallePedido>> findAll() {
        return ResponseEntity.ok(detallePedidoService.findAll());
    }

    // Obtener detalles por id de pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> findAllByPedidoId(@PathVariable Integer pedidoId) {
        try {
            return ResponseEntity.ok(detallePedidoService.findAllByPedidoId(pedidoId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Guardar detalle
    @PostMapping
    public ResponseEntity<?> saveDetalle(@RequestBody DetallePedido detalle) {
        try {
            DetallePedido nuevo = detallePedidoService.saveDetalle(detalle);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar detalle
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDetalle(@PathVariable Integer id, @RequestBody DetallePedido detalle) {
        try {
            DetallePedido actualizado = detallePedidoService.updateDetalle(id, detalle);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDetalle(@PathVariable Integer id) {
        try {
            detallePedidoService.deleteDetalle(id);
            return ResponseEntity.ok("Detalle eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
