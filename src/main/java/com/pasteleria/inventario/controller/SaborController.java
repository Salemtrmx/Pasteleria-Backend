package com.pasteleria.inventario.controller;

import com.pasteleria.inventario.model.Sabor;
import com.pasteleria.inventario.repository.SaborRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sabor")
public class SaborController {

    private final SaborRepository saborRepository;

    public SaborController(SaborRepository saborRepository) {
        this.saborRepository = saborRepository;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<Sabor> sabores = saborRepository.findAll();
            return ResponseEntity.ok(sabores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Sabor sabor = saborRepository.findById(id).orElse(null);
            if (sabor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El sabor no existe");
            }
            return ResponseEntity.ok(sabor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Sabor sabor) {
        try {
            if (sabor.getNombre_sabor() == null || sabor.getNombre_sabor().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del sabor es requerido");
            }
            if (sabor.getPrecio() == null) {
                sabor.setPrecio(0.0);
            }
            Sabor nuevo = saborRepository.save(sabor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Sabor sabor) {
        try {
            Sabor existente = saborRepository.findById(id).orElse(null);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El sabor no existe");
            }
            if (sabor.getNombre_sabor() != null) existente.setNombre_sabor(sabor.getNombre_sabor());
            if (sabor.getPrecio() != null) existente.setPrecio(sabor.getPrecio());
            return ResponseEntity.ok(saborRepository.save(existente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!saborRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El sabor no existe");
            }
            saborRepository.deleteById(id);
            return ResponseEntity.ok("Sabor eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se puede eliminar el sabor porque está en uso");
        }
    }
}
