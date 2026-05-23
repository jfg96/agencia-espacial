package com.agenciaespacial.service;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.repository.MisionRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link Mision}.
 * <p>
 * Es el único punto de acceso a datos de misiones para la capa de
 * interfaz; ningún controlador debe llamar directamente al repositorio.
 * Aplica las siguientes reglas de negocio antes de delegar en
 * {@link MisionRepository}:
 * </p>
 * <ul>
 *   <li><b>RS-001</b>: {@code nombre}, {@code fechaLanzamiento} y
 *       {@code estado} son obligatorios (no nulos ni vacíos).</li>
 *   <li><b>RS-005</b>: el nombre de la misión debe ser único en el
 *       sistema.</li>
 * </ul>
 * <p>
 * Cuando una validación falla se lanza {@link IllegalArgumentException}
 * con un mensaje descriptivo que el controlador JavaFX mostrará en un
 * {@code Alert}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class MisionService {

    /** Repositorio de acceso a datos de misiones. */
    private final MisionRepository misionRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public MisionService() {
        this.misionRepository = new MisionRepository();
    }

    /**
     * Valida y guarda una nueva misión en el sistema.
     * <p>
     * Aplica RS-001 (campos obligatorios) y RS-005 (nombre único).
     * </p>
     *
     * @param mision la entidad {@link Mision} a persistir; no debe ser
     *               {@code null}
     * @return la misión guardada con su identificador asignado
     * @throws IllegalArgumentException si el nombre, la fecha de lanzamiento
     *         o el estado son nulos/vacíos, o si ya existe una misión con
     *         el mismo nombre
     */
    public Mision guardar(Mision mision) {
        validarCamposObligatorios(mision);
        // RS-005: unicidad del nombre solo al crear
        if (misionRepository.existeConNombre(mision.getNombre())) {
            throw new IllegalArgumentException(
                    "RS-005: Ya existe una misión con el nombre '"
                    + mision.getNombre() + "'.");
        }
        return misionRepository.guardar(mision);
    }

    /**
     * Busca una misión por su identificador único.
     *
     * @param id el identificador de la misión
     * @return un {@link Optional} con la misión si existe, o vacío en
     *         caso contrario
     */
    public Optional<Mision> buscarPorId(Long id) {
        return misionRepository.buscarPorId(id);
    }

    /**
     * Recupera todas las misiones almacenadas en el sistema.
     *
     * @return lista con todas las misiones; nunca {@code null}
     */
    public List<Mision> listarTodas() {
        return misionRepository.listarTodas();
    }

    /**
     * Valida y actualiza los datos de una misión existente.
     * <p>
     * Aplica RS-001 (campos obligatorios). No aplica RS-005 en la
     * actualización porque se asume que el nombre no cambia; si se
     * permitiera cambiar el nombre, el controlador debe gestionar ese
     * caso antes de llamar a este método.
     * </p>
     *
     * @param mision la entidad {@link Mision} con los datos actualizados;
     *               debe tener un identificador válido
     * @return la misión actualizada
     * @throws IllegalArgumentException si el nombre, la fecha de lanzamiento
     *         o el estado son nulos/vacíos
     */
    public Mision actualizar(Mision mision) {
        validarCamposObligatorios(mision);
        misionRepository.buscarPorId(mision.getId()).ifPresent(existente -> {
            if (!existente.getNombre().equals(mision.getNombre())
                    && misionRepository.existeConNombre(mision.getNombre())) {
                throw new IllegalArgumentException(
                        "RS-005: Ya existe una misión con el nombre '"
                        + mision.getNombre() + "'.");
            }
        });
        return misionRepository.actualizar(mision);
    }

    /**
     * Elimina la misión con el identificador indicado.
     *
     * @param id el identificador de la misión a eliminar
     */
    public void eliminar(Long id) {
        misionRepository.eliminar(id);
    }

    // ── Métodos privados de validación ────────────────────────────────────

    /**
     * Comprueba que los campos obligatorios de una misión (RS-001)
     * no sean nulos ni estén vacíos.
     *
     * @param mision la misión a validar
     * @throws IllegalArgumentException si algún campo obligatorio falta
     */
    private void validarCamposObligatorios(Mision mision) {
        if (mision.getNombre() == null || mision.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "RS-001: El nombre de la misión es obligatorio.");
        }
        if (mision.getFechaLanzamiento() == null) {
            throw new IllegalArgumentException(
                    "RS-001: La fecha de lanzamiento de la misión es obligatoria.");
        }
        if (mision.getEstado() == null || mision.getEstado().isBlank()) {
            throw new IllegalArgumentException(
                    "RS-001: El estado de la misión es obligatorio.");
        }
    }
}
