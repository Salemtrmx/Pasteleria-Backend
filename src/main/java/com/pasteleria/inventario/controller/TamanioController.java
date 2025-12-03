package com.pasteleria.inventario.controller;

import com.pasteleria.inventario.model.Tamanio;
import com.pasteleria.inventario.repository.TamanioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tamanio")
public class TamanioController {

    private final TamanioRepository tamanioRepository;

    public TamanioController(TamanioRepository tamanioRepository) {
        this.tamanioRepository = tamanioRepository;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<Tamanio> tamanios = tamanioRepository.findAll();
            return ResponseEntity.ok(tamanios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            Tamanio tamanio = tamanioRepository.findById(id).orElse(null);
            if (tamanio == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tamaño no existe");
            }
            return ResponseEntity.ok(tamanio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Tamanio tamanio) {
        try {
            if (tamanio.getNombre_tamanio() == null || tamanio.getNombre_tamanio().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del tamaño es requerido");
            }
            if (tamanio.getPrecio() == null) {
                tamanio.setPrecio(0.0);
            }
            Tamanio nuevo = tamanioRepository.save(tamanio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Tamanio tamanio) {
        try {
            Tamanio existente = tamanioRepository.findById(id).orElse(null);
            if (existente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tamaño no existe");
            }
            if (tamanio.getNombre_tamanio() != null) existente.setNombre_tamanio(tamanio.getNombre_tamanio());
            if (tamanio.getPrecio() != null) existente.setPrecio(tamanio.getPrecio());
            return ResponseEntity.ok(tamanioRepository.save(existente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!tamanioRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tamaño no existe");
            }
            tamanioRepository.deleteById(id);
            return ResponseEntity.ok("Tamaño eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se puede eliminar el tamaño porque está en uso");
        }
    }
}
