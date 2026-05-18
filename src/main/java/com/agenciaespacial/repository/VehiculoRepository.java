package com.agenciaespacial.repository;

import com.agenciaespacial.model.VehiculoLanzamiento;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link VehiculoLanzamiento}.
 * <p>
 * Proporciona operaciones CRUD básicas sobre los vehículos de lanzamiento.
 * Esta capa no contiene lógica de negocio; cualquier validación se
 * delega en {@code VehiculoService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class VehiculoRepository {

    /**
     * Persiste un nuevo vehículo de lanzamiento en la base de datos.
     *
     * @param vehiculo la entidad {@link VehiculoLanzamiento} a guardar;
     *                 no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public VehiculoLanzamiento guardar(VehiculoLanzamiento vehiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehiculo);
            em.getTransaction().commit();
            return vehiculo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Busca un vehículo de lanzamiento por su identificador único.
     *
     * @param id el identificador del vehículo
     * @return un {@link Optional} con el vehículo si existe, o vacío en caso contrario
     */
    public Optional<VehiculoLanzamiento> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehiculoLanzamiento vehiculo = em.find(VehiculoLanzamiento.class, id);
            return Optional.ofNullable(vehiculo);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los vehículos de lanzamiento almacenados en la base de datos.
     *
     * @return lista con todos los vehículos; nunca {@code null}, puede estar vacía
     */
    public List<VehiculoLanzamiento> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<VehiculoLanzamiento> query = em.createQuery(
                    "SELECT v FROM VehiculoLanzamiento v", VehiculoLanzamiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de un vehículo de lanzamiento ya existente.
     *
     * @param vehiculo la entidad {@link VehiculoLanzamiento} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public VehiculoLanzamiento actualizar(VehiculoLanzamiento vehiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            VehiculoLanzamiento actualizado = em.merge(vehiculo);
            em.getTransaction().commit();
            return actualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Elimina el vehículo de lanzamiento con el identificador indicado.
     * Si no existe ningún vehículo con ese identificador, el método no hace nada.
     *
     * @param id el identificador del vehículo a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            VehiculoLanzamiento vehiculo = em.find(VehiculoLanzamiento.class, id);
            if (vehiculo != null) {
                em.remove(vehiculo);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
