package com.agenciaespacial.repository;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.model.Participacion;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Participacion}.
 * <p>
 * Gestiona la tabla asociativa entre {@code Astronauta} y {@code Mision},
 * incluyendo el campo extra {@code rol}. Proporciona operaciones CRUD y
 * la consulta de participaciones filtradas por misión. Esta capa no
 * contiene lógica de negocio; cualquier validación se delega en el
 * servicio correspondiente.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class ParticipacionRepository {

    /**
     * Persiste una nueva participación en la base de datos.
     *
     * @param participacion la entidad {@link Participacion} a guardar;
     *                      no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public Participacion guardar(Participacion participacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(participacion);
            em.getTransaction().commit();
            return participacion;
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
     * Busca una participación por su identificador único.
     *
     * @param id el identificador de la participación
     * @return un {@link Optional} con la participación si existe,
     *         o vacío en caso contrario
     */
    public Optional<Participacion> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Participacion participacion = em.find(Participacion.class, id);
            return Optional.ofNullable(participacion);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todas las participaciones asociadas a una misión concreta.
     *
     * @param mision la {@link Mision} por la que filtrar; no debe ser {@code null}
     * @return lista de participaciones de esa misión; nunca {@code null},
     *         puede estar vacía
     */
    public List<Participacion> listarPorMision(Mision mision) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Participacion> query = em.createQuery(
                    "SELECT p FROM Participacion p WHERE p.mision = :mision",
                    Participacion.class);
            query.setParameter("mision", mision);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de una participación ya existente.
     *
     * @param participacion la entidad {@link Participacion} con los datos
     *                      actualizados; debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public Participacion actualizar(Participacion participacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Participacion actualizada = em.merge(participacion);
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
     * Elimina la participación con el identificador indicado.
     * Si no existe ninguna participación con ese identificador, el método
     * no hace nada.
     *
     * @param id el identificador de la participación a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Participacion participacion = em.find(Participacion.class, id);
            if (participacion != null) {
                em.remove(participacion);
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
