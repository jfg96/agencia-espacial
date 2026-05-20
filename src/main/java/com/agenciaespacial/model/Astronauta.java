package com.agenciaespacial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "astronauta")
public class Astronauta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private String nacionalidad;

    // RS-004: fecha de nacimiento no puede ser futura
    // Validado en la capa de servicio: fechaNacimiento.isBefore(LocalDate.now())
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private String especialidad;

    // ── Relación 1:N con Participacion (lado inverso) ──
    @OneToMany(mappedBy = "astronauta", cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Participacion> participaciones = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────
    public Astronauta() {}

    public Astronauta(String nombreCompleto, String nacionalidad,
                      LocalDate fechaNacimiento, String especialidad) {
        this.nombreCompleto  = nombreCompleto;
        this.nacionalidad    = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.especialidad    = especialidad;
    }

    // ── Getters y Setters ──────────────────────────────────────
    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public String getNombreCompleto()                { return nombreCompleto; }
    public void setNombreCompleto(String n)          { this.nombreCompleto = n; }

    public String getNacionalidad()                  { return nacionalidad; }
    public void setNacionalidad(String n)            { this.nacionalidad = n; }

    public LocalDate getFechaNacimiento()            { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate f)      { this.fechaNacimiento = f; }

    public String getEspecialidad()                  { return especialidad; }
    public void setEspecialidad(String e)            { this.especialidad = e; }

    public List<Participacion> getParticipaciones()  { return participaciones; }
    public void setParticipaciones(List<Participacion> p) { this.participaciones = p; }

    @Override
    public String toString() {
        return "Astronauta{id=" + id + ", nombre='" + nombreCompleto +
               "', nacionalidad='" + nacionalidad +
               "', especialidad='" + especialidad + "'}";
    }
}
