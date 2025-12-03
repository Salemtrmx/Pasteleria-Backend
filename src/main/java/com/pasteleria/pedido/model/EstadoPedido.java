package com.pasteleria.pedido.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "estado_pedido")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private int id;
    
    @Column(name = "nombre_estado", unique = true, nullable = false)
    private String nombreEstado;

    public EstadoPedido() {
    }

    public EstadoPedido(int id, String nombreEstado) {
        this.id = id;
        this.nombreEstado = nombreEstado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
