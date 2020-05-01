package tech.jriascos.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.regex.Pattern;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tech.jriascos.util.Tools;

public class Poker {
    private Deck playerHand;
    private Deck tableCards;
    private Deck playdeck;
    private double playerFunds;
    private boolean finished;
    private boolean secondBetTurn;
    private Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
    private int trades;

    public Poker() {
        Deck newplaydeck = new Deck();
        newplaydeck.createFullDeck();
        //newplaydeck.shuffle();
        this.playdeck = newplaydeck;
        this.playerHand = new Deck();
        this.tableCards = new Deck();
        this.playerFunds = 1000;
        this.finished = true;
        this.secondBetTurn = false;
        this.trades = 3;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public int getTrades() {
        return this.trades;
    }

    public void setTrades(int trades) {
        this.trades = trades;
    }

    public void startGame(Scene scene) throws FileNotFoundException {
        if (this.finished == false) {
            return;
        }
        this.trades = 3;
        clearHands(scene);
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        TextField betTF = (TextField) scene.lookup("#money");
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        Label playLog = (Label) scene.lookup("#playLog");
        Button confirmTrades = (Button) scene.lookup("#confirmTrades");
        playLog.setText("");

        confirmTrades.setDisable(true);

        for (int i = 0; i < 5; i++) {
            CheckBox b = (CheckBox) playButtons.getChildren().get(i);
            b.setDisable(true);
        }

        for (int i = 0; i < 5; i++) {
            CheckBox c = (CheckBox) playButtons.getChildren().get(i);
            c.setSelected(false);
        }

        if (this.playerFunds > 0) {
            double playerBet = Integer.parseInt(betTF.getText());
            if (playerBet > this.playerFunds) {
                playLog.setText(playLog.getText() + "\nYou can't bet more than your current balance.");
                return;
            }
        }
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);

        this.secondBetTurn = true;

        drawHands(scene);

        playLog.setText(playLog.getText() + "\nBet again? If so, enter another amount.\nIf not, then simply hit confirm bet again.");

        this.finished = false;

    }

    public void checkTrades(Scene scene) {
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        Label playLog = (Label) scene.lookup("#playLog");
        int trades = 0;
        for (int i = 0; i < 5; i++) {
            CheckBox c = (CheckBox) playButtons.getChildren().get(i);
            if (c.isSelected()) {
                trades++;
            }
        }
        if (trades > this.trades) {
            playLog.setText("");
            playLog.setText(playLog.getText() + "\nYou have too many trades! There can only be 3, 4 if you have an ace.");
            for (int i = 0; i < 5; i++) {
                CheckBox c = (CheckBox) playButtons.getChildren().get(i);
                c.setSelected(false);
            }
        }
        
    }

    public void confirmTrades(Scene scene) throws FileNotFoundException {
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        Button confirmTrades = (Button) scene.lookup("#confirmTrades");
        for (int i = 0; i < 5; i++) {
            CheckBox c = (CheckBox) playButtons.getChildren().get(i);
            if (c.isSelected() == true) {
                this.playerHand.sendTo(this.playdeck, i);
                this.playerHand.drawToIndex(playdeck, i);
            }
        }

        drawHands(scene);

        confirmTrades.setDisable(true);

        checkHand(scene);

    }

    private void clearHands(Scene scene) {
        HBox playerHandBox = (HBox) scene.lookup("#playerHand");

        playerHandBox.getChildren().clear();
    }

    private void drawHands(Scene scene) throws FileNotFoundException {
        clearHands(scene);
        HBox playerHandBox = (HBox) scene.lookup("#playerHand");

        for (Card c : this.playerHand) {
            ImageView cardView = new ImageView(new Image(new FileInputStream(Tools.getClasspathDir() + "images/cards/" + c.getImgString())));
            cardView.setFitHeight(250);
            cardView.setFitWidth(175);
            playerHandBox.getChildren().add(cardView);
        }
    }

    public void secondBetTurn(Scene scene) {
        Label playLog = (Label) scene.lookup("#playLog");
        TextField betTF = (TextField) scene.lookup("#money");

        if (isNumeric(betTF.getText())) {
            playOrKeep(scene, Integer.parseInt(betTF.getText()));
        }else {
            playOrKeep(scene, -1);
        }
        

    }

    private void playOrKeep(Scene scene, int parseInt) {
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        //this is for the phase of the game where the player can choose to keep or remove the cards they have
        for(Card c : this.playerHand) {
            if (c.getValue() == Value.ACE) {
                this.trades = 4;
            }
        }

        for (int i = 0; i < 5; i++) {
            CheckBox b = (CheckBox) playButtons.getChildren().get(i);
            b.setDisable(false);
        }
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false; 
        }
        return pattern.matcher(strNum).matches();
    }

    public void checkHand(Scene scene) {
        System.out.println("______________");
        //now for the worst part of the game
        boolean royalflush = false;
        boolean royalflush1 = true;
        boolean fourofakind = false;
        boolean fullhouse = false;
        boolean flush = true;
        boolean straight = true;
        boolean threeofakind = false;
        boolean twopair = false;
        boolean pair = false;
        boolean straightflush = false;
        int repetitions = 1;
        int pairs = 0;
        int trios = 0;
        int aces = 0;

        Deck tempArray = this.playerHand;

        Collections.sort(tempArray);

        for (int i = 1; i < 5; i++) {
            if (tempArray.get(i).getValue().ordinal() == tempArray.get(i-1).getValue().ordinal()) {
                repetitions++;
            }
            if (repetitions == 2) {
                pairs++;
            }
            if (repetitions == 3) {
                trios++;
            }
            if (tempArray.get(i).getValue() == Value.ACE) {
                aces++;
            }

        }

        

        //check for three of a kind
        if (repetitions == 3) {
            threeofakind = true;
        }
        //check for four of a kind
        if (repetitions == 4) {
            fourofakind = true;
        }
        //flush check
        for (int i = 1; i < 5; i++) {
            if (tempArray.get(i-1).getSuit() != tempArray.get(i).getSuit()) {
                flush = false;
            }
        }
        //royal flush check 
        for (int i = 0; i < 5; i++) {
            if (tempArray.get(i).getValue().ordinal() < Value.TEN.ordinal()) {
                royalflush1 = false;
            }
        }
        //checking for actual royal flush
        if (flush && royalflush1) {
            royalflush = true;
        }
        //check for a straight
        for(int i = 1; i < 5; i++) {
            if (tempArray.get(i).getValue().ordinal() < tempArray.get(i-1).getValue().ordinal()) {
                straight = false;
            }
        }

        if (pairs == 1) {
            pair = true;
        } else if(pairs == 2) {
            twopair = true;
        }

        if(pairs == 1 && trios == 1) {
            fullhouse = true;
        }

        if(flush = true && straight == true) {
            straightflush = true;
        }

        System.out.println("Pairs: " + pairs);
        System.out.println("Trios: " + trios);
        System.out.println("Pair: " + pair);
        System.out.println("Two Pair: " + twopair);
        System.out.println("Straight: " + straight);
        System.out.println("Three of a Kind: " + threeofakind);
        System.out.println("Four of a Kind: " + fourofakind);
        System.out.println("Flush: " + flush);
        System.out.println("Full House: " + fullhouse);
        System.out.println("Straight Flush: " + straightflush);
        System.out.println("Royal Flush (lol): " + royalflush);

        this.finished = true;
        this.playerHand.emptyDeck(playdeck);
    }

}