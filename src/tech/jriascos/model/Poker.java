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
    private int playerBet;
    private int secondPlayerBet;

    public Poker() {
        Deck newplaydeck = new Deck();
        newplaydeck.createFullDeck();
        newplaydeck.shuffle();
        this.playdeck = newplaydeck;
        this.playerHand = new Deck();
        this.tableCards = new Deck();
        this.playerFunds = 1000;
        this.finished = true;
        this.secondBetTurn = false;
        this.trades = 3;
        this.playerBet = 0;
        this.secondPlayerBet = 0;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public double getPlayerFunds() {
        return this.playerFunds;
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
        this.playerBet = 0;
        clearHands(scene);
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        Label betTF = (Label) scene.lookup("#money");
        HBox playButtons = (HBox) scene.lookup("#playButtons");
        Label playLog = (Label) scene.lookup("#playLog");
        Button confirmTrades = (Button) scene.lookup("#confirmTrades");
        playLog.setText("");
        fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet: " + this.playerBet);
        

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
            this.playerBet = Integer.parseInt(betTF.getText());
            if (this.playerBet > this.playerFunds) {
                playLog.setText(playLog.getText() + "\nYou can't bet more than your current balance.");
                return;
            }
        }
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);
        this.playerHand.draw(this.playdeck);

        this.playerFunds -= this.playerBet;
        fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet: " + this.playerBet);

        this.secondBetTurn = true;

        drawHands(scene);

        playLog.setText(playLog.getText() + "\nYou can now bet again, increasing or decreasing the second bet.\nIf so, enter another amount.\nIf not, then simply hit confirm bet again.");

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
        Label betTF = (Label) scene.lookup("#money");
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        Button confirmTrades = (Button) scene.lookup("#confirmTrades");
        playLog.setText("Now you check the boxes under the cards you want to trade.\nIt is 3 trades if you don't have an ace,\n4 if you do.");
        this.secondPlayerBet = Integer.parseInt(betTF.getText());

        if (this.secondPlayerBet > this.playerFunds) {
            confirmTrades.setDisable(true);
            playLog.setText(playLog.getText() + "\nYou can't bet more than your current balance.");
            return;
        }
        confirmTrades.setDisable(false);
        this.playerFunds -= this.secondPlayerBet;
        fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet: " + (this.playerBet + this.secondPlayerBet));

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
        int repetitions = 0;
        int pairs = 0;
        int trios = 0;
        int aces = 0;

        Deck tempArray = this.playerHand;

        Collections.sort(tempArray);

        for (int i = 1; i < 5; i++) {
            if (tempArray.get(i).getValue().ordinal() == tempArray.get(i-1).getValue().ordinal()) {
                if (repetitions == 0) {
                    repetitions += 2;
                }else {
                    repetitions++;
                }
                if (repetitions == 2) {
                    pairs++;
                }
                if (repetitions == 3) {
                    trios++;
                    pairs--;
                }
            }else {
                repetitions = 0;
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
            if(tempArray.get(i).getValue() == Value.ACE && tempArray.get(i-1).getValue() == Value.FIVE) {
                continue;
            }
            if (tempArray.get(i).getValue().ordinal() < tempArray.get(i-1).getValue().ordinal()) {
                straight = false;
            }else if (tempArray.get(i-1).getValue().ordinal() + 1 != tempArray.get(i).getValue().ordinal()) {
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

        System.out.println(tempArray);
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

        if(royalflush) {
            payout("rflush", scene);
        }else if(straightflush) {
            payout("sflush", scene);
        }else if (fourofakind) {
            payout("4ofakind", scene);
        }else if (fullhouse) {
            payout("fullhouse", scene);
        }else if (flush) {
            payout("flush", scene);
        }else if (straight) {
            payout("straight", scene);
        }else if (threeofakind) {
            payout("3ofakind", scene);
        }else if (twopair) {
            payout("2pair", scene);
        }else if (pair) {
            payout("pair", scene);
        }else {
            payout("fail", scene);
        }

        this.finished = true;
        this.playerHand.emptyDeck(playdeck);
    }

    private void payout(String string, Scene scene) {
        Label playLog = (Label) scene.lookup("#playLog");
        int totalBet = this.playerBet + this.secondPlayerBet;
        Label fundsDisplay = (Label) scene.lookup("#fundsDisplay");
        switch(string){
            case "rflush":
                playLog.setText("You got a royal flush! Damn! Here's your money.");
                this.playerFunds += totalBet * 250 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "sflush":
                playLog.setText("You got a straight flush! Nice!");
                this.playerFunds += totalBet * 100 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "4ofakind":
                playLog.setText("You got a 4 of a kind? Awesome!");
                this.playerFunds+= totalBet * 25 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "fullhouse":
                playLog.setText("Is that a full house? Well done!");
                this.playerFunds+= totalBet * 10 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "flush":
                playLog.setText("Flush? Nice hand, here's your money.");
                this.playerFunds+= totalBet * 5 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "straight":
                playLog.setText("Straight? Good hand.");
                this.playerFunds+= totalBet * 3 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "3ofakind":
                playLog.setText("3 of a kind? Nice.");
                this.playerFunds+= this.playerBet * 2 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "2pair":
                playLog.setText("2 pair is a good hand. Here's the payout:");
                this.playerFunds+= totalBet * 1 + totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            case "pair":
                playLog.setText("Better than nothing. Here's the bet back.");
                this.playerFunds+= totalBet;
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break;
            default:
                playLog.setText("Sorry, that's not a strong enough hand. You lose: " + totalBet);
                fundsDisplay.setText("Funds: "+ Double.toString(this.playerFunds) + " | Bet Was: " + totalBet);
                break; 

        }   
    }

}