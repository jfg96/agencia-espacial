package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad de solo acceso por ID.
 * El enunciado indica que dado el volumen masivo de datos,
 * cada registro solo puede recuperarse conociendo su ID único.
 * Por ello NO se implementa listado general de esta entidad (RF-007).
 */
@Entity
@Table(name = "registro_telemetria")
public class RegistroTelemetria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ── Relación N:1 con Satélite (lado propietario) ──
    @ManyToOne
    @JoinColumn(name = "satelite_id", nullable = false)
    private Satelite satelite;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Double temperatura;

    @Column(nullable = false)
    private Double velocidad;

    // RS-003: nivel de batería debe estar entre 0 y 100
    // Validado en la capa de servicio: nivelBateria >= 0 && nivelBateria <= 100
    @Column(name = "nivel_bateria", nullable = false)
    private Double nivelBateria;

    // ── Constructores ──────────────────────────────────────────
    public RegistroTelemetria() {}

    public RegistroTelemetria(Satelite satelite, LocalDateTime fechaHora,
                               Double temperatura, Double velocidad,
                               Double nivelBateria) {
        this.satelite     = satelite;
        this.fechaHora    = fechaHora;
        this.temperatura  = temperatura;
        this.velocidad    = velocidad;
        this.nivelBateria = nivelBateria;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public Satelite getSatelite()              { return satelite; }
    public void setSatelite(Satelite s)        { this.satelite = s; }

    public LocalDateTime getFechaHora()        { return fechaHora; }
    public void setFechaHora(LocalDateTime f)  { this.fechaHora = f; }

    public Double getTemperatura()             { return temperatura; }
    public void setTemperatura(Double t)       { this.temperatura = t; }

    public Double getVelocidad()               { return velocidad; }
    public void setVelocidad(Double v)         { this.velocidad = v; }

    public Double getNivelBateria()            { return nivelBateria; }
    public void setNivelBateria(Double n)      { this.nivelBateria = n; }

    @Override
    public String toString() {
        return "RegistroTelemetria{id=" + id +
               ", satelite=" + (satelite != null ? satelite.getNombre() : "null") +
               ", fechaHora=" + fechaHora +
               ", temperatura=" + temperatura +
               ", velocidad=" + velocidad +
               ", bateria=" + nivelBateria + "%}";
    }
}
