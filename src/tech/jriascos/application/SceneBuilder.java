package tech.jriascos.application;

import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class SceneBuilder {
    //Lots of panes and nodes being made here, to eventually replace each other with setRoot()
    public static GridPane buildMainMenu() {
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

    private static VBox buildPokerSide() {
        VBox container = new VBox();
        return new VBox();
    }

    private static VBox buildBlackjackSide() {
        return new VBox();
    }
}