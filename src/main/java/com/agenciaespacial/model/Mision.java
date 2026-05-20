package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mision",
       uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))  // RS-005
public class Mision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // RS-001: nombre obligatorio + RS-005: único
    @Column(nullable = false, unique = true)
    private String nombre;

    // RS-001: objetivo obligatorio
    @Column(nullable = false)
    private String objetivo;

    // RS-001: fecha de lanzamiento obligatoria
    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    @Column(name = "fecha_fin_prevista")
    private LocalDate fechaFinPrevista;

    // RS-001: estado obligatorio
    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Boolean tripulada = false;

    // ── Relación N:1 con VehiculoLanzamiento (lado propietario) ──
    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private VehiculoLanzamiento vehiculo;

    // ── Relación 1:N con Satelite (lado inverso) ──
    @OneToMany(mappedBy = "mision", cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Satelite> satelites = new ArrayList<>();

    // ── Relación 1:N con Participacion (lado inverso) ──
    @OneToMany(mappedBy = "mision", cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Participacion> participaciones = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────
    public Mision() {}

    public Mision(String nombre, String objetivo, LocalDate fechaLanzamiento,
                  LocalDate fechaFinPrevista, String estado,
                  Boolean tripulada, VehiculoLanzamiento vehiculo) {
        this.nombre            = nombre;
        this.objetivo          = objetivo;
        this.fechaLanzamiento  = fechaLanzamiento;
        this.fechaFinPrevista  = fechaFinPrevista;
        this.estado            = estado;
        this.tripulada         = tripulada;
        this.vehiculo          = vehiculo;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public String getNombre()                        { return nombre; }
    public void setNombre(String nombre)             { this.nombre = nombre; }

    public String getObjetivo()                      { return objetivo; }
    public void setObjetivo(String objetivo)         { this.objetivo = objetivo; }

    public LocalDate getFechaLanzamiento()           { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate f)     { this.fechaLanzamiento = f; }

    public LocalDate getFechaFinPrevista()           { return fechaFinPrevista; }
    public void setFechaFinPrevista(LocalDate f)     { this.fechaFinPrevista = f; }

    public String getEstado()                        { return estado; }
    public void setEstado(String estado)             { this.estado = estado; }

    public Boolean getTripulada()                    { return tripulada; }
    public void setTripulada(Boolean tripulada)      { this.tripulada = tripulada; }

    public VehiculoLanzamiento getVehiculo()         { return vehiculo; }
    public void setVehiculo(VehiculoLanzamiento v)   { this.vehiculo = v; }

    public List<Satelite> getSatelites()             { return satelites; }
    public void setSatelites(List<Satelite> s)       { this.satelites = s; }

    public List<Participacion> getParticipaciones()  { return participaciones; }
    public void setParticipaciones(List<Participacion> p) { this.participaciones = p; }

    @Override
    public String toString() {
        return "Mision{id=" + id + ", nombre='" + nombre +
               "', estado='" + estado + "', tripulada=" + tripulada + "}";
    }
}
