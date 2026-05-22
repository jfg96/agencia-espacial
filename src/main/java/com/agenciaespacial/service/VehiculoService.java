package com.agenciaespacial.service;

import com.agenciaespacial.model.VehiculoLanzamiento;
import com.agenciaespacial.repository.VehiculoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link VehiculoLanzamiento}.
 * <p>
 * Es el único punto de acceso a datos de vehículos de lanzamiento para
 * la capa de interfaz; ningún controlador debe llamar directamente al
 * repositorio. Este servicio no aplica validaciones de negocio propias
 * y delega directamente en {@link VehiculoRepository}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class VehiculoService {

    /** Repositorio de acceso a datos de vehículos de lanzamiento. */
    private final VehiculoRepository vehiculoRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public VehiculoService() {
        this.vehiculoRepository = new VehiculoRepository();
    }

    /**
     * Guarda un nuevo vehículo de lanzamiento en el sistema.
     *
     * @param vehiculo la entidad {@link VehiculoLanzamiento} a persistir;
     *                 no debe ser {@code null}
     * @return el vehículo guardado con su identificador asignado
     */
    public VehiculoLanzamiento guardar(VehiculoLanzamiento vehiculo) {
        return vehiculoRepository.guardar(vehiculo);
    }

    /**
     * Busca un vehículo de lanzamiento por su identificador único.
     *
     * @param id el identificador del vehículo
     * @return un {@link Optional} con el vehículo si existe, o vacío
     *         en caso contrario
     */
    public Optional<VehiculoLanzamiento> buscarPorId(Long id) {
        return vehiculoRepository.buscarPorId(id);
    }

    /**
     * Recupera todos los vehículos de lanzamiento almacenados en el sistema.
     *
     * @return lista con todos los vehículos; nunca {@code null}
     */
    public List<VehiculoLanzamiento> listarTodos() {
        return vehiculoRepository.listarTodos();
    }

    /**
     * Actualiza los datos de un vehículo de lanzamiento existente.
     *
     * @param vehiculo la entidad {@link VehiculoLanzamiento} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return el vehículo actualizado
     */
    public VehiculoLanzamiento actualizar(VehiculoLanzamiento vehiculo) {
        return vehiculoRepository.actualizar(vehiculo);
    }

    /**
     * Elimina el vehículo de lanzamiento con el identificador indicado.
     *
     * @param id el identificador del vehículo a eliminar
     */
    public void eliminar(Long id) {
        vehiculoRepository.eliminar(id);
    }
}
