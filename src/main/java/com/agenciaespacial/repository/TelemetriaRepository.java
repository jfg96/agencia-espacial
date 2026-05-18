package com.agenciaespacial.repository;

import com.agenciaespacial.model.RegistroTelemetria;
import com.agenciaespacial.model.Satelite;
import com.agenciaespacial.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link RegistroTelemetria}.
 * <p>
 * Proporciona operaciones CRUD y la consulta de registros filtrados
 * por satélite. De acuerdo con el enunciado (RF-007), los registros
 * de telemetría son una entidad débil: no se expone un listado general;
 * solo se accede por ID o por satélite. Esta capa no contiene lógica
 * de negocio; la validación RS-003 (nivelBateria en rango 0–100) se
 * aplica en {@code TelemetriaService}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class TelemetriaRepository {

    /**
     * Persiste un nuevo registro de telemetría en la base de datos.
     *
     * @param registro la entidad {@link RegistroTelemetria} a guardar;
     *                 no debe ser {@code null}
     * @return la misma instancia con el identificador asignado por JPA
     */
    public RegistroTelemetria guardar(RegistroTelemetria registro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(registro);
            em.getTransaction().commit();
            return registro;
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
     * Busca un registro de telemetría por su identificador único.
     *
     * @param id el identificador del registro
     * @return un {@link Optional} con el registro si existe,
     *         o vacío en caso contrario
     */
    public Optional<RegistroTelemetria> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            RegistroTelemetria registro = em.find(RegistroTelemetria.class, id);
            return Optional.ofNullable(registro);
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los registros de telemetría asociados a un satélite concreto.
     *
     * @param satelite el {@link Satelite} por el que filtrar; no debe ser {@code null}
     * @return lista de registros de ese satélite; nunca {@code null},
     *         puede estar vacía
     */
    public List<RegistroTelemetria> listarPorSatelite(Satelite satelite) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<RegistroTelemetria> query = em.createQuery(
                    "SELECT r FROM RegistroTelemetria r WHERE r.satelite = :satelite",
                    RegistroTelemetria.class);
            query.setParameter("satelite", satelite);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de un registro de telemetría ya existente.
     *
     * @param registro la entidad {@link RegistroTelemetria} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return la instancia gestionada por JPA con los cambios aplicados
     */
    public RegistroTelemetria actualizar(RegistroTelemetria registro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            RegistroTelemetria actualizado = em.merge(registro);
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
     * Elimina el registro de telemetría con el identificador indicado.
     * Si no existe ningún registro con ese identificador, el método no hace nada.
     *
     * @param id el identificador del registro a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            RegistroTelemetria registro = em.find(RegistroTelemetria.class, id);
            if (registro != null) {
                em.remove(registro);
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
