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
@Table (name = "tamanio")
public class Tamanio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tamanio")
    private Integer idTamanio;
    @Column(name = "nombre_tamanio", unique=true)
    private String nombre_tamanio;
    @Column(name = "precio")
    private Double precio;
    public Tamanio() {
    }
    public Tamanio(Integer idTamanio, String nombre_tamanio, Double precio) {
        this.idTamanio = idTamanio;
        this.nombre_tamanio = nombre_tamanio;
        this.precio = precio;
    }

    public Integer getIdTamanio() {
        return idTamanio;
    }

    public void setIdTamanio(Integer idTamanio) {
        this.idTamanio = idTamanio;
    }

    public String getNombre_tamanio() {
        return nombre_tamanio;
    }

    public void setNombre_tamanio(String nombre_tamanio) {
        this.nombre_tamanio = nombre_tamanio;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}
