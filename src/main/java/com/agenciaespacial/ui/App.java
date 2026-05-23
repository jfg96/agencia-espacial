package com.agenciaespacial.ui;

import com.agenciaespacial.util.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Aplicación JavaFX principal que inicia la interfaz de usuario.
 *
 * Responsabilidades:
 * - Cargar la vista principal ({@code MainView.fxml}) y mostrarla como {@code Stage}.
 * - Gestionar el ciclo de vida de la aplicación JavaFX.
 * - Liberar recursos de persistencia al cerrar la aplicación mediante {@link com.agenciaespacial.util.JPAUtil#close()}.
 *
 * Uso:
 * - Ejecutar la clase para arrancar la GUI. El método {@code main} delega en {@code Application.launch(...)}.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 * Versión: 1.0
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/agenciaespacial/ui/MainView.fxml"));
        primaryStage.setTitle(" Agencia Espacial");
        primaryStage.setScene(new Scene(loader.load(), 480, 520));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        JPAUtil.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
