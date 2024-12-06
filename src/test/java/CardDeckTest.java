package src.test.java;

import src.main.java.Card;
import src.main.java.CardDeck;
import org.junit.Before;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class CardDeckTest {
    private static CardDeck deck;
    private static Card card1;
    private static Card card2;

    @Before
    public void setUpClass() {
        // Run before each test
        deck = new CardDeck(0);
        card1 = new Card(1);
        card2 = new Card(2);
    }

    @Test
    public void testAddCardToDeck() {
        deck.addCardToDeck(card1);
        assertEquals("Deck size should be 1 after adding a card,", 1, deck.getSize());
        deck.addCardToDeck(card2);
        assertEquals("Deck size should be 2 after adding another card,", 2, deck.getSize());
    }

    @Test
    public void testDrawCard() {
        deck.addCardToDeck(card1);
        deck.addCardToDeck(card2);

        Card drawnCard = deck.drawCard();
        assertEquals("The drawn card should match the first card added,", card1, drawnCard);
        assertEquals("Deck size should decrease by 1,", 1, deck.getSize());
        
        drawnCard = deck.drawCard();
        assertEquals("The drawn card should match the second card added,", card2, drawnCard);
        assertEquals("Deck size should now be 0,", 0, deck.getSize());

        assertNull("Drawing from an empty deck should return null,", deck.drawCard());
    }

    @Test
    public void testGetSize() {
        assertEquals("Deck size should be 0 initially,", 0, deck.getSize());

        deck.addCardToDeck(card1);
        assertEquals("Deck size should now be 1,", 1, deck.getSize());

        deck.addCardToDeck(card2);
        assertEquals("Deck size should now be 2,", 2, deck.getSize());

        deck.drawCard();
        assertEquals("Deck size should reduce back to 1,", 1, deck.getSize());
    }
    
   @Test
   public void testGetDeckID() {
        assertEquals("Deck ID should be 0,", 0, deck.getDeckID());
   }

   @Test
   public void testWriteDeckContentsToFile() throws FileNotFoundException, IOException {
        deck.addCardToDeck(card1);
        deck.addCardToDeck(card2);

        String filename = "test_output.txt";
        deck.writeDeckContentsToFile(filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            assertNotNull("File should not be empty", line);
            assertTrue("File should include correct initial deck ID,", line.contains("deck0 contents"));
            assertTrue("File should include the first card,", line.contains(card1.toString()));
            assertTrue("File should include the second card,", line.contains(card2.toString()));
        }
        finally {
            new File(filename).delete();
        }
   }
}
