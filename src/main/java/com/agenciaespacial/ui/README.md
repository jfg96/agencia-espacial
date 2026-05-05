# Paquete: ui

## ¿Qué va aquí?

La interfaz de usuario por consola. Lee la entrada del usuario,
llama a los servicios correspondientes y muestra los resultados.

## Clases a implementar

| Clase           | Responsabilidad                                              |
|-----------------|--------------------------------------------------------------|
| `Menu`          | Menú principal con el bucle de opciones                      |
| `MisionMenu`    | Submenú para todas las operaciones sobre Misiones            |
| `AstronautaMenu`| Submenú para Astronautas                                     |
| `SateliteMenu`  | Submenú para Satélites                                       |
| `TelemetriaMenu`| Submenú para Registros de Telemetría                         |
| `VehiculoMenu`  | Submenú para Vehículos de Lanzamiento                        |
| `EstacionMenu`  | Submenú para Estaciones de Seguimiento                       |

## Estructura del menú principal (Menu.java)

```java
public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== AGENCIA ESPACIAL =====");
            System.out.println("1. Misiones");
            System.out.println("2. Vehículos de lanzamiento");
            System.out.println("3. Astronautas");
            System.out.println("4. Satélites");
            System.out.println("5. Registros de telemetría");
            System.out.println("6. Estaciones de seguimiento");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            int opcion = Integer.parseInt(scanner.nextLine().trim());
            switch (opcion) {
                case 1 -> new MisionMenu(scanner).mostrar();
                case 2 -> new VehiculoMenu(scanner).mostrar();
                // ...
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }
        JPAUtil.close();
        System.out.println("Hasta luego.");
    }
}
```

## Buenas prácticas

- Captura siempre `IllegalArgumentException` de los servicios y muestra
  el mensaje de error sin traza de pila.
- Usa `scanner.nextLine()` en vez de `scanner.nextInt()` para evitar
  problemas con el buffer de entrada.
- Cierra `JPAUtil` al salir del menú principal (libera la conexión con ObjectDB).
