package src.main.java;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CardDeck {
    private final int deckID;
    private final LinkedBlockingQueue<Card> cards;
    private final ReentrantLock lock;

    public CardDeck(int deckId) {
        this.deckID = deckId;
        this.cards = new LinkedBlockingQueue<>();
        this.lock = new ReentrantLock();

    }

    public synchronized void addCardToDeck(Card card) {
        try {
            lock.lock();
            cards.offer(card); // .offer() adds the element to the queue
        } finally {
            lock.unlock();
        }
    }

    public synchronized Card drawCard() {
        try {
            lock.lock();
            return cards.poll(); // .poll() removes and returns the head of the queue
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        try {
            lock.lock();
            return cards.size(); // returns num of cards in the deck
        } finally {
            lock.unlock();
        }
    }

    public int getDeckID() {
        return deckID; // returns ID of the deck
    }

    public void writeDeckContentsToFile(String filename) { // filename will be entered when method called e.g. player1_output.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.format("deck%d contents ", this.deckID + 1));
            for (Card card: cards) {
                writer.write(card.toString() + " ");

            }

        } catch (IOException e) {
            System.err.println("Error writing deck contents to file: " + e.getMessage());
        }
    }
}  
