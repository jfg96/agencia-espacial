package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "satelite")
public class Satelite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo;

    // RS-002: altitud orbital debe ser > 0
    // Validado en la capa de servicio: altitudOrbital > 0
    @Column(name = "altitud_orbital", nullable = false)
    private Double altitudOrbital;

    @Column(name = "fecha_orbita", nullable = false)
    private LocalDate fechaOrbita;

    // ── Relación N:1 con Misión (lado propietario) ──
    // Un satélite pertenece a UNA única misión
    @ManyToOne
    @JoinColumn(name = "mision_id", nullable = false)
    private Mision mision;

    // ── Relación 1:N con RegistroTelemetria (lado inverso) ──
    @OneToMany(mappedBy = "satelite", cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<RegistroTelemetria> registrosTelemetria = new ArrayList<>();

    // ── Relación N:M con EstacionSeguimiento ──
    @ManyToMany(mappedBy = "satelites")
    private List<EstacionSeguimiento> estaciones = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────
    public Satelite() {}

    public Satelite(String nombre, String tipo, Double altitudOrbital,
                    LocalDate fechaOrbita, Mision mision) {
        this.nombre         = nombre;
        this.tipo           = tipo;
        this.altitudOrbital = altitudOrbital;
        this.fechaOrbita    = fechaOrbita;
        this.mision         = mision;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }

    public String getNombre()                            { return nombre; }
    public void setNombre(String nombre)                 { this.nombre = nombre; }

    public String getTipo()                              { return tipo; }
    public void setTipo(String tipo)                     { this.tipo = tipo; }

    public Double getAltitudOrbital()                    { return altitudOrbital; }
    public void setAltitudOrbital(Double a)              { this.altitudOrbital = a; }

    public LocalDate getFechaOrbita()                    { return fechaOrbita; }
    public void setFechaOrbita(LocalDate f)              { this.fechaOrbita = f; }

    public Mision getMision()                            { return mision; }
    public void setMision(Mision mision)                 { this.mision = mision; }

    public List<RegistroTelemetria> getRegistrosTelemetria() { return registrosTelemetria; }
    public void setRegistrosTelemetria(List<RegistroTelemetria> r) { this.registrosTelemetria = r; }

    public List<EstacionSeguimiento> getEstaciones()     { return estaciones; }
    public void setEstaciones(List<EstacionSeguimiento> e) { this.estaciones = e; }

    @Override
    public String toString() {
        return "Satelite{id=" + id + ", nombre='" + nombre +
               "', tipo='" + tipo + "', altitud=" + altitudOrbital + "km}";
    }
}
