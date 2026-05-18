package com.agenciaespacial.service;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.model.Satelite;
import com.agenciaespacial.repository.SateliteRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link Satelite}.
 * <p>
 * Es el único punto de acceso a datos de satélites para la capa de
 * interfaz; ningún controlador debe llamar directamente al repositorio.
 * Aplica la siguiente regla de negocio antes de delegar en
 * {@link SateliteRepository}:
 * </p>
 * <ul>
 *   <li><b>RS-002</b>: la altitud orbital debe ser estrictamente mayor
 *       que cero.</li>
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
public class SateliteService {

    /** Repositorio de acceso a datos de satélites. */
    private final SateliteRepository sateliteRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public SateliteService() {
        this.sateliteRepository = new SateliteRepository();
    }

    /**
     * Valida y guarda un nuevo satélite en el sistema.
     * <p>
     * Aplica RS-002 (altitud orbital &gt; 0).
     * </p>
     *
     * @param satelite la entidad {@link Satelite} a persistir;
     *                 no debe ser {@code null}
     * @return el satélite guardado con su identificador asignado
     * @throws IllegalArgumentException si la altitud orbital no es
     *         estrictamente mayor que cero
     */
    public Satelite guardar(Satelite satelite) {
        validarAltitudOrbital(satelite.getAltitudOrbital());
        return sateliteRepository.guardar(satelite);
    }

    /**
     * Busca un satélite por su identificador único.
     *
     * @param id el identificador del satélite
     * @return un {@link Optional} con el satélite si existe, o vacío
     *         en caso contrario
     */
    public Optional<Satelite> buscarPorId(Long id) {
        return sateliteRepository.buscarPorId(id);
    }

    /**
     * Recupera todos los satélites asociados a una misión concreta.
     *
     * @param mision la {@link Mision} por la que filtrar; no debe ser
     *               {@code null}
     * @return lista de satélites de esa misión; nunca {@code null}
     */
    public List<Satelite> listarPorMision(Mision mision) {
        return sateliteRepository.listarPorMision(mision);
    }

    /**
     * Valida y actualiza los datos de un satélite existente.
     * <p>
     * Aplica RS-002 (altitud orbital &gt; 0).
     * </p>
     *
     * @param satelite la entidad {@link Satelite} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return el satélite actualizado
     * @throws IllegalArgumentException si la altitud orbital no es
     *         estrictamente mayor que cero
     */
    public Satelite actualizar(Satelite satelite) {
        validarAltitudOrbital(satelite.getAltitudOrbital());
        return sateliteRepository.actualizar(satelite);
    }

    /**
     * Elimina el satélite con el identificador indicado.
     *
     * @param id el identificador del satélite a eliminar
     */
    public void eliminar(Long id) {
        sateliteRepository.eliminar(id);
    }

    // ── Métodos privados de validación ────────────────────────────────────

    /**
     * Comprueba que la altitud orbital sea estrictamente positiva (RS-002).
     *
     * @param altitudOrbital el valor a validar
     * @throws IllegalArgumentException si el valor es nulo, cero o negativo
     */
    private void validarAltitudOrbital(Double altitudOrbital) {
        if (altitudOrbital == null) {
            throw new IllegalArgumentException(
                    "RS-002: La altitud orbital es obligatoria.");
        }
        if (altitudOrbital <= 0) {
            throw new IllegalArgumentException(
                    "RS-002: La altitud orbital debe ser mayor que 0 km. "
                    + "Valor introducido: " + altitudOrbital);
        }
    }
}
