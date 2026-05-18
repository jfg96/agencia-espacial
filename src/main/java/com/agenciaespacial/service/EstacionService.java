package com.agenciaespacial.service;

import com.agenciaespacial.model.EstacionSeguimiento;
import com.agenciaespacial.model.Mision;
import com.agenciaespacial.repository.EstacionRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link EstacionSeguimiento}.
 * <p>
 * Es el único punto de acceso a datos de estaciones de seguimiento para
 * la capa de interfaz; ningún controlador debe llamar directamente al
 * repositorio. Este servicio no aplica validaciones de negocio propias
 * y delega directamente en {@link EstacionRepository}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class EstacionService {

    /** Repositorio de acceso a datos de estaciones de seguimiento. */
    private final EstacionRepository estacionRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public EstacionService() {
        this.estacionRepository = new EstacionRepository();
    }

    /**
     * Guarda una nueva estación de seguimiento en el sistema.
     *
     * @param estacion la entidad {@link EstacionSeguimiento} a persistir;
     *                 no debe ser {@code null}
     * @return la estación guardada con su identificador asignado
     */
    public EstacionSeguimiento guardar(EstacionSeguimiento estacion) {
        return estacionRepository.guardar(estacion);
    }

    /**
     * Busca una estación de seguimiento por su identificador único.
     *
     * @param id el identificador de la estación
     * @return un {@link Optional} con la estación si existe, o vacío
     *         en caso contrario
     */
    public Optional<EstacionSeguimiento> buscarPorId(Long id) {
        return estacionRepository.buscarPorId(id);
    }

    /**
     * Recupera todas las estaciones de seguimiento almacenadas en el sistema.
     *
     * @return lista con todas las estaciones; nunca {@code null}
     */
    public List<EstacionSeguimiento> listarTodas() {
        return estacionRepository.listarTodas();
    }

    /**
     * Actualiza los datos de una estación de seguimiento existente.
     *
     * @param estacion la entidad {@link EstacionSeguimiento} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return la estación actualizada
     */
    public EstacionSeguimiento actualizar(EstacionSeguimiento estacion) {
        return estacionRepository.actualizar(estacion);
    }

    /**
     * Elimina la estación de seguimiento con el identificador indicado.
     *
     * @param id el identificador de la estación a eliminar
     */
    public void eliminar(Long id) {
        estacionRepository.eliminar(id);
    }

    /**
     * Devuelve la lista de misiones monitorizadas por una estación concreta.
     * <p>
     * Navega la relación {@code EstacionSeguimiento → Satelite → Mision}
     * y elimina duplicados, ya que una misión puede tener varios satélites
     * vigilados por la misma estación.
     * </p>
     *
     * @param estacionId el identificador de la estación de seguimiento
     * @return lista de misiones distintas monitorizadas por esa estación;
     *         nunca {@code null}, puede estar vacía
     */
    public List<Mision> misionesPorEstacion(Long estacionId) {
        return estacionRepository.misionesPorEstacion(estacionId);
    }
}
