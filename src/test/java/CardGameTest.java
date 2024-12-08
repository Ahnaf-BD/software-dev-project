package src.test.java;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import src.main.java.CardDeck;
import src.main.java.CardGame;
import src.main.java.Player;

public class CardGameTest {

    private CardGame cardGame;
    

    @Before
    public void setUp() {
        // Runs before each test
        int numPlayers = 2;
        String mockPackPath = "C:/Uni/ECM2414 Coursework/software-dev-project/src/test/java/mockPack.txt";

        cardGame = new CardGame(numPlayers, mockPackPath);
    }

    @Test
    public void testCardGameCreation() {
        // Test for successful game creation
        assertNotNull("CardGame should be successfully created", cardGame);
        assertEquals("Number of players should match", 2, cardGame.getPlayers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPackSize() {
        /// Test for invalid pack size (must be 8 * numPlayers cards)
        String invalidPackPath = "C:/Uni/ECM2414 Coursework/software-dev-project/src/test/java/invalidPackSize.txt";
        cardGame = new CardGame(2, invalidPackPath);
    }

    @Test
    public void testDistributeCards() {
        // Test for distributing cards to players
        cardGame.loadPack("C:/Uni/ECM2414 Coursework/software-dev-project/src/test/java/mockPack.txt");
        cardGame.distributeCards();

        for (Player player : cardGame.getPlayers()) {
            assertEquals("Player should have 4 cards", 4, player.getPlayerHand().size());
        }

        for (CardDeck deck : cardGame.getDecks()) {
            assertEquals("Deck should have 8 cards", 8, deck.getSize());
        }
    }

    @Test
    public void testStartGame() {
        // Test for starting the game and verifying thread creation
        cardGame.startGame();
        for (Player player : cardGame.getPlayers()) {
            assertNotNull("Player thread should be created", player.getThread());
        }
    }

    @Test
    public void testGameEnd() {
        // Test for game ending by simulating a player winning
        Player winningPlayer = cardGame.getPlayers().get(0);
        winningPlayer.setWinningHand(true);
        cardGame.notifyAllPlayers(winningPlayer.getPlayerID());
        assertTrue("Game should end when a player wins", winningPlayer.hasWon());
    }
}