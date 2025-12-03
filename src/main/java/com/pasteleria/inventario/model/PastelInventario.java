package com.pasteleria.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pastel_inventario")
public class PastelInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pastel_inv")
    private Integer idPastelInventario; 
    @Column(name = "nombre_pastel", unique=true, nullable = false)
    private String nombrePastel;
    @Column(name = "precio", nullable = false)
    private Double precio;
    @Column(name = "stock", nullable = false)
    private Integer stock;
    @Column(name = "imagen_referencia")
    private String urlImagen;

    @Column(name = "estatus")
    private boolean estatus = true;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "id_decoracion", referencedColumnName= "id_decoracion")
    private Decoracion decoracion;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "id_sabor", referencedColumnName= "id_sabor")
    private Sabor sabor;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "id_tamanio", referencedColumnName= "id_tamanio")
    private Tamanio tamanio;

    public PastelInventario() {}

    public PastelInventario(Integer idPastelInventario, String nombrePastel, Double precio, String urlImagen, Integer stock, boolean estatus, Decoracion decoracion, Sabor sabor, Tamanio tamanio) {
        this.idPastelInventario = idPastelInventario;
        this.nombrePastel = nombrePastel;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.stock = stock;
        this.estatus = estatus;
        this.decoracion = decoracion;
        this.sabor = sabor;
        this.tamanio = tamanio;
    }

    public Integer getIdPastelInventario() {
        return idPastelInventario;
    }

    public void setIdPastelInventario(Integer idPastelInventario) {
        this.idPastelInventario = idPastelInventario;
    }

    public String getNombrePastel() {
        return nombrePastel;
    }

    public void setNombrePastel(String nombrePastel) {
        this.nombrePastel = nombrePastel;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Decoracion getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(Decoracion decoracion) {
        this.decoracion = decoracion;
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
}
