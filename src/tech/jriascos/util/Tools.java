package tech.jriascos.util;

import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tech.jriascos.application.SceneBuilder;
import tech.jriascos.model.Blackjack;

public class Tools {
    public static void mainMenuListeners(Scene scene, Stage stage) {
        Button startBJ = (Button) scene.lookup("#startBJ");
        Button startPK = (Button) scene.lookup("#startPK");

        EventHandler<ActionEvent> showBJScreen = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Blackjack game = new Blackjack();
                try {
                    stage.getScene().setRoot(SceneBuilder.buildBJScreen());
                    Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
                    fundsDisplay.setText(String.valueOf(game.getPlayerFunds()));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                listenersBJ(scene, game);
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

    protected static void listenersBJ(Scene scene, Blackjack game) {
        Button hitButton = (Button) scene.lookup("#hitButton");
        Button standButton = (Button) scene.lookup("#standButton");
        Button confirmBet = (Button) scene.lookup("#confirmBet");

        EventHandler<ActionEvent> hit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    game.bet(scene, false, game.getPlayerHand(), game.getDealerHand());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };

        EventHandler<ActionEvent> stand = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    game.stand(scene, false);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };

        EventHandler<ActionEvent> bet = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    game.startGame(scene);

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };

        confirmBet.setOnAction(bet);
        hitButton.setOnAction(hit);
        standButton.setOnAction(stand);
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