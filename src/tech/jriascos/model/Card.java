package tech.jriascos.model;

public class Card implements Comparable<Card> {

    private Suit suit;
    private Value value;
    
    public Card (Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public String toString () {
        return this.value.toString() + " of " + this.suit.toString() + "S";
    }

    public Value getValue() {
        return this.value;
    }

    @Override
    public int compareTo(Card c) {
        return this.getValue().compareTo(c.getValue());
    }
}