# Paquete: service

## ¿Qué va aquí?

La capa de lógica de negocio. Los servicios reciben peticiones del menú (UI),
aplican las validaciones de los requisitos de seguridad (RS-001 a RS-005)
y delegan el acceso a datos en los repositorios.

**Regla clave:** ninguna clase de `ui` debe llamar directamente a un repositorio.
Toda la lógica pasa por el servicio correspondiente.

## Clases a implementar

| Clase               | Responsabilidad                                        |
|---------------------|--------------------------------------------------------|
| `MisionService`     | Validaciones RS-001, RS-005 + delegar en repositorio   |
| `AstronautaService` | Validación RS-004 + delegar en repositorio             |
| `SateliteService`   | Validación RS-002 + delegar en repositorio             |
| `TelemetriaService` | Validación RS-003 + delegar en repositorio             |
| `VehiculoService`   | Sin validaciones especiales, delega directamente       |
| `EstacionService`   | Sin validaciones especiales, delega directamente       |

## Ejemplo: validación en el servicio

```java
public class MisionService {

    private final MisionRepository repo = new MisionRepository();

    public void registrar(Mision mision) {
        // RS-001: campos obligatorios
        if (mision.getNombre() == null || mision.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la misión es obligatorio.");
        if (mision.getFechaLanzamiento() == null)
            throw new IllegalArgumentException("La fecha de lanzamiento es obligatoria.");
        if (mision.getEstado() == null)
            throw new IllegalArgumentException("El estado de la misión es obligatorio.");

        // RS-005: unicidad del nombre
        if (repo.existeConNombre(mision.getNombre()))
            throw new IllegalArgumentException("Ya existe una misión con ese nombre.");

        repo.guardar(mision);
    }
}
```

## Excepciones

Lanza `IllegalArgumentException` para errores de validación de datos.
La capa UI captura estas excepciones y muestra el mensaje al usuario.
