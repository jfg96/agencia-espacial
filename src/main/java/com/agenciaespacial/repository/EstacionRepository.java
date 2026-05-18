package com.agenciaespacial.repository;

import com.agenciaespacial.model.EstacionSeguimiento;
import com.agenciaespacial.model.Mision;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link EstacionSeguimiento}.
 * <p>
 * Proporciona operaciones CRUD básicas y una consulta especializada para
 * obtener las misiones monitorizadas por una estación, navegando a través
 * de la relación N:M con {@code Satelite} y de ahí a {@code Mision}.
 * Esta capa no contiene lógica de negocio; cualquier validación se delega
 * en {@code EstacionService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class EstacionRepository {

    /**
     * Persiste una nueva estación de seguimiento en la base de datos.
     *
     * @param estacion la entidad {@link EstacionSeguimiento} a guardar;
     *                 no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public EstacionSeguimiento guardar(EstacionSeguimiento estacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(estacion);
            em.getTransaction().commit();
            return estacion;
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
     * Busca una estación de seguimiento por su identificador único.
     *
     * @param id el identificador de la estación
     * @return un {@link Optional} con la estación si existe,
     *         o vacío en caso contrario
     */
    public Optional<EstacionSeguimiento> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            EstacionSeguimiento estacion = em.find(EstacionSeguimiento.class, id);
            return Optional.ofNullable(estacion);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todas las estaciones de seguimiento almacenadas en la base
     * de datos.
     *
     * @return lista con todas las estaciones; nunca {@code null},
     *         puede estar vacía
     */
    public List<EstacionSeguimiento> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<EstacionSeguimiento> query = em.createQuery(
                    "SELECT e FROM EstacionSeguimiento e", EstacionSeguimiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de una estación de seguimiento ya existente.
     *
     * @param estacion la entidad {@link EstacionSeguimiento} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public EstacionSeguimiento actualizar(EstacionSeguimiento estacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            EstacionSeguimiento actualizada = em.merge(estacion);
            em.getTransaction().commit();
            return actualizada;
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
     * Elimina la estación de seguimiento con el identificador indicado.
     * Si no existe ninguna estación con ese identificador, el método no hace nada.
     *
     * @param id el identificador de la estación a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            EstacionSeguimiento estacion = em.find(EstacionSeguimiento.class, id);
            if (estacion != null) {
                em.remove(estacion);
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

    /**
     * Devuelve la lista de misiones monitorizadas por una estación concreta.
     * <p>
     * La consulta navega la relación: {@code EstacionSeguimiento → Satelite → Mision}
     * y elimina duplicados con {@code DISTINCT}, ya que una misión puede tener
     * varios satélites vigilados por la misma estación.
     * </p>
     *
     * @param estacionId el identificador de la estación de seguimiento
     * @return lista de misiones distintas monitorizadas por esa estación;
     *         nunca {@code null}, puede estar vacía
     */
    public List<Mision> misionesPorEstacion(Long estacionId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Mision> query = em.createQuery(
                    "SELECT DISTINCT s.mision " +
                    "FROM EstacionSeguimiento e JOIN e.satelites s " +
                    "WHERE e.id = :estacionId",
                    Mision.class);
            query.setParameter("estacionId", estacionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
