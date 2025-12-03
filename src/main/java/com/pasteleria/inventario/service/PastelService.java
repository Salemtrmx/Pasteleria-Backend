package com.pasteleria.inventario.service;

import com.pasteleria.inventario.model.*;
import com.pasteleria.inventario.repository.DecoracionRepository;
import com.pasteleria.inventario.repository.PastelInventarioRepository;
import com.pasteleria.inventario.repository.SaborRepository;
import com.pasteleria.inventario.repository.TamanioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PastelService {
    private final SaborRepository saborRepository;
    private final TamanioRepository tamanioRepository;
    private final DecoracionRepository decoracionRepository;
    private final PastelInventarioRepository pastelInventarioRepository;

    public PastelService(SaborRepository saborRepository, TamanioRepository tamanioRepository,
                         DecoracionRepository decoracionRepository, PastelInventarioRepository pastelInventarioRepository) {
        this.saborRepository = saborRepository;
        this.tamanioRepository = tamanioRepository;
        this.decoracionRepository = decoracionRepository;
        this.pastelInventarioRepository = pastelInventarioRepository;
    }

    public List<PastelInventario> findAllPasteles(){
        return pastelInventarioRepository.findAll();
    }

    public Optional<PastelInventario> findById(Integer id){
        return pastelInventarioRepository.findById(id);
    }

    public PastelInventario savePastel(PastelInventario p) {
        if (p.getSabor() == null || p.getSabor().getIdSabor() == null)
            throw new IllegalArgumentException("idSabor requerido");

        if (p.getDecoracion() == null || p.getDecoracion().getIdDecoracion() == null)
            throw new IllegalArgumentException("idDecoracion requerido");

        if (p.getTamanio() == null || p.getTamanio().getIdTamanio() == null)
            throw new IllegalArgumentException("idTamanio requerido");

        Sabor sabor = saborRepository.findById(p.getSabor().getIdSabor())
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        Decoracion decoracion = decoracionRepository.findById(p.getDecoracion().getIdDecoracion())
                .orElseThrow(() -> new RuntimeException("Decoración no encontrada"));

        Tamanio tamanio = tamanioRepository.findById(p.getTamanio().getIdTamanio())
                .orElseThrow(() -> new RuntimeException("Tamaño no encontrado"));

        p.setSabor(sabor);
        p.setDecoracion(decoracion);
        p.setTamanio(tamanio);

        return pastelInventarioRepository.save(p);
    }

    public PastelInventario updatePastelInventario(Integer id, PastelInventario pastelInventario){
        PastelInventario pastelExistente = pastelInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pastel con id: " + id + " no existe"));

        if(pastelInventario.getNombrePastel() != null) pastelExistente.setNombrePastel(pastelInventario.getNombrePastel());
        if(pastelInventario.getUrlImagen() != null) pastelExistente.setUrlImagen(pastelInventario.getUrlImagen());
        if (pastelInventario.getPrecio() != null) pastelExistente.setPrecio(pastelInventario.getPrecio());
        if (pastelInventario.getStock() != null) pastelExistente.setStock(pastelInventario.getStock());
        
        // Actualizar estatus
        pastelExistente.setEstatus(pastelInventario.getEstatus());

        if (pastelInventario.getSabor() != null && pastelInventario.getSabor().getIdSabor() != null) {
            Sabor sabor = saborRepository.findById(pastelInventario.getSabor().getIdSabor())
                    .orElseThrow(() -> new RuntimeException("El sabor con id " + pastelInventario.getSabor().getIdSabor() + " no existe"));
            pastelExistente.setSabor(sabor);
        }

        if (pastelInventario.getDecoracion() != null && pastelInventario.getDecoracion().getIdDecoracion() != null) {
            Decoracion decoracion = decoracionRepository.findById(pastelInventario.getDecoracion().getIdDecoracion())
                    .orElseThrow(() -> new RuntimeException("La decoración con id " + pastelInventario.getDecoracion().getIdDecoracion() + " no existe"));
            pastelExistente.setDecoracion(decoracion);
        }

        if (pastelInventario.getTamanio() != null && pastelInventario.getTamanio().getIdTamanio() != null) {
            Tamanio tamanio = tamanioRepository.findById(pastelInventario.getTamanio().getIdTamanio())
                    .orElseThrow(() -> new RuntimeException("El tamaño con id " + pastelInventario.getTamanio().getIdTamanio() + " no existe"));
            pastelExistente.setTamanio(tamanio);
        }

        return pastelInventarioRepository.save(pastelExistente);
    }

    public String deletePastelInventario(Integer id){
        PastelInventario pastelExistente = pastelInventarioRepository.findByIdPastelInventarioAndEstatus(id, true)
                .orElseThrow(() -> new RuntimeException("El pastel con id: " + id + " no existe o está desactivado"));
        pastelExistente.setEstatus(false);
        pastelInventarioRepository.save(pastelExistente);
        return "Pastel Eliminado";
    }

    public String activarPastel(Integer id){
        PastelInventario pastelExistente = pastelInventarioRepository.findByIdPastelInventarioAndEstatus(id, false)
                .orElseThrow(() -> new RuntimeException("El pastel con id: " + id + " no existe o está activado"));
        pastelExistente.setEstatus(true);
        pastelInventarioRepository.save(pastelExistente);
        return "Pastel Activado";
    }
}
