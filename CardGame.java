import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CardGame {
    private final List<Player> players;
    private final List<CardDeck> decks;
    private final List<Card> pack;
    private final int numPlayers;

    public CardGame(int numPlayers, String packFilePath) {
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>(numPlayers);
        this.decks = new ArrayList <>(numPlayers);
        this.pack = new ArrayList<>();
        loadPack(packFilePath);
        if (!validatePack()) {
            throw new IllegalArgumentException("Invalid pack size, must be exactly" + (8 * numPlayers));
        }
        setupGame();
    }

    /** loads the pack of cards from the input file 
     * @param packFilePath is path to the card pack file
     */
    public void loadPack(String packFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(packFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int cardValue = Integer.parseInt(line.trim());
                pack.add(new Card(cardValue));
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error loading card pack: " + e.getMessage());
        }
    }

    /** check if pack is valid size */
    public boolean validatePack() {
        return pack.size() == 8 * numPlayers;
    }

    /** setup game. Create players, decks and distribute cards 
    */

    private void setupGame() {
        // Create decks
        for (int i = 0; i < numPlayers; i++) {
            decks.add(new CardDeck(i));
        }

        // Create Players
        for (int i = 0; i < numPlayers; i++) {
            CardDeck leftDeck = decks.get(i);
            CardDeck rightDeck = decks.get((i + 1) % numPlayers);
            players.add(new Player(i+1, leftDeck, rightDeck));

        }

        // Distribute cards
        distributeCards();
    }

}
