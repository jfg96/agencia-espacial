package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estacion_seguimiento")
public class EstacionSeguimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    // ── Relación N:M con Satélite (lado PROPIETARIO) ──
    // EstacionSeguimiento es el lado propietario porque gestiona
    // la tabla intermedia estacion_satelite
    @ManyToMany
    @JoinTable(
        name = "estacion_satelite",
        joinColumns        = @JoinColumn(name = "estacion_id"),
        inverseJoinColumns = @JoinColumn(name = "satelite_id")
    )
    private List<Satelite> satelites = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────
    public EstacionSeguimiento() {}

    public EstacionSeguimiento(String nombre, String pais, String ciudad,
                                Double latitud, Double longitud) {
        this.nombre   = nombre;
        this.pais     = pais;
        this.ciudad   = ciudad;
        this.latitud  = latitud;
        this.longitud = longitud;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getNombre()                    { return nombre; }
    public void setNombre(String nombre)         { this.nombre = nombre; }

    public String getPais()                      { return pais; }
    public void setPais(String pais)             { this.pais = pais; }

    public String getCiudad()                    { return ciudad; }
    public void setCiudad(String ciudad)         { this.ciudad = ciudad; }

    public Double getLatitud()                   { return latitud; }
    public void setLatitud(Double latitud)       { this.latitud = latitud; }

    public Double getLongitud()                  { return longitud; }
    public void setLongitud(Double longitud)     { this.longitud = longitud; }

    public List<Satelite> getSatelites()         { return satelites; }
    public void setSatelites(List<Satelite> s)   { this.satelites = s; }

    @Override
    public String toString() {
        return "EstacionSeguimiento{id=" + id + ", nombre='" + nombre +
               "', pais='" + pais + "', ciudad='" + ciudad +
               "', lat=" + latitud + ", lon=" + longitud + "}";
    }
}
