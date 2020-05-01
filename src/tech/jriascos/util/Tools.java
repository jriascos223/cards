package tech.jriascos.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tech.jriascos.application.SceneBuilder;
import tech.jriascos.model.Blackjack;
import tech.jriascos.model.Card;
import tech.jriascos.model.Poker;

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
                listenersBJ(scene, stage, game);
            }
        };

        startBJ.setOnAction(showBJScreen);

        EventHandler<ActionEvent> showPKScreen = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Poker game = new Poker();
                try {
                    stage.getScene().setRoot(SceneBuilder.buildPKScreen());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                listenersPK(scene, stage, game);
            }
        };

        startPK.setOnAction(showPKScreen);
    }

    protected static void listenersPK(Scene scene, Stage stage, Poker game) {
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        Button confirmBet = (Button) scene.lookup("#confirmBet");
        Button confirmTrades = (Button) scene.lookup("#confirmTrades");
        Button backButton = (Button) scene.lookup("#backButton");

        EventHandler<ActionEvent> back = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    scene.setRoot(SceneBuilder.buildMainMenu());
                    mainMenuListeners(scene, stage);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        };

        backButton.setOnAction(back);

        EventHandler<ActionEvent> holdCard = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    game.checkTrades(scene);
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        EventHandler<ActionEvent> bet = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (game.getFinished()) {
                    try {
                        game.startGame(scene);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    confirmTrades.setDisable(false);
                    game.secondBetTurn(scene);
                }
                
            }
        };

        EventHandler<ActionEvent> cTrades = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    game.confirmTrades(scene);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };

        confirmTrades.setOnAction(cTrades);

        confirmBet.setOnAction(bet);

        for (int i = 0; i < 5; i++) {
            CheckBox b = (CheckBox) playButtons.getChildren().get(i);
            b.setOnAction(holdCard);
        }

    }

    protected static void listenersBJ(Scene scene, Stage stage, Blackjack game) {
        Button hitButton = (Button) scene.lookup("#hitButton");
        Button standButton = (Button) scene.lookup("#standButton");
        Button confirmBet = (Button) scene.lookup("#confirmBet");
        Button backButton = (Button) scene.lookup("#backButton");

        EventHandler<ActionEvent> back = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    scene.setRoot(SceneBuilder.buildMainMenu());
                    mainMenuListeners(scene, stage);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        };

        backButton.setOnAction(back);

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