package com.agenciaespacial.repository;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Mision}.
 * <p>
 * Proporciona operaciones CRUD básicas y consultas específicas sobre
 * misiones espaciales. Esta capa accede directamente a la base de datos
 * mediante JPA y no contiene lógica de negocio; toda validación se
 * delega en {@code MisionService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class MisionRepository {

    /**
     * Persiste una nueva misión en la base de datos.
     *
     * @param mision la entidad {@link Mision} a guardar; no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public Mision guardar(Mision mision) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mision);
            em.getTransaction().commit();
            return mision;
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
     * Busca una misión por su identificador único.
     *
     * @param id el identificador de la misión
     * @return un {@link Optional} con la misión si existe, o vacío en caso contrario
     */
    public Optional<Mision> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Mision mision = em.find(Mision.class, id);
            return Optional.ofNullable(mision);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todas las misiones almacenadas en la base de datos.
     *
     * @return lista con todas las misiones; nunca {@code null}, puede estar vacía
     */
    public List<Mision> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Mision> query = em.createQuery(
                    "SELECT m FROM Mision m", Mision.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de una misión ya existente.
     *
     * @param mision la entidad {@link Mision} con los datos actualizados; debe
     *               tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public Mision actualizar(Mision mision) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Mision actualizada = em.merge(mision);
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
     * Elimina la misión con el identificador indicado.
     * Si no existe ninguna misión con ese identificador, el método no hace nada.
     *
     * @param id el identificador de la misión a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Mision mision = em.find(Mision.class, id);
            if (mision != null) {
                em.remove(mision);
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
     * Comprueba si ya existe una misión con el nombre indicado.
     * Se utiliza para aplicar la regla de unicidad RS-005.
     *
     * @param nombre el nombre de la misión a comprobar
     * @return {@code true} si ya existe una misión con ese nombre;
     *         {@code false} en caso contrario
     */
    public boolean existeConNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(m) FROM Mision m WHERE m.nombre = :nombre",
                    Long.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
