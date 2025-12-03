/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pasteleria.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sabor")
public class Sabor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sabor")
    private Integer idSabor;
    @Column(name = "nombre_sabor", unique=true)
    private String nombre_sabor;
    @Column(name = "precio")
    private Double precio;

    public Sabor() {
    }
    public Sabor(Integer idSabor, String nombre_sabor, Double precio) {
        this.idSabor = idSabor;
        this.nombre_sabor = nombre_sabor;
        this.precio = precio;
    }

    public Integer getIdSabor() {
        return idSabor;
    }

    public void setIdSabor(Integer idSabor) {
        this.idSabor = idSabor;
    }

    public String getNombre_sabor() {
        return nombre_sabor;
    }

    public void setNombre_sabor(String nombre_sabor) {
        this.nombre_sabor = nombre_sabor;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}
