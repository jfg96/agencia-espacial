package com.agenciaespacial.ui;

import com.agenciaespacial.util.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
