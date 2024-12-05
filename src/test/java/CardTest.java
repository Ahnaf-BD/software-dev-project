package src.test.java;

import src.main.java.Card;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {
    private static Card card;

    @BeforeClass
    // Runs before tests
    public static void setUpClass() {
        card = new Card(1);
    }

    @AfterClass
    // runs after tests
    public static void tearDownClass() {
        card = null;
    }

    @Test
    // Testing getValue
    public void testGetValue() {
        assertEquals("Card value should be 1", 1, card.getValue());
    }

    @Test
    // Testing toString 
    public void testToString() {
        assertEquals("String should match", "1", card.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegative() {
        // test to see if constructor throws error on negative value
        new Card(-1);
    }
}
