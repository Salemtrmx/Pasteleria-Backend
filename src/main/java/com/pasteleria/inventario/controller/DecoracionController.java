package com.pasteleria.inventario.controller;

import com.pasteleria.inventario.model.Decoracion;
import com.pasteleria.inventario.repository.DecoracionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decoracion")
public class DecoracionController {

    private final DecoracionRepository decoracionRepository;

    public DecoracionController(DecoracionRepository decoracionRepository) {
        this.decoracionRepository = decoracionRepository;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<Decoracion> decoraciones = decoracionRepository.findAll();
            return ResponseEntity.ok(decoraciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Decoracion decoracion = decoracionRepository.findById(id).orElse(null);
            if (decoracion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La decoración no existe");
            }
            return ResponseEntity.ok(decoracion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Decoracion decoracion) {
        try {
            if (decoracion.getNombre_decoracion() == null || decoracion.getNombre_decoracion().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de la decoración es requerido");
            }
            if (decoracion.getPrecio() == null) {
                decoracion.setPrecio(0.0);
            }
            Decoracion nuevo = decoracionRepository.save(decoracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Decoracion decoracion) {
        try {
            Decoracion existente = decoracionRepository.findById(id).orElse(null);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La decoración no existe");
            }
            if (decoracion.getNombre_decoracion() != null) existente.setNombre_decoracion(decoracion.getNombre_decoracion());
            if (decoracion.getPrecio() != null) existente.setPrecio(decoracion.getPrecio());
            return ResponseEntity.ok(decoracionRepository.save(existente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!decoracionRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La decoración no existe");
            }
            decoracionRepository.deleteById(id);
            return ResponseEntity.ok("Decoración eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se puede eliminar la decoración porque está en uso");
        }
    }
}
