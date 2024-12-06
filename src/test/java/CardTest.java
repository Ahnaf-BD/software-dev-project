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
    // Runs after tests
    public static void tearDownClass() {
        card = null;
    }

    @Test
    // Testing getValue
    public void testGetValue() {
        assertEquals("Card value should be 1,", 1, card.getValue());
    }

    @Test
    // Testing toString 
    public void testToString() {
        assertEquals("String should match integer value,", "1", card.toString());
    }

    @Test
    public void testConstructorWithNegative() {
        // Test to see if constructor throws error on negative value
        assertThrows(IllegalArgumentException.class, () -> new Card(-1));
    }
}
