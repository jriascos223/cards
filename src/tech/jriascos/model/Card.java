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

    public Suit getSuit() {
        return this.suit;
    }

    public String getImgString() {
        String suit = "";
        String value = "";
        switch(this.suit) {
            case CLUB:
                suit = "C";
                break;
            case SPADE:
                suit = "S";
                break;
            case DIAMOND:
                suit = "D";
                break;
            case HEART:
                suit = "H";
                break;
        } 

        switch(this.value) {
            case TWO: value = "2"; break;
            case THREE: value = "3"; break;
            case FOUR: value = "4"; break;
            case FIVE: value = "5"; break;
            case SIX: value = "6"; break;
            case SEVEN: value = "7"; break;
            case EIGHT: value = "8"; break;
            case NINE: value = "9"; break;
            case TEN: value = "10"; break;
            case JACK: value = "J"; break;
            case QUEEN: value = "Q"; break;
            case KING: value = "K"; break;
            case ACE: value = "A"; break;
        }

        return value + suit + ".png";
    }

    @Override
    public int compareTo(Card c) {
        return this.getValue().compareTo(c.getValue());
    }
}