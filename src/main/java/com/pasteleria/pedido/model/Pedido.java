package com.pasteleria.pedido.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pasteleria.usuario.model.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "pedido")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private int id;

    @Column(name = "fecha_creacion")
    private LocalDateTime fecha_creacion;
    
    @Column(name = "fecha_entrega", nullable = false)
    private LocalDate fecha_entrega;
    
    @Column(name = "hora_entrega", nullable = false)
    private LocalTime hora_entrega;
    
    @Column(name = "precio_total", nullable = false)
    private Double precio;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado")
    private EstadoPedido estado;

    public Pedido() {
    }

    public Pedido(int id, LocalDateTime fecha_creacion, LocalDate fecha_entrega, LocalTime hora_entrega, Double precio, String metodoPago, Usuario usuario, EstadoPedido estado) {
        this.id = id;
        this.fecha_creacion = fecha_creacion;
        this.fecha_entrega = fecha_entrega;
        this.hora_entrega = hora_entrega;
        this.precio = precio;
        this.metodoPago = metodoPago;
        this.usuario = usuario;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDate getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(LocalDate fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public LocalTime getHora_entrega() {
        return hora_entrega;
    }

    public void setHora_entrega(LocalTime hora_entrega) {
        this.hora_entrega = hora_entrega;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
