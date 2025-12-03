package com.pasteleria.detalle_pedido.model;

import com.pasteleria.inventario.model.Decoracion;
import com.pasteleria.inventario.model.PastelInventario;
import com.pasteleria.inventario.model.Sabor;
import com.pasteleria.inventario.model.Tamanio;
import com.pasteleria.pedido.model.Pedido;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_ped")
    private Integer idDetalle;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "tipo_pastel", nullable = false, length = 50)
    private String tipoPastel;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "mensaje_texto", columnDefinition = "TEXT")
    private String mensajeTexto;

    @Column(name = "ruta_imagen", length = 255)
    private String rutaImagen;


    @ManyToOne(optional = true)
    @JoinColumn(name = "id_pastel_inv")
    private PastelInventario pastelInv;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_sabor")
    private Sabor sabor;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tamanio")
    private Tamanio tamanio;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_decoracion")
    private Decoracion decoracion;

    public DetallePedido() {
    }

    public DetallePedido(Integer idDetalle, Pedido pedido, Double precioUnitario, String tipoPastel, Integer cantidad, String mensajeTexto, String rutaImagen, PastelInventario pastelInv, Sabor sabor, Tamanio tamanio, Decoracion decoracion) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.precioUnitario = precioUnitario;
        this.tipoPastel = tipoPastel;
        this.cantidad = cantidad;
        this.mensajeTexto = mensajeTexto;
        this.rutaImagen = rutaImagen;
        this.pastelInv = pastelInv;
        this.sabor = sabor;
        this.tamanio = tamanio;
        this.decoracion = decoracion;
    }

    // Getters y setters

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getTipoPastel() {
        return tipoPastel;
    }

    public void setTipoPastel(String tipoPastel) {
        this.tipoPastel = tipoPastel;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMensajeTexto() {
        return mensajeTexto;
    }

    public void setMensajeTexto(String mensajeTexto) {
        this.mensajeTexto = mensajeTexto;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public PastelInventario getPastelInv() {
        return pastelInv;
    }

    public void setPastelInv(PastelInventario pastelInv) {
        this.pastelInv = pastelInv;
    }

    public Sabor getSabor() {
        return sabor;
    }

    public void setSabor(Sabor sabor) {
        this.sabor = sabor;
    }

    public Tamanio getTamanio() {
        return tamanio;
    }

    public void setTamanio(Tamanio tamanio) {
        this.tamanio = tamanio;
    }

    public Decoracion getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(Decoracion decoracion) {
        this.decoracion = decoracion;
    }

}
