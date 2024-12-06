package src.test.java;

import src.main.java.Card;
import src.main.java.CardDeck;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.beans.Transient;

public class CardDeckTest {

    private static CardDeck deck;
    private static Card card1;
    private static Card card2;

    @BeforeClass
    public static void setUpClass() {
        // Runs before tests
        deck = new CardDeck(0);
        card1 = new Card(1);
        card2 = new Card(2);
    }

    @AfterClass
    public static void tearDownClass() {
        // Runs after tests
        deck = null;
        card1 = null;
        card2 = null;
    }

    @Test
    public void testAddCardToDeck() {
        deck.addCardToDeck(card1);
        assertEquals("Deck size should be 1 after adding a card", 1, deck.getSize());
        deck.addCardToDeck(card2);
        assertEquals("Deck size should be 2 after adding another card", 2, deck.getSize());
    }


    
}
