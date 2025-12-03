package com.pasteleria.inventario.controller;

import com.pasteleria.inventario.model.PastelInventario;
import com.pasteleria.inventario.service.PastelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class PastelInventarioController {

    private final PastelService pastelService;

    public PastelInventarioController(PastelService pastelService) {
        this.pastelService = pastelService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(pastelService.findAllPasteles());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            PastelInventario pastel = pastelService.findById(id).orElse(null);
            if (pastel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El pastel no existe");
            }
            return ResponseEntity.ok(pastel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<?> save(@RequestBody PastelInventario pastelInventario) {
        try {
            PastelInventario nuevo = pastelService.savePastel(pastelInventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody PastelInventario pastelInventario) {
        try {
            return ResponseEntity.ok(pastelService.updatePastelInventario(id, pastelInventario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pastelService.deletePastelInventario(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<?> activar(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pastelService.activarPastel(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}