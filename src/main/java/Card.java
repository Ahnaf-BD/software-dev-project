package src.main.java;
public class Card {
    private final int value;

    public Card(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Card value cannot be negative: " + value);
        }
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}