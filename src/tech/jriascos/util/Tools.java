package tech.jriascos.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tech.jriascos.application.SceneBuilder;

public class Tools {
    public static void mainMenuListeners(Scene scene, Stage stage) {
        Button startBJ = (Button) scene.lookup("#startBJ");
        Button startPK = (Button) scene.lookup("#startPK");

        EventHandler<ActionEvent> showBJScreen = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stage.getScene().setRoot(SceneBuilder.buildBJScreen());
            }
        };

        startBJ.setOnAction(showBJScreen);

        EventHandler<ActionEvent> showPKScreen = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stage.getScene().setRoot(SceneBuilder.buildPKScreen());
            }
        };

        startPK.setOnAction(showPKScreen);
    }

    public static String getClasspathDir() {
        String classpath = System.getProperty("java.class.path", ".");
        boolean windows = false;
        if (classpath.matches(".*\\\\.*")) {
            windows = true;
        }
        if (windows) {
            String[] splitClasspathDir = classpath.split(";");
            String classpathDirectory = "";
            for (String s : splitClasspathDir) {
                if (s.matches(".*lib\\\\.*")) {
                    classpathDirectory = s;
                }
            }
            return classpathDirectory;
        } else {
            String[] splitClasspathDir = classpath.split(":");
            String classpathDirectory = "";
            for (String s : splitClasspathDir) {
                if (s.matches(".*lib/.*")) {
                    classpathDirectory = s;
                }
            }
            return classpathDirectory;
        }
    }
}