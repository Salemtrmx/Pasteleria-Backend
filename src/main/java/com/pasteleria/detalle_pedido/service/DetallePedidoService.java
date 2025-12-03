package com.pasteleria.detalle_pedido.service;

import com.pasteleria.detalle_pedido.model.DetallePedido;
import com.pasteleria.detalle_pedido.repository.DetallePedidoRepository;
import com.pasteleria.inventario.model.PastelInventario;
import com.pasteleria.inventario.repository.PastelInventarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PastelInventarioRepository pastelInventarioRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, 
                                 PastelInventarioRepository pastelInventarioRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pastelInventarioRepository = pastelInventarioRepository;
    }

    // Obtener todos los detalles
    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    // Obtener detalles por id de pedido
    public List<DetallePedido> findAllByPedidoId(Integer pedidoId) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("El id del pedido es obligatorio.");
        }
        return detallePedidoRepository.findByPedido_Id(pedidoId);
    }

    // Guardar un detalle y reducir stock si es pastel de inventario
    public DetallePedido saveDetalle(DetallePedido detalle) {
        if (detalle.getPedido() == null || detalle.getPedido().getId() == 0) {
            throw new IllegalArgumentException("El pedido asociado es obligatorio.");
        }
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        
        // Si es un pastel del catálogo (inventario), reducir el stock
        if (detalle.getPastelInv() != null && detalle.getPastelInv().getIdPastelInventario() != null) {
            PastelInventario pastel = pastelInventarioRepository.findById(detalle.getPastelInv().getIdPastelInventario())
                    .orElseThrow(() -> new RuntimeException("Pastel no encontrado en inventario"));
            
            int nuevoStock = pastel.getStock() - detalle.getCantidad();
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("Stock insuficiente para el pastel: " + pastel.getNombrePastel());
            }
            
            pastel.setStock(nuevoStock);
            pastelInventarioRepository.save(pastel);
        }
        
        return detallePedidoRepository.save(detalle);
    }

    // Actualizar detalle
    public DetallePedido updateDetalle(Integer id, DetallePedido detalle) {
        DetallePedido existente = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle con id " + id + " no existe."));

        if (detalle.getPrecioUnitario() != null) existente.setPrecioUnitario(detalle.getPrecioUnitario());
        if (detalle.getTipoPastel() != null) existente.setTipoPastel(detalle.getTipoPastel());
        if (detalle.getCantidad() != null) existente.setCantidad(detalle.getCantidad());
        if (detalle.getMensajeTexto() != null) existente.setMensajeTexto(detalle.getMensajeTexto());
        if (detalle.getRutaImagen() != null) existente.setRutaImagen(detalle.getRutaImagen());
        if (detalle.getPastelInv() != null) existente.setPastelInv(detalle.getPastelInv());
        if (detalle.getSabor() != null) existente.setSabor(detalle.getSabor());
        if (detalle.getTamanio() != null) existente.setTamanio(detalle.getTamanio());
        if (detalle.getDecoracion() != null) existente.setDecoracion(detalle.getDecoracion());

        return detallePedidoRepository.save(existente);
    }

    // Eliminar detalle
    public void deleteDetalle(Integer id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new RuntimeException("Detalle con id " + id + " no existe.");
        }
        detallePedidoRepository.deleteById(id);
    }
}
