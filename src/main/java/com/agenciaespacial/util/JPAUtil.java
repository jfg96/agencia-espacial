package com.agenciaespacial.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase utilitaria que gestiona el ciclo de vida del EntityManagerFactory.
 *
 * USO:
 *   EntityManager em = JPAUtil.getEntityManager();
 *   // ... operaciones JPA ...
 *   em.close();
 *
 * Al cerrar la aplicación:
 *   JPAUtil.close();
 *
 * NOTAS:
 *   - El nombre "agencia-espacial" debe coincidir con el persistence-unit en persistence.xml.
 *   - EntityManagerFactory es costosa de crear; se instancia una sola vez (singleton).
 *   - Cada hilo / operación debe usar su propio EntityManager y cerrarlo cuando termine.
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT = "agencia-espacial";
    private static EntityManagerFactory emf;

    private JPAUtil() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
