package tech.jriascos.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tech.jriascos.model.Blackjack;
import tech.jriascos.util.Tools;

public class Window extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle("Blackjack and Poker");
        primaryStage.setMaximized(true);

        final Scene defaultScene = new Scene(SceneBuilder.buildMainMenu(), 300, 275);
        defaultScene.getStylesheets().add(Window.class.getResource("/style/style.css").toExternalForm());
        
        primaryStage.setScene(defaultScene);

        primaryStage.show();

        Tools.mainMenuListeners(defaultScene, primaryStage);
    }
}