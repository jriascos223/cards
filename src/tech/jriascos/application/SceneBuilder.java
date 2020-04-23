package tech.jriascos.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import tech.jriascos.util.Tools;

public class SceneBuilder {
    // Lots of panes and nodes being made here, to eventually replace each other
    // with setRoot()
    public static GridPane buildMainMenu() throws FileNotFoundException {
        GridPane grid = new GridPane();
        grid.setId("mainMenu");
        grid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(50);
        col2.setPercentWidth(50);

        grid.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        grid.getRowConstraints().addAll(row1);

        VBox blackjackSide = new VBox();
        blackjackSide = buildBlackjackSide();
        blackjackSide.setId("blackjackSide");
        grid.add(blackjackSide, 0, 0);

        VBox pokerSide = new VBox();
        pokerSide = buildPokerSide();
        pokerSide.setId("pokerSide");
        grid.add(pokerSide, 1, 0);

        return grid;
    }

    private static VBox buildPokerSide() throws FileNotFoundException {
        VBox container = new VBox();
        container.setSpacing(5);
        container.setAlignment(Pos.CENTER);
        container.setId("pokerContainer");
        Button startPK = new Button();
        startPK.prefWidthProperty().bind(container.widthProperty().divide(2));
        startPK.setText("Play Poker!");
        startPK.setId("startPK");
        FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/poker.png");
        Image titlePK = new Image(inputStream);
        ImageView imageView = new ImageView(titlePK);
        imageView.setId("PKImage");
        container.getChildren().addAll(imageView, startPK);

        return container;
    }

    private static VBox buildBlackjackSide() throws FileNotFoundException {
        VBox container = new VBox();
        container.setSpacing(5);
        container.setAlignment(Pos.CENTER);
        container.setId("blackjackContainer");
        Button startBJ = new Button();
        startBJ.prefWidthProperty().bind(container.widthProperty().divide(2));
        startBJ.setText("Play Blackjack!");
        startBJ.setId("startBJ");
        System.out.println(Tools.getClasspathDir());
        FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/blackjack.png");
        Image titleBJ = new Image(inputStream);
        ImageView imageView = new ImageView(titleBJ);
        imageView.setId("BJImage");
        container.getChildren().addAll(imageView, startBJ);

        return container;
    }

	public static GridPane buildBJScreen() {
        GridPane screenBJ = new GridPane();
        screenBJ.setId("screenBJ");

        //center, which will contain hit & stand buttons, 
        VBox cMenu = new VBox();
        cMenu.setSpacing(100);
        cMenu.prefHeightProperty().bind(screenBJ.heightProperty());
        cMenu.setId("cMenu");
        cMenu.setAlignment(Pos.CENTER);
        Label playLog = new Label();
        playLog.setId("playLog");
        playLog.setText(playLog.getText() + "WOAH");
        VBox betting = new VBox();
        betting.setId("betting");
        playLog.setAlignment(Pos.CENTER);
        betting.setAlignment(Pos.CENTER);

        Label fundsDisplay = new Label("FUNDS HERE");
        TextField betInput = new TextField();
        Button submit = new Button("CONFIRM BET");

        betting.getChildren().addAll(fundsDisplay, betInput, submit);

        Label dealerLabel = new Label("DEALER");
        dealerLabel.setId("dealerLabel");
        HBox dealerHand = new HBox();
        dealerHand.setId("dealerHand");

        HBox playButtons = new HBox();
        Button hitButton = new Button("HIT");
        Button standButton = new Button("STAND");
        hitButton.setId("hitButton");
        standButton.setId("standButton");
        playButtons.getChildren().addAll(hitButton, standButton);
        playButtons.setAlignment(Pos.CENTER);
        playButtons.setSpacing(10);


        Label playerLabel = new Label("PLAYER");
        playerLabel.setId("playerLabel");
        HBox playerHand = new HBox();
        playerHand.setId("playerHand");

        cMenu.getChildren().addAll(dealerLabel, dealerHand, playButtons, playerLabel, playerHand);

        screenBJ.add(playLog, 0, 0);
        screenBJ.add(cMenu, 1, 0);
        screenBJ.add(betting, 2, 0);

        ColumnConstraints clm1 = new ColumnConstraints();
        ColumnConstraints clm2 = new ColumnConstraints();
        ColumnConstraints clm3 = new ColumnConstraints();

        RowConstraints row1 = new RowConstraints();

        row1.setPercentHeight(100);

        clm1.setPercentWidth(33);
        clm2.setPercentWidth(33);
        clm3.setPercentWidth(33);

        screenBJ.getColumnConstraints().addAll(clm1, clm2, clm3);
        screenBJ.getRowConstraints().addAll(row1);

		return screenBJ;
	}

	public static GridPane buildPKScreen() {
		return new GridPane();
	}
}