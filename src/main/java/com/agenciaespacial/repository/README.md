# Paquete: repository

## ¿Qué va aquí?

Las clases de acceso a datos (DAO / Repository). Cada clase gestiona
las operaciones CRUD de una entidad concreta usando JPA + ObjectDB.

## Clases a implementar

| Clase                        | Responsabilidad                                      |
|------------------------------|------------------------------------------------------|
| `MisionRepository`           | CRUD de Misión + consultas especiales                |
| `VehiculoRepository`         | CRUD de VehiculoLanzamiento                          |
| `AstronautaRepository`       | CRUD de Astronauta                                   |
| `ParticipacionRepository`    | CRUD de Participacion                                |
| `SateliteRepository`         | CRUD de Satelite + buscar por misión                 |
| `TelemetriaRepository`       | CRUD de RegistroTelemetria + buscar por satélite     |
| `EstacionRepository`         | CRUD de EstacionSeguimiento + misiones por estación  |

## Estructura típica de un repositorio

```java
public class MisionRepository {

    public void guardar(Mision mision) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mision);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Mision buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Mision.class, id);
        } finally {
            em.close();
        }
    }

    public List<Mision> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mision m", Mision.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public void actualizar(Mision mision) { ... }
    public void eliminar(Long id) { ... }
}
```

## Consultas JPQL relevantes

- Satélites por misión: `SELECT s FROM Satelite s WHERE s.mision.id = :idMision`
- Telemetría por satélite: `SELECT t FROM RegistroTelemetria t WHERE t.satelite.id = :idSatelite`
- Astronautas por misión: `SELECT p FROM Participacion p WHERE p.mision.id = :idMision`
- Misiones por estación: requiere join a través de la relación ManyToMany entre Satelite y EstacionSeguimiento
