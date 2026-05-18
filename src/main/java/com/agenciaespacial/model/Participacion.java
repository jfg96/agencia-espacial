package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entidad asociativa entre Astronauta y Misión.
 * Necesita ser una entidad propia (no @ManyToMany simple)
 * porque tiene un atributo propio: "rol".
 */
@Entity
@Table(name = "participacion",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"astronauta_id", "mision_id"}))
public class Participacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ── Relación N:1 con Astronauta (lado propietario) ──
    @ManyToOne
    @JoinColumn(name = "astronauta_id", nullable = false)
    private Astronauta astronauta;

    // ── Relación N:1 con Misión (lado propietario) ──
    @ManyToOne
    @JoinColumn(name = "mision_id", nullable = false)
    private Mision mision;

    // Atributo propio de la relación (por eso no es @ManyToMany)
    @Column(nullable = false)
    private String rol;

    // ── Constructores ──────────────────────────────────────────
    public Participacion() {}

    public Participacion(Astronauta astronauta, Mision mision, String rol) {
        this.astronauta = astronauta;
        this.mision     = mision;
        this.rol        = rol;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public Astronauta getAstronauta()          { return astronauta; }
    public void setAstronauta(Astronauta a)    { this.astronauta = a; }

    public Mision getMision()                  { return mision; }
    public void setMision(Mision m)            { this.mision = m; }

    public String getRol()                     { return rol; }
    public void setRol(String rol)             { this.rol = rol; }

    @Override
    public String toString() {
        return "Participacion{id=" + id +
               ", astronauta=" + (astronauta != null ? astronauta.getNombreCompleto() : "null") +
               ", mision=" + (mision != null ? mision.getNombre() : "null") +
               ", rol='" + rol + "'}";
    }
}
