# 🚀 Agencia Espacial — Space Mission Manager

> Aplicación de escritorio JavaFX para la gestión integral de misiones espaciales.  
> Permite administrar misiones, astronautas, satélites, vehículos de lanzamiento, estaciones de seguimiento y telemetría mediante operaciones CRUD completas con interfaz gráfica.

---

## 📋 Descripción del proyecto

**Agencia Espacial** es una aplicación Java con interfaz gráfica JavaFX conectada a una base de datos orientada a objetos **ObjectDB** mediante **JPA 2.2**. Implementa una arquitectura en tres capas (UI → Service → Repository) que separa claramente la presentación, la lógica de negocio y el acceso a datos.

La base de datos se crea **automáticamente** al arrancar la aplicación en la carpeta `data/`; no es necesario instalar ni configurar ningún servidor de base de datos externo.

### Funcionalidades principales

- Gestión completa de **misiones** (tripuladas y no tripuladas), con estado, fechas y vehículo asignado
- Gestión de **astronautas** con su especialidad, nacionalidad y participaciones por misión
- Administración de **vehículos de lanzamiento** con capacidad de carga y país de fabricación
- Gestión de **satélites** vinculados a misiones, con altitud orbital y tipo
- Registro de **telemetría** por satélite (temperatura, velocidad, nivel de batería)
- Gestión de **estaciones de seguimiento** con ubicación geográfica y satélites monitorizados
- Validaciones de negocio aplicadas en la capa de servicio (RS-001 a RS-005)

---

## ⚙️ Requisitos previos

Asegúrate de tener instalado lo siguiente antes de ejecutar el proyecto:

| Software | Versión mínima | Enlace de descarga |
|---|---|---|
| Java JDK | 17 | https://adoptium.net |
| Apache Maven | 3.8+ | https://maven.apache.org |
| Git | cualquier versión reciente | https://git-scm.com |

> **No se necesita instalar ningún servidor de base de datos.** ObjectDB está incluido como dependencia Maven y el fichero `agencia-espacial.odb` se genera automáticamente en `data/` al primer arranque.

---

## 🗄️ Configuración de la base de datos

**No se requiere configuración manual.** Al ejecutar la aplicación por primera vez:

1. Se crea automáticamente el directorio `data/` con el archivo `agencia-espacial.odb`.
2. JPA registra todas las entidades declaradas en `persistence.xml` y ObjectDB inicializa el esquema.
3. La aplicación queda lista para usar de inmediato.

Si quieres partir de un esquema SQL de referencia, puedes consultarlo en:

```
src/main/resources/modelo_sql.sql
```

> ⚠️ Este script está en sintaxis MySQL (generado con MySQL Workbench para el diseño del modelo). No es necesario ejecutarlo; la persistencia real usa ObjectDB a través de JPA.

---

## 📥 Clonar el repositorio

```bash
git clone https://github.com/jfg96/agencia-espacial.git
cd agencia-espacial
```

---

## 🔨 Compilación y ejecución

### Opción 1 — Plugin JavaFX de Maven (recomendado)

```bash
mvn javafx:run
```

### Opción 2 — Maven exec

```bash
# Compilar
mvn compile

# Ejecutar
mvn exec:java -Dexec.mainClass="com.agenciaespacial.Main"
```

### Opción 3 — Generar JAR ejecutable

```bash
mvn package
java -cp "target/agencia-espacial-1.0-SNAPSHOT.jar:target/libs/*" com.agenciaespacial.Main
```

> En Windows sustituye `:` por `;` en el classpath.

### Opción 4 — Desde IntelliJ IDEA

1. Abre el proyecto como proyecto Maven (`File → Open → selecciona la carpeta agencia-espacial`).
2. Espera a que Maven descargue las dependencias automáticamente.
3. Localiza `src/main/java/com/agenciaespacial/Main.java` y pulsa el botón **▶ Run**.

---

## 📁 Estructura del proyecto

```
agencia-espacial/
├── pom.xml                                     ← Configuración Maven (Java 17, ObjectDB 2.9.0, JavaFX 17)
├── .gitignore                                  ← Excluye target/, data/, *.odb, archivos de IDE
└── src/main/
    ├── java/com/agenciaespacial/
    │   ├── Main.java                           ← Punto de entrada · delega en App.java
    │   ├── model/
    │   │   ├── Mision.java                     ← @Entity · misión espacial (tripulada o no)
    │   │   ├── Astronauta.java                 ← @Entity · participante en misiones tripuladas
    │   │   ├── Participacion.java              ← @Entity · relación Astronauta ↔ Mision con rol
    │   │   ├── VehiculoLanzamiento.java        ← @Entity · vehículo usado en una misión
    │   │   ├── Satelite.java                   ← @Entity · satélite puesto en órbita
    │   │   ├── RegistroTelemetria.java         ← @Entity · lectura de telemetría de un satélite
    │   │   └── EstacionSeguimiento.java        ← @Entity · estación terrestre de monitorización
    │   ├── repository/
    │   │   ├── MisionRepository.java
    │   │   ├── AstronautaRepository.java
    │   │   ├── ParticipacionRepository.java
    │   │   ├── VehiculoRepository.java
    │   │   ├── SateliteRepository.java
    │   │   ├── TelemetriaRepository.java
    │   │   └── EstacionRepository.java
    │   ├── service/
    │   │   ├── MisionService.java              ← Aplica RS-001 y RS-005
    │   │   ├── AstronautaService.java          ← Aplica RS-004
    │   │   ├── SateliteService.java            ← Aplica RS-002
    │   │   ├── TelemetriaService.java          ← Aplica RS-003
    │   │   ├── VehiculoService.java
    │   │   └── EstacionService.java
    │   ├── ui/
    │   │   ├── App.java                        ← Application JavaFX · carga MainView.fxml
    │   │   ├── MainController.java             ← Abre las ventanas secundarias
    │   │   ├── mision/MisionController.java
    │   │   ├── astronauta/AstronautaController.java
    │   │   ├── satelite/SateliteController.java
    │   │   ├── telemetria/TelemetriaController.java
    │   │   ├── vehiculo/VehiculoController.java
    │   │   └── estacion/EstacionController.java
    │   └── util/
    │       └── JPAUtil.java                    ← Singleton · gestiona EntityManagerFactory
    └── resources/
        ├── META-INF/persistence.xml            ← Persistence unit "agencia-espacial" (ObjectDB)
        ├── modelo_sql.sql                      ← Esquema de referencia (sintaxis MySQL)
        ├── Diagrama E-R.png                    ← Diagrama conceptual del modelo de datos
        └── com/agenciaespacial/ui/
            ├── MainView.fxml
            ├── astronauta/AstronautaView.fxml
            ├── estacion/EstacionView.fxml
            ├── mision/MisionView.fxml
            ├── satelite/SateliteView.fxml
            ├── telemetria/TelemetriaView.fxml
            └── vehiculo/VehiculoView.fxml
```

---

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura en tres capas**:

```
┌─────────────────────────────────────┐
│          UI  (JavaFX / FXML)        │  ← Controllers + Views
├─────────────────────────────────────┤
│       Service  (Lógica de negocio)  │  ← Validaciones RS-001..RS-005
├─────────────────────────────────────┤
│    Repository  (Acceso a datos)     │  ← JPA + ObjectDB (JPQL)
└─────────────────────────────────────┘
```

**Regla clave:** ningún controlador accede directamente a un repositorio. Toda operación pasa por su servicio correspondiente.

### Modelo de datos (entidades JPA)

| Entidad | Descripción | Relaciones principales |
|---|---|---|
| `Mision` | Misión espacial | N:1 con `VehiculoLanzamiento`; 1:N con `Satelite` y `Participacion` |
| `Astronauta` | Tripulante | 1:N con `Participacion` |
| `Participacion` | Astronauta en misión (con rol) | N:1 con `Astronauta` y `Mision` |
| `VehiculoLanzamiento` | Cohete o nave | 1:N con `Mision` |
| `Satelite` | Satélite en órbita | N:1 con `Mision`; N:M con `EstacionSeguimiento` |
| `RegistroTelemetria` | Lectura de datos de un satélite | N:1 con `Satelite` |
| `EstacionSeguimiento` | Estación terrestre | N:M con `Satelite` |

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal · lógica de negocio y modelo |
| JavaFX | 17.0.11 | Interfaz gráfica de usuario (FXML + Controllers) |
| ObjectDB | 2.9.0 | Base de datos orientada a objetos embebida |
| JPA | 2.2 | API de persistencia (`@Entity`, `@ManyToMany`, JPQL…) |
| Apache Maven | 3.8+ | Gestión de dependencias y compilación |
| IntelliJ IDEA | cualquier versión reciente | IDE de desarrollo |

---

## 👥 Equipo

| Nombre | Rol |
|---|---|
| Javier Fernández | Jefe de Proyecto |
| Héctor Rodríguez | Diseñador de Base de Datos |
| Carlos Fernández | Desarrollador Backend |
| Antonio Manuel Rodríguez | Desarrollador de Interfaz |
| Rafa Navarro | Responsable de Calidad y Documentación |

---

## 📹Enlace para ver video de prueba

https://youtu.be/XNfmhqS5CjI

*I.E.S. Delgado Hernández · Bollullos Par del Condado · DAM 2025–2026*
