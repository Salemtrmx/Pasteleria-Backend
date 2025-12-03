package com.pasteleria.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "decoracion")
public class Decoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_decoracion")
    private Integer idDecoracion;
    @Column(name = "nombre_decoracion", unique=true)
    private String nombre_decoracion;
    @Column(name = "precio")
    private Double precio;

    public Decoracion() {
    }

    public Decoracion(Integer idDecoracion, String nombre_decoracion, Double precio) {
        this.idDecoracion = idDecoracion;
        this.nombre_decoracion = nombre_decoracion;
        this.precio = precio;
    }

    public Integer getIdDecoracion() {
        return idDecoracion;
    }

    public void setIdDecoracion(Integer idDecoracion) {
        this.idDecoracion = idDecoracion;
    }

    public String getNombre_decoracion() {
        return nombre_decoracion;
    }

    public void setNombre_decoracion(String nombre_decoracion) {
        this.nombre_decoracion = nombre_decoracion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}

