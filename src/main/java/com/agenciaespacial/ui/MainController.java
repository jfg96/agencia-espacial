package com.agenciaespacial.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador de la ventana principal (MainView).
 *
 * Responsabilidades:
 * - Abrir ventanas secundarias (misiones, astronautas, satélites, telemetría, vehículos, estaciones).
 * - Encapsular la carga de recursos FXML y la creación de {@code Stage} en el método {@link #abrir}.
 *
 * Consideraciones:
 * - Las excepciones al cargar FXML se registran en la consola; para producción se recomienda
 *   mostrar mensajes de error al usuario o un logger centralizado.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 */
public class MainController {

    private void abrir(String fxml, String titulo, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/agenciaespacial/ui/" + fxml));
            Stage s = new Stage();
            s.setTitle(titulo);
            s.setScene(new Scene(loader.load(), w, h));
            s.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void abrirMisiones()    { abrir("mision/MisionView.fxml",       "Misiones",                    1050, 660); }
    @FXML private void abrirAstronautas() { abrir("astronauta/AstronautaView.fxml","Astronautas",                 950,  620); }
    @FXML private void abrirSatelites()   { abrir("satelite/SateliteView.fxml",    "Satélites",                   1000, 640); }
    @FXML private void abrirTelemetria()  { abrir("telemetria/TelemetriaView.fxml","Telemetría",                  1000, 640); }
    @FXML private void abrirVehiculos()   { abrir("vehiculo/VehiculoView.fxml",    "Vehículos de Lanzamiento",    950,  620); }
    @FXML private void abrirEstaciones()  { abrir("estacion/EstacionView.fxml",    "Estaciones de Seguimiento",   1000, 640); }
}
