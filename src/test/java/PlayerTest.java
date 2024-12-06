package src.test.java;

import org.junit.BeforeClass;
import org.junit.Test;
import src.main.java.Card;
import src.main.java.CardDeck;
import src.main.java.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class PlayerTest {
    private static Player player;
    private static CardDeck leftDeck;
    private static CardDeck rightDeck;

    @BeforeClass
    public static void setUp() {
        leftDeck = new CardDeck(1);
        rightDeck = new CardDeck(2);
        player = new Player(1, leftDeck, rightDeck);
    }

    @Test
    public void testAddInitialCard() {
        // make cards
        Card card1 = new Card(1);
        Card card2 = new Card(2);

        // add cards to player hands
        player.addInitialCard(card1);
        player.addInitialCard(card2);

        List hand = player.getPlayerHand();
        assertEquals(2, hand.size());
        assertTrue(hand.contains(card1));
        assertTrue(hand.contains(card2));

    }

    @Test
    public void testCheckWinningHand() {
        // make winning hand 
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(2);
        Card card4 = new Card(2);

        // add cards 
        player.addInitialCard(card1);
        player.addInitialCard(card2);
        player.addInitialCard(card3);
        player.addInitialCard(card4);

        // run
        player.run();
        // check win boolean 
        assertTrue(player.hasWon());
    }

    @Test
    public void testNonWinningHand() {
        // make non winning hand 
        Card card1 = new Card(1);
        Card card2 = new Card(2);
        Card card3 = new Card(3);
        Card card4 = new Card(4);

        // add cards 
        player.addInitialCard(card1);
        player.addInitialCard(card2);
        player.addInitialCard(card3);
        player.addInitialCard(card4);

        // run
        player.run();
        // check win boolean 
        assertFalse(player.hasWon());
    }

    @Test 
    public void testChooseCardToDiscard() {
        // add cards to hand 
        Card preferred1 = new Card(1);
        Card preferred2 = new Card(1);
        Card preferred3 = new Card(1);
        Card notPreferred = new Card(3);
        
        player.addInitialCard(preferred1);
        player.addInitialCard(preferred2);
        player.addInitialCard(preferred3);
        player.addInitialCard(notPreferred);



        leftDeck.addCardToDeck(new Card(3));
        
        Card discarded = player.chooseCardToDiscard();
        System.out.println(discarded);

        assertTrue(discarded.getValue() == notPreferred.getValue());
    }

    @Test 
    public void testNotifyGameEnd() {
        // start a player thread
        Thread playerThread = new Thread(player);
        playerThread.start();

        player.notifyGameEnd();

        // see if notifyGameEnd stops thread
        assertTrue(playerThread.isInterrupted());
    }

    
   
    
}
