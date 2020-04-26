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

    public Blackjack() {
        Deck playdeck = new Deck();
        playdeck.createFullDeck();
        playdeck.shuffle();
        this.playdeck = playdeck;
        playerHand = new Deck();
        dealerHand = new Deck();
        this.playerFunds = 1000;
    }

    public void startGame(Scene scene) throws FileNotFoundException {
        TextField betTF = (TextField) scene.lookup("#money");
        Label playLog = (Label) scene.lookup("#playLog");

        if (playerFunds > 0) {
            double playerBet = Integer.parseInt(betTF.getText());
 
            if (playerBet > playerFunds) {
                playLog.setText(playLog.getText() + "\nYou can't bet more than your current balance.");
            }

            this.playerHand.draw(this.playdeck);
            this.playerHand.draw(this.playdeck);
            this.dealerHand.draw(this.playdeck);
            this.dealerHand.draw(this.playdeck);
            drawHands(playerHand, dealerHand, scene, false);

            playLog.setText(playLog.getText() + "\nYour Hand: " + this.playerHand.toString());
            playLog.setText(playLog.getText() + "\nYour hand's value is: " + this.playerHand.getValue());
            playLog.setText(playLog.getText() + "\nDealer's hand: " + this.dealerHand.get(0).toString() + " and another surprise card!");
            playLog.setText(playLog.getText() + "\nWould you like to Hit or Stand?");

            
        }
    }

    public void bet(Scene scene, boolean endRound) {
        Label playLog = (Label) scene.lookup("#playLog");
        TextField betTF = (TextField) scene.lookup("#money");
        double playerBet = Integer.parseInt(betTF.getText());


        playerHand.draw(playdeck);
        System.out.println("The card you drew was: " + playerHand.get(playerHand.size() - 1).toString());
        if (this.playerHand.getValue() > 21) {
            System.out.println("Bust. Busted at: " + playerHand.getValue());
            playerFunds -= playerBet;
            stand(scene, true);
        } else if (this.playerHand.getValue() == 21 && playerHand.size() == 2) {
            if (this.playerHand.get(0).getValue() == Value.ACE || playerHand.get(1).getValue() == Value.ACE) {
                System.out.println("Blackjack! Here's your money!");
                playerFunds += playerBet * (3 / 2);
            }
        }
    }

    public void stand(Scene scene, boolean endRound) {
        TextField betTF = (TextField) scene.lookup("#money");
        double playerBet = Integer.parseInt(betTF.getText());
        if (endRound) {
            return;
        }
        
        if ((dealerHand.getValue() > playerHand.getValue()) && endRound == false) {
            System.out.println("Dealer beats you " + dealerHand.getValue() + " to " + playerHand.getValue());
            playerFunds -= playerBet;
            endRound = true;
        }
        // Dealer hits < 17
        while ((dealerHand.getValue() < 17) && endRound == false) {
            dealerHand.draw(playdeck);
            System.out.println("Dealer draws: " + dealerHand.get(dealerHand.size() - 1).toString());
        }

        System.out.println("Dealer hand value: " + dealerHand.getValue());

        if ((dealerHand.getValue() > 21) && endRound == false) {
            System.out.println("Dealer has busted. You win!");
            playerFunds += playerBet;
            endRound = true;
        }

        if ((dealerHand.getValue() == playerHand.getValue()) && endRound == false) {
            System.out.println("Push.");
            endRound = true;
        }

        if ((playerHand.getValue() > dealerHand.getValue()) && endRound == false) {
            System.out.println("You win the hand.");
            playerFunds += playerBet;
            endRound = true;
        } else if (endRound == false) // dealer wins
        {
            System.out.println("Dealer wins.");
            playerFunds -= playerBet;
        }

        playerHand.emptyDeck(playdeck);
        dealerHand.emptyDeck(playdeck);
        System.out.println("End of this round.");

    }

    private void drawHands(Deck playerCards, Deck dealerCards, Scene scene,
            boolean reveal)
            throws FileNotFoundException {
        HBox dealerHand = (HBox) scene.lookup("#dealerHand");
        HBox playerHand = (HBox) scene.lookup("#playerHand");

        ArrayList<String> playerFileNames = new ArrayList<>();
        for (Card c : playerCards) {
            playerFileNames.add(c.getImgString());
        }
        ArrayList<String> dealerFileNames = new ArrayList<>();
        for (Card c : dealerCards) {
            dealerFileNames.add(c.getImgString());
        }

        for (String s : playerFileNames) {
            FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + s);
            ImageView cardView = new ImageView();
            Image card = new Image(inputStream);
            cardView.setImage(card);
            cardView.setFitHeight(250);
            cardView.setFitWidth(175);
            playerHand.getChildren().addAll(cardView);
            
        }

        if (!reveal) {

            String s = dealerFileNames.get(0);
            FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + s);
            ImageView cardView = new ImageView();
            Image card = new Image(inputStream);
            cardView.setImage(card);
            cardView.setFitHeight(250);
            cardView.setFitWidth(175);
            dealerHand.getChildren().addAll(cardView);
            FileInputStream inputstream2 = new FileInputStream(Tools.getClasspathDir() + "images/cards/blue_back.png");
            ImageView cardBackView = new ImageView();
            Image cardBack = new Image (inputstream2);
            cardBackView.setImage(cardBack);
            cardBackView.setFitHeight(250);
            cardBackView.setFitWidth(175);
            dealerHand.getChildren().addAll(cardBackView);
                
        }else {
            dealerHand.getChildren().remove(1);
            for (String s : dealerFileNames) {
                FileInputStream inputStream = new FileInputStream(Tools.getClasspathDir() + "images/cards/" + s);
                ImageView cardView = new ImageView();
                Image card = new Image(inputStream);
                cardView.setImage(card);
                cardView.setFitHeight(250);
                cardView.setFitWidth(175);
                dealerHand.getChildren().addAll(cardView);
            }
        }

        
    }


}