package tech.jriascos.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
		return new GridPane();
	}

	public static GridPane buildPKScreen() {
		return new GridPane();
	}
}