import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class Player implements Runnable {
    // Class fields
    private final int playerID;
    private final List<Card> hand;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private final AtomicBoolean hasWon;
    private final AtomicBoolean gameEnded;
    private final String outputFile;
    private Thread gameThread;
    private final Random random;
    private final Object handLock;

    /**
     * Constructor for Player class
     * @param playerID The ID of the player
     * @param leftDeck The deck to draw from
     * @param rightDeck The deck to discard to
     */
    public Player(int playerID, CardDeck leftDeck, CardDeck rightDeck) {
        this.playerID = playerID;
        this.hand = new ArrayList<>(4); // Initialize with capacity of 4
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hasWon = new AtomicBoolean(false);
        this.gameEnded = new AtomicBoolean(false);
        this.outputFile = String.format("player%d_output.txt", playerID);
        this.random = new Random();
        this.handLock = new Object();
    }

    /**
     * Adds a card to the player's hand during initial deal
     * @param card The card to add
     */
    public void addInitialCard(Card card) {
        synchronized(handLock) {
            if (hand.size() < 4) {
                hand.add(card);
            }
        }
    }

    /**
     * Writes a message to the player's output file
     * @param message The message to write
     */
    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file for player " + playerID + ": " + e.getMessage());
        }
    }

    /**
     * Checks if the current hand is a winning hand
     * @return true if hand is winning, false otherwise
     */
    private boolean checkWinningHand() {
        synchronized(handLock) {
            if (hand.size() == 4) {
                int firstValue = hand.get(0).getValue();
                for (Card card : hand) {
                    if (card.getValue() != firstValue) {
                        return false;
                    }
                }
                // Winning hand found
                hasWon.set(true);
                writeToFile("player " + playerID + " wins");
                System.out.println("player " + playerID + " wins");
                writeToFile("player " + playerID + " exits");
                writeToFile("player " + playerID + " final hand: " + getHandString());
                gameEnded.set(true);
                return true;
            }
            return false;
        }
    }

    /**
     * Chooses a card to discard based on game rules
     * @return The card to discard
     */
    private Card chooseCardToDiscard() {
        synchronized(handLock) {
            // Find all non-preferred cards
            List<Card> nonPreferredCards = new ArrayList<>();
            for (Card card : hand) {
                if (card.getValue() % playerID != 0) {
                    nonPreferredCards.add(card);
                }
            }
            
            // If all cards are preferred, randomly choose one
            if (nonPreferredCards.isEmpty()) {
                return hand.get(random.nextInt(hand.size()));
            }
            
            // Randomly choose from non-preferred cards
            return nonPreferredCards.get(random.nextInt(nonPreferredCards.size()));
        }
    }

    /**
     * Gets the current hand as a string
     * @return String representation of the hand
     */
    private String getHandString() {
        synchronized(handLock) {
            StringBuilder sb = new StringBuilder();
            for (Card card : hand) {
                sb.append(card.getValue()).append(" ");
            }
            return sb.toString().trim();
        }
    }

    /**
     * Writes the initial hand to the output file
     */
    private void writeInitialHand() {
        writeToFile("player " + playerID + " initial hand " + getHandString());
    }

    @Override
    public void run() {
        gameThread = Thread.currentThread();
        writeInitialHand();
        
        if (checkWinningHand()) {
            return;
        }

        while (!gameEnded.get()) {
            try {
                // Draw and discard as an atomic action
                synchronized(handLock) {
                    Card drawnCard = leftDeck.drawCard();
                    if (drawnCard == null) {
                        continue;  // If deck is empty, try again
                    }
                    
                    writeToFile(String.format("player %d draws a %d from deck %d", 
                        playerID, drawnCard.getValue(), leftDeck.getDeckID() + 1));
                    
                    Card cardToDiscard = chooseCardToDiscard();
                    hand.remove(cardToDiscard);
                    hand.add(drawnCard);
                    
                    rightDeck.addCardToDeck(cardToDiscard);
                    writeToFile(String.format("player %d discards a %d to deck %d", 
                        playerID, cardToDiscard.getValue(), rightDeck.getDeckID()));
                    writeToFile("player " + playerID + " current hand is " + getHandString());
                    
                    if (checkWinningHand()) {
                        return;
                    }
                }
                
                Thread.sleep(10);  // Prevent CPU overload
                
            } catch (InterruptedException e) {
                if (gameEnded.get()) {
                    writeToFile("player " + playerID + " has been informed that another player has won");
                    writeToFile("player " + playerID + " exits");
                    writeToFile("player " + playerID + " hand: " + getHandString());
                    return;
                }
            }
        }
    }

    /**
     * Notifies the player that the game has ended
     */
    public void notifyGameEnd() {
        gameEnded.set(true);
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    /**
     * @return true if player has won, false otherwise
     */
    public boolean hasWon() {
        return hasWon.get();
    }

    /**
     * @return The player's ID
     */
    public int getPlayerID() {
        return playerID;
    }
}