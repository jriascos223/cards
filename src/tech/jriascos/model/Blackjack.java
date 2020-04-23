package tech.jriascos.model;

import tech.jriascos.model.Deck;

import java.util.Scanner;

import tech.jriascos.model.Card;
import tech.jriascos.model.Suit;
import tech.jriascos.model.Value;

public class Blackjack {
    
    public static void startGame() {
        Deck playDeck = new Deck();
        playDeck.createFullDeck();
        playDeck.shuffle();

        Scanner input = new Scanner(System.in);

        Deck playerHand = new Deck();
        Deck dealerHand = new Deck();

        double playerFunds = 100.00;

        while(playerFunds > 0) {
            System.out.println("You have $" + playerFunds + ", how much are you willing to bet?");
            double playerBet = input.nextDouble();
            boolean endRound = false;
            if(playerBet > playerFunds) {
                System.out.println("You can't bet more than your current balance.");
                continue;
            }

            System.out.println("Dealing...");

            playerHand.draw(playDeck);
            playerHand.draw(playDeck);
            dealerHand.draw(playDeck);
            dealerHand.draw(playDeck);

            while(true) {
                System.out.println("Your Hand: " + playerHand.toString());
                System.out.println("Your hand's value is: " + playerHand.getValue());
                System.out.println("Dealer's hand: " + dealerHand.get(0).toString() + " and another surprise card!");
                System.out.println("Would you like to (1)Hit or (2)Stand");

                int response = input.nextInt();

                if (response == 1) {
                    playerHand.draw(playDeck);
                    System.out.println("The card you drew was: " + playerHand.get(playerHand.size()-1).toString());
                    if (playerHand.getValue() > 21) {
                        System.out.println("Bust. Busted at: " + playerHand.getValue());
                        playerFunds -= playerBet;
                        endRound = true;
                        break;
                    }else if (playerHand.getValue() == 21 && playerHand.size() == 2) {
                        if (playerHand.get(0).getValue() == Value.ACE || playerHand.get(1).getValue() == Value.ACE) {
                            System.out.println("Blackjack! Here's your money!");
                            playerFunds += playerBet * (3/2);
                        }
                    }
                }

                if (response == 2) {
                    break;
                }
    
            }

            System.out.println("Dealer Cards: " + dealerHand.toString());

            //Gotta check for different outcomes, I guess double down would go here.
            //Dealer wins
            if ((dealerHand.getValue() > playerHand.getValue()) && endRound == false) {
                System.out.println("Dealer beats you " + dealerHand.getValue() + " to " + playerHand.getValue());
                playerFunds -= playerBet;
                endRound = true;
            }
            //Dealer hits < 17
            while((dealerHand.getValue() < 17) && endRound == false) {
                dealerHand.draw(playDeck);
                System.out.println("Dealer draws: " + dealerHand.get(dealerHand.size()-1).toString());
            }

            System.out.println("Dealer hand value: " + dealerHand.getValue());

            if((dealerHand.getValue() > 21) && endRound == false) {
                System.out.println("Dealer has busted. You win!");
                playerFunds += playerBet;
                endRound = true;
            }

            if ((dealerHand.getValue() == playerHand.getValue()) && endRound == false) {
                System.out.println("Push.");
                endRound = true;
            }

            if((playerHand.getValue() > dealerHand.getValue()) && endRound == false){
				System.out.println("You win the hand.");
				playerFunds += playerBet;
				endRound = true;
			}
			else if(endRound == false) //dealer wins
			{
				System.out.println("Dealer wins.");
				playerFunds -= playerBet;
            }
            
            playerHand.emptyDeck(playDeck);
            dealerHand.emptyDeck(playDeck);
            System.out.println("End of this round.");

            
        }
        System.out.println("Goodbye!");

        input.close();
    }
}