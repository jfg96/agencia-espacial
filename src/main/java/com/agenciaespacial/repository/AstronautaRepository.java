package com.agenciaespacial.repository;

import com.agenciaespacial.model.Astronauta;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Astronauta}.
 * <p>
 * Proporciona operaciones CRUD básicas sobre los astronautas registrados
 * en el sistema. Esta capa no contiene lógica de negocio; cualquier
 * validación se delega en {@code AstronautaService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class AstronautaRepository {

    /**
     * Persiste un nuevo astronauta en la base de datos.
     *
     * @param astronauta la entidad {@link Astronauta} a guardar;
     *                   no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public Astronauta guardar(Astronauta astronauta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(astronauta);
            em.getTransaction().commit();
            return astronauta;
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
     * Busca un astronauta por su identificador único.
     *
     * @param id el identificador del astronauta
     * @return un {@link Optional} con el astronauta si existe,
     *         o vacío en caso contrario
     */
    public Optional<Astronauta> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Astronauta astronauta = em.find(Astronauta.class, id);
            return Optional.ofNullable(astronauta);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los astronautas almacenados en la base de datos.
     *
     * @return lista con todos los astronautas; nunca {@code null},
     *         puede estar vacía
     */
    public List<Astronauta> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Astronauta> query = em.createQuery(
                    "SELECT a FROM Astronauta a", Astronauta.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de un astronauta ya existente.
     *
     * @param astronauta la entidad {@link Astronauta} con los datos
     *                   actualizados; debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public Astronauta actualizar(Astronauta astronauta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Astronauta actualizado = em.merge(astronauta);
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
     * Elimina el astronauta con el identificador indicado.
     * Si no existe ningún astronauta con ese identificador, el método no hace nada.
     *
     * @param id el identificador del astronauta a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Astronauta astronauta = em.find(Astronauta.class, id);
            if (astronauta != null) {
                em.remove(astronauta);
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
