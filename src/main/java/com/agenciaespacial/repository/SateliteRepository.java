package com.agenciaespacial.repository;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.model.Satelite;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Satelite}.
 * <p>
 * Proporciona operaciones CRUD básicas y la consulta de satélites
 * filtrados por misión. Esta capa no contiene lógica de negocio;
 * la validación RS-002 (altitud orbital &gt; 0) se aplica en
 * {@code SateliteService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class SateliteRepository {

    /**
     * Persiste un nuevo satélite en la base de datos.
     *
     * @param satelite la entidad {@link Satelite} a guardar;
     *                 no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public Satelite guardar(Satelite satelite) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(satelite);
            em.getTransaction().commit();
            return satelite;
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
     * Busca un satélite por su identificador único.
     *
     * @param id el identificador del satélite
     * @return un {@link Optional} con el satélite si existe,
     *         o vacío en caso contrario
     */
    public Optional<Satelite> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Satelite satelite = em.find(Satelite.class, id);
            return Optional.ofNullable(satelite);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los satélites asociados a una misión concreta.
     *
     * @param mision la {@link Mision} por la que filtrar; no debe ser {@code null}
     * @return lista de satélites de esa misión; nunca {@code null},
     *         puede estar vacía
     */
    public List<Satelite> listarPorMision(Mision mision) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Satelite> query = em.createQuery(
                    "SELECT s FROM Satelite s WHERE s.mision = :mision",
                    Satelite.class);
            query.setParameter("mision", mision);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de un satélite ya existente.
     *
     * @param satelite la entidad {@link Satelite} con los datos actualizados;
     *                 debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public Satelite actualizar(Satelite satelite) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Satelite actualizado = em.merge(satelite);
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
     * Elimina el satélite con el identificador indicado.
     * Si no existe ningún satélite con ese identificador, el método no hace nada.
     *
     * @param id el identificador del satélite a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Satelite satelite = em.find(Satelite.class, id);
            if (satelite != null) {
                em.remove(satelite);
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
