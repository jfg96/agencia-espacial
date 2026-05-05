# Paquete: util

## ¿Qué va aquí?

Clases de utilidad compartidas por el resto de capas.

## Clases incluidas

| Clase       | Descripción                                                           |
|-------------|-----------------------------------------------------------------------|
| `JPAUtil`   | Singleton que gestiona el `EntityManagerFactory` de ObjectDB          |

## Puedes añadir aquí también

- `Validador.java`: métodos estáticos de validación reutilizables
  (p.ej. validar rango numérico, fechas no futuras, etc.)
- `Consola.java`: helpers para leer entradas del usuario de forma segura
  (leer un Long, leer un LocalDate, confirmar acción con s/n, etc.)

## JPAUtil — Recordatorio de uso

```java
// Obtener un EntityManager para una operación
EntityManager em = JPAUtil.getEntityManager();
try {
    em.getTransaction().begin();
    // ... operaciones ...
    em.getTransaction().commit();
} catch (Exception e) {
    if (em.getTransaction().isActive()) em.getTransaction().rollback();
    throw e;
} finally {
    em.close(); // SIEMPRE cerrar el EntityManager
}

// Al terminar la aplicación (en Menu.iniciar(), antes de salir)
JPAUtil.close();
```
