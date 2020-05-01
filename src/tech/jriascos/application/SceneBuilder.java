package tech.jriascos.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
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
import javafx.scene.text.TextAlignment;
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
        FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/blackjack.png");
        Image titleBJ = new Image(inputStream);
        ImageView imageView = new ImageView(titleBJ);
        imageView.setId("BJImage");
        container.getChildren().addAll(imageView, startBJ);

        return container;
    }

	public static GridPane buildBJScreen() throws FileNotFoundException {
        GridPane screenBJ = new GridPane();
        screenBJ.setId("screenBJ");

        //center, which will contain hit & stand buttons, 
        VBox cMenu = new VBox();
        cMenu.setSpacing(60);
        cMenu.prefHeightProperty().bind(screenBJ.heightProperty());
        cMenu.setId("cMenu");
        cMenu.setAlignment(Pos.CENTER);
        Label playLog = new Label();
        playLog.setId("playLog");
        Button back = new Button("BACK");
        back.setId("backButton");
        VBox betting = new VBox();
        betting.setSpacing(10);
        betting.setId("betting");
        betting.setAlignment(Pos.CENTER);
        back.setAlignment(Pos.TOP_LEFT);
        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(back, playLog);
        playLog.setAlignment(Pos.CENTER);
        leftSide.prefHeightProperty().bind(screenBJ.prefHeightProperty());

        Label fundsDisplay = new Label();
        fundsDisplay.setId("fundsDisplay");
        HBox betInputRow = new HBox();
        betInputRow.setSpacing(5);
        Button minus = new Button("-");
        minus.setId("minus");
        Button plus = new Button("+");
        plus.setId("plus");
        minus.setPrefWidth(50);
        plus.setPrefWidth(50);
        betInputRow.setId("betIntputRow");
        Label betInput = new Label("2");
        betInput.setId("money");
        betInput.setAlignment(Pos.CENTER);
        betInput.setPrefWidth(270);
        betInputRow.getChildren().addAll(minus, betInput, plus);
        Button submit = new Button("CONFIRM BET");
        submit.setId("confirmBet");

        betting.getChildren().addAll(fundsDisplay, betInputRow, submit);

        Label dealerLabel = new Label("DEALER");
        dealerLabel.setId("dealerLabel");
        HBox dealerHand = new HBox();
        dealerHand.setId("dealerHand");
        dealerHand.setPrefHeight(250);
        dealerHand.setPrefWidth(175);

        HBox playButtons = new HBox();
        Button hitButton = new Button("HIT");
        Button standButton = new Button("STAND");
        hitButton.setId("hitButton");
        standButton.setId("standButton");
        hitButton.setPrefHeight(90);
        standButton.setPrefHeight(90);
        hitButton.setPrefWidth(90);
        standButton.setPrefWidth(90);
        playButtons.getChildren().addAll(hitButton, standButton);
        playButtons.setAlignment(Pos.CENTER);
        playButtons.setSpacing(10);

        Label playerLabel = new Label("PLAYER");
        playerLabel.setId("playerLabel");
        HBox playerHand = new HBox();
        playerHand.setId("playerHand");
        playerHand.setPrefHeight(250);
        playerHand.setPrefWidth(175);

        cMenu.getChildren().addAll(dealerLabel, dealerHand, playButtons, playerLabel, playerHand);

        screenBJ.add(leftSide, 0, 0);
        screenBJ.add(cMenu, 1, 0);
        screenBJ.add(betting, 2, 0);

        ColumnConstraints clm1 = new ColumnConstraints();
        ColumnConstraints clm2 = new ColumnConstraints();
        ColumnConstraints clm3 = new ColumnConstraints();

        RowConstraints row1 = new RowConstraints();

        row1.setPercentHeight(100);

        clm1.setPercentWidth(20);
        clm2.setPercentWidth(60);
        clm3.setPercentWidth(20);

        screenBJ.getColumnConstraints().addAll(clm1, clm2, clm3);
        screenBJ.getRowConstraints().addAll(row1);

		return screenBJ;
	}

	public static GridPane buildPKScreen() throws FileNotFoundException {
        GridPane screenPK = new GridPane();
        screenPK.setId("screenPK");

        //center, which will contain hit & stand buttons, 
        VBox cMenu = new VBox();
        cMenu.setSpacing(60);
        cMenu.prefHeightProperty().bind(screenPK.heightProperty());
        cMenu.setId("cMenu");
        cMenu.setAlignment(Pos.CENTER);
        Label playLog = new Label();
        playLog.setId("playLog");
        Button back = new Button("BACK");
        back.setId("backButton");
        VBox betting = new VBox();
        betting.setSpacing(10);
        betting.setId("betting");
        betting.setAlignment(Pos.CENTER);
        back.setAlignment(Pos.TOP_LEFT);
        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(back, playLog);
        leftSide.prefHeightProperty().bind(screenPK.prefHeightProperty());
        playLog.setAlignment(Pos.CENTER);

        Label fundsDisplay = new Label();
        fundsDisplay.setId("fundsDisplay");
        HBox betInputRow = new HBox();
        betInputRow.setSpacing(5);
        Label betInput = new Label("2");
        betInput.setId("money");
        betInput.setAlignment(Pos.CENTER);
        betInput.setPrefWidth(270);
        Button minus = new Button("-");
        minus.setId("minus");
        Button plus = new Button("+");
        plus.setId("plus");
        minus.setPrefWidth(50);
        plus.setPrefWidth(50);
        betInputRow.setId("betIntputRow");
        betInputRow.getChildren().addAll(minus, betInput, plus);

        Button submit = new Button("CONFIRM BET");
        submit.setId("confirmBet");
        Button confirmTrades = new Button("CONFIRM TRADES");
        confirmTrades.setId("confirmTrades");

        betting.getChildren().addAll(fundsDisplay, betInputRow, submit, confirmTrades);

        HBox playButtons = new HBox();
        CheckBox swap1 = new CheckBox("SWAP FIRST");
        CheckBox swap2 = new CheckBox("SWAP SECOND");
        CheckBox swap3 = new CheckBox("SWAP THIRD");
        CheckBox swap4 = new CheckBox("SWAP FOURTH");
        CheckBox swap5 = new CheckBox("SWAP FIFTH");
        swap1.setId("swap1");
        swap2.setId("swap2");
        swap3.setId("swap3");
        swap4.setId("swap4");
        swap5.setId("swap5");
        swap1.setPrefHeight(120);
        swap2.setPrefHeight(120);
        swap3.setPrefHeight(120);
        swap4.setPrefHeight(120);
        swap5.setPrefHeight(120);
        swap1.setPrefWidth(120);
        swap2.setPrefWidth(120);
        swap3.setPrefWidth(120);
        swap4.setPrefWidth(120);
        swap5.setPrefWidth(120);
        //Jesus I could have done that better
        playButtons.getChildren().addAll(swap1, swap2, swap3, swap4, swap5);
        playButtons.setAlignment(Pos.CENTER);
        playButtons.setSpacing(90);
        playButtons.setId("playButtons");

        Label playerLabel = new Label("PLAYER");
        playerLabel.setId("playerLabel");
        HBox playerHand = new HBox();
        playerHand.setSpacing(60);
        playerHand.setId("playerHand");
        playerHand.setPrefHeight(250);
        playerHand.setPrefWidth(175);

        cMenu.getChildren().addAll(playerLabel, playerHand, playButtons);

        screenPK.add(leftSide, 0, 0);
        screenPK.add(cMenu, 1, 0);
        screenPK.add(betting, 2, 0);

        ColumnConstraints clm1 = new ColumnConstraints();
        ColumnConstraints clm2 = new ColumnConstraints();
        ColumnConstraints clm3 = new ColumnConstraints();

        RowConstraints row1 = new RowConstraints();

        row1.setPercentHeight(100);

        clm1.setPercentWidth(20);
        clm2.setPercentWidth(60);
        clm3.setPercentWidth(20);

        screenPK.getColumnConstraints().addAll(clm1, clm2, clm3);
        screenPK.getRowConstraints().addAll(row1);

		return screenPK;
	}
}