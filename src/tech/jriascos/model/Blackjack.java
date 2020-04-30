package tech.jriascos.model;

import tech.jriascos.model.Deck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import tech.jriascos.model.Card;
import tech.jriascos.model.Suit;
import tech.jriascos.model.Value;
import tech.jriascos.util.Tools;

public class Blackjack {

    private Deck playdeck;
    private Deck dealerHand;
    private Deck playerHand;
    private double playerFunds;
    private boolean finished;

    public Blackjack() {
        Deck newplaydeck = new Deck();
        newplaydeck.createFullDeck();
        newplaydeck.shuffle();
        this.playdeck = newplaydeck;
        playerHand = new Deck();
        dealerHand = new Deck();
        this.playerFunds = 1000;
        this.finished = true;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public double getPlayerFunds() {
        return this.playerFunds;
    }

    public Deck getPlaydeck() {
        return this.playdeck;
    }

    public Deck getPlayerHand() {
        return this.playerHand;
    }

    public Deck getDealerHand() {
        return this.dealerHand;
    }

    public void startGame(Scene scene) throws FileNotFoundException {
        if (this.finished == false) {
            return;
        }
        clearHands(scene);
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        TextField betTF = (TextField) scene.lookup("#money");
        Label playLog = (Label) scene.lookup("#playLog");
        playLog.setText("");

        if (this.playerFunds > 0) {
            double playerBet = Integer.parseInt(betTF.getText());
 
            if (playerBet > this.playerFunds) {
                playLog.setText(playLog.getText() + "\nYou can't bet more than your current balance.");
                return;
            }
            this.playerHand.draw(this.playdeck);
            this.playerHand.draw(this.playdeck);
            this.dealerHand.draw(this.playdeck);
            this.dealerHand.draw(this.playdeck);
            drawHands(this.playerHand, this.dealerHand, scene, true);

            if (this.playerHand.getValue() == 21 && playerHand.size() == 2) {
                if (this.playerHand.get(0).getValue() == Value.ACE || playerHand.get(1).getValue() == Value.ACE) {
                    playLog.setText(playLog.getText() + "\nBlackjack! Here's your money!");
                    this.playerFunds += playerBet * (3 / 2);
                    this.playerHand.emptyDeck(this.playdeck);
                    this.dealerHand.emptyDeck(this.playdeck);
                    fundsDisplay.setText(String.valueOf(this.playerFunds));
                    this.finished = true;
                    return;
                }
            }

            playLog.setText(playLog.getText() + "\nYour Hand: " + this.playerHand.toString());
            playLog.setText(playLog.getText() + "\nYour hand's value is: " + this.playerHand.getValue());
            playLog.setText(playLog.getText() + "\nDealer's hand: " + this.dealerHand.get(0).toString() + " and another surprise card!");
            playLog.setText(playLog.getText() + "\nWould you like to Hit or Stand?");
            this.finished = false;
        }else {
            this.finished = true;
        }
    }

    public void bet(Scene scene, boolean endRound, Deck playerCards, Deck dealerCards) throws FileNotFoundException {
        if (this.finished == true) {
            return;
        }
        Label playLog = (Label) scene.lookup("#playLog");
        TextField betTF = (TextField) scene.lookup("#money");
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        double playerBet = Integer.parseInt(betTF.getText());


        playerHand.draw(this.playdeck);
        playLog.setText(playLog.getText() + "\nThe card you drew was: " + playerHand.get(playerHand.size() - 1).toString());
        playLog.setText(playLog.getText() + "\nYour hand's new value is: " + playerHand.getValue());
        drawHands(playerCards, dealerCards, scene, true);
        if (this.playerHand.getValue() > 21) {
            playLog.setText(playLog.getText() + "\nBust. Busted at: " + playerHand.getValue());
            this.playerFunds -= playerBet;
            this.finished = true;
            this.playerHand.emptyDeck(this.playdeck);
            this.dealerHand.emptyDeck(this.playdeck);
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
        }
    }

    private void clearHands(Scene scene) {
        HBox dealerHandBox = (HBox) scene.lookup("#dealerHand");
        HBox playerHandBox = (HBox) scene.lookup("#playerHand");

        playerHandBox.getChildren().clear();
        dealerHandBox.getChildren().clear();
    }

    public void stand(Scene scene, boolean endRound) throws FileNotFoundException {
        if (this.finished) {
            return;
        }
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        Label playLog = (Label) scene.lookup("#playLog");
        TextField betTF = (TextField) scene.lookup("#money");
        double playerBet = Integer.parseInt(betTF.getText());
        if ((this.dealerHand.getValue() > this.playerHand.getValue()) && endRound == false) {
            playLog.setText(playLog.getText() + "\nDealer beats you " + this.dealerHand.getValue() + " to " + this.playerHand.getValue());
            this.playerFunds -= playerBet;
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
            endRound = true;
        }
        drawHands(this.playerHand, this.dealerHand, scene, true);
        while ((this.dealerHand.getValue() < 17) && endRound == false) {
            this.dealerHand.draw(this.playdeck);
            playLog.setText(playLog.getText() + "\nDealer draws: " + this.dealerHand.get(this.dealerHand.size() - 1).toString());
        }

        drawHands(this.playerHand, this.dealerHand, scene, false);
        playLog.setText(playLog.getText() + "\nDealer hand value: " + this.dealerHand.getValue());

        if ((dealerHand.getValue() > 21) && endRound == false) {
            playLog.setText(playLog.getText() + "\nDealer has busted. You win!");
            this.playerFunds += playerBet * 2;
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
            endRound = true;
        }

        if ((dealerHand.getValue() == playerHand.getValue()) && endRound == false) {
            playLog.setText(playLog.getText() + "\nPush.");
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
            endRound = true;
        }

        if ((playerHand.getValue() > dealerHand.getValue()) && endRound == false) {
            playLog.setText(playLog.getText() + "\nYou win the hand.");
            this.playerFunds += playerBet * 2;
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
            endRound = true;
        } else if (endRound == false) // dealer wins
        {
            playLog.setText(playLog.getText() + "\nDealer wins.");
            this.playerFunds -= playerBet;
            fundsDisplay.setText(String.valueOf(this.playerFunds));
            System.out.println(this.playerFunds);
        }

        playLog.setText(playLog.getText() + "\nCARDS RETURNED");
        playerHand.emptyDeck(this.playdeck);
        dealerHand.emptyDeck(this.playdeck);
        playLog.setText(playLog.getText() + "\nEnd of this round.");
        this.finished = true;

    }

    private void drawHands(Deck playerCards, Deck dealerCards, Scene scene, boolean conceal) throws FileNotFoundException {
        HBox dealerHandBox = (HBox) scene.lookup("#dealerHand");
        HBox playerHandBox = (HBox) scene.lookup("#playerHand");
        playerHandBox.getChildren().clear();
        dealerHandBox.getChildren().clear();

        for (Card c : this.playerHand) {
            FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + c.getImgString());
            ImageView cardView = new ImageView();
            Image card = new Image(inputStream);
            cardView.setImage(card);
            cardView.setFitHeight(250);
            cardView.setFitWidth(175);
            playerHandBox.getChildren().addAll(cardView);
            
        }

        if (conceal) {
            FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + this.dealerHand.get(0).getImgString());
            ImageView cardView = new ImageView();
            Image card = new Image(inputStream);
            cardView.setImage(card);
            cardView.setFitHeight(250);
            cardView.setFitWidth(175);
            dealerHandBox.getChildren().addAll(cardView);
            FileInputStream inputstream2 = new FileInputStream(Tools.getClasspathDir() + "images/cards/blue_back.png");
            ImageView cardBackView = new ImageView();
            Image cardBack = new Image (inputstream2);
            cardBackView.setImage(cardBack);
            cardBackView.setFitHeight(250);
            cardBackView.setFitWidth(175);
            dealerHandBox.getChildren().addAll(cardBackView);
        }else {
            for (Card c : this.dealerHand) {
                FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + c.getImgString());
                ImageView cardView = new ImageView();
                Image card = new Image(inputStream);
                cardView.setImage(card);
                cardView.setFitHeight(250);
                cardView.setFitWidth(175);
                dealerHandBox.getChildren().addAll(cardView);
            }
        }

        
    }


}
