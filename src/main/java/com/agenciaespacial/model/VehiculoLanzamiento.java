package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculo_lanzamiento")
public class VehiculoLanzamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String modelo;

    // RS-002 equivalente: validado en la capa de servicio (capacidad > 0)
    @Column(name = "capacidad_kg", nullable = false)
    private Double capacidadKg;

    @Column(name = "pais_fabricacion", nullable = false)
    private String paisFabricacion;

    // Lado inverso de la relación con Misión (1:N)
    @OneToMany(mappedBy = "vehiculo")
    private List<Mision> misiones = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────
    public VehiculoLanzamiento() {}

    public VehiculoLanzamiento(String nombre, String modelo,
                                Double capacidadKg, String paisFabricacion) {
        this.nombre         = nombre;
        this.modelo         = modelo;
        this.capacidadKg    = capacidadKg;
        this.paisFabricacion = paisFabricacion;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public String getNombre()                  { return nombre; }
    public void setNombre(String nombre)       { this.nombre = nombre; }

    public String getModelo()                  { return modelo; }
    public void setModelo(String modelo)       { this.modelo = modelo; }

    public Double getCapacidadKg()             { return capacidadKg; }
    public void setCapacidadKg(Double c)       { this.capacidadKg = c; }

    public String getPaisFabricacion()         { return paisFabricacion; }
    public void setPaisFabricacion(String p)   { this.paisFabricacion = p; }

    public List<Mision> getMisiones()          { return misiones; }
    public void setMisiones(List<Mision> m)    { this.misiones = m; }

    @Override
    public String toString() {
        return "VehiculoLanzamiento{id=" + id + ", nombre='" + nombre +
               "', modelo='" + modelo + "', capacidadKg=" + capacidadKg +
               ", pais='" + paisFabricacion + "'}";
    }
}
