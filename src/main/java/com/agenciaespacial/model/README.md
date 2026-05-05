# Paquete: model

## ¿Qué va aquí?

Las clases de entidad JPA que representan el modelo de datos del sistema.
Cada clase debe anotarse con `@Entity` y tener un campo `@Id`.

## Entidades a implementar

| Clase              | Descripción                                              |
|--------------------|----------------------------------------------------------|
| `Mision`           | Misión espacial (puede ser tripulada o no tripulada)     |
| `VehiculoLanzamiento` | Vehículo usado en una misión                          |
| `Astronauta`       | Participante en misiones tripuladas                      |
| `Participacion`    | Relación entre Astronauta y Mision, incluye el rol       |
| `Satelite`         | Satélite puesto en órbita por una misión                 |
| `RegistroTelemetria` | Lectura de telemetría de un satélite (entidad débil)   |
| `EstacionSeguimiento` | Estación terrestre que monitoriza satélites           |

## Convenciones

- Usa `@GeneratedValue` para los IDs autogenerados.
- Las relaciones many-to-many entre Satelite y EstacionSeguimiento
  requieren `@ManyToMany` con `@JoinTable`.
- La relación Astronauta ↔ Mision pasa por la entidad `Participacion`
  para poder almacenar el campo `rol`.
- `RegistroTelemetria` es una entidad débil: solo se puede recuperar
  sabiendo su ID (RF-002). No es necesario listado global (RF-003).

## Ejemplo mínimo de entidad

```java
@Entity
public class Mision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // ... resto de atributos, getters y setters
}
```
