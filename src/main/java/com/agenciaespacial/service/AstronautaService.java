package com.agenciaespacial.service;

import com.agenciaespacial.model.Astronauta;
import com.agenciaespacial.repository.AstronautaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link Astronauta}.
 * <p>
 * Es el único punto de acceso a datos de astronautas para la capa de
 * interfaz; ningún controlador debe llamar directamente al repositorio.
 * Aplica la siguiente regla de negocio antes de delegar en
 * {@link AstronautaRepository}:
 * </p>
 * <ul>
 *   <li><b>RS-004</b>: la fecha de nacimiento no puede ser una fecha
 *       futura.</li>
 * </ul>
 * <p>
 * Cuando la validación falla se lanza {@link IllegalArgumentException}
 * con un mensaje descriptivo que el controlador JavaFX mostrará en un
 * {@code Alert}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class AstronautaService {

    /** Repositorio de acceso a datos de astronautas. */
    private final AstronautaRepository astronautaRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public AstronautaService() {
        this.astronautaRepository = new AstronautaRepository();
    }

    /**
     * Valida y guarda un nuevo astronauta en el sistema.
     * <p>
     * Aplica RS-004 (fecha de nacimiento no futura).
     * </p>
     *
     * @param astronauta la entidad {@link Astronauta} a persistir;
     *                   no debe ser {@code null}
     * @return el astronauta guardado con su identificador asignado
     * @throws IllegalArgumentException si la fecha de nacimiento es futura
     */
    public Astronauta guardar(Astronauta astronauta) {
        validarFechaNacimiento(astronauta.getFechaNacimiento());
        return astronautaRepository.guardar(astronauta);
    }

    /**
     * Busca un astronauta por su identificador único.
     *
     * @param id el identificador del astronauta
     * @return un {@link Optional} con el astronauta si existe, o vacío
     *         en caso contrario
     */
    public Optional<Astronauta> buscarPorId(Long id) {
        return astronautaRepository.buscarPorId(id);
    }

    /**
     * Recupera todos los astronautas almacenados en el sistema.
     *
     * @return lista con todos los astronautas; nunca {@code null}
     */
    public List<Astronauta> listarTodos() {
        return astronautaRepository.listarTodos();
    }

    /**
     * Valida y actualiza los datos de un astronauta existente.
     * <p>
     * Aplica RS-004 (fecha de nacimiento no futura).
     * </p>
     *
     * @param astronauta la entidad {@link Astronauta} con los datos
     *                   actualizados; debe tener un identificador válido
     * @return el astronauta actualizado
     * @throws IllegalArgumentException si la fecha de nacimiento es futura
     */
    public Astronauta actualizar(Astronauta astronauta) {
        validarFechaNacimiento(astronauta.getFechaNacimiento());
        return astronautaRepository.actualizar(astronauta);
    }

    /**
     * Elimina el astronauta con el identificador indicado.
     *
     * @param id el identificador del astronauta a eliminar
     */
    public void eliminar(Long id) {
        astronautaRepository.eliminar(id);
    }

    // ── Métodos privados de validación ────────────────────────────────────

    /**
     * Comprueba que la fecha de nacimiento no sea futura (RS-004).
     *
     * @param fechaNacimiento la fecha a validar
     * @throws IllegalArgumentException si la fecha es posterior a la fecha
     *         actual
     */
    private void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException(
                    "RS-004: La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "RS-004: La fecha de nacimiento no puede ser una fecha futura.");
        }
    }
}
