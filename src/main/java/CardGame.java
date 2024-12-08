package src.main.java;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
            throw new IllegalArgumentException("Invalid pack size, must be exactly " + (8 * numPlayers));
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
            int leftDeckIndex = i;
            int rightDeckIndex = ((i + 1) + numPlayers) % numPlayers;

            // System.out.println("Player " + (i) + ": leftDeck=" + leftDeckIndex + ", rightDeck=" + rightDeckIndex);   debugging purposes

            CardDeck leftDeck = decks.get(leftDeckIndex);
            CardDeck rightDeck = decks.get(rightDeckIndex);

            players.add(new Player(i, leftDeck, rightDeck));

        }

        // Distribute cards
        distributeCards();
    }

    /** distibutes cards to players and decks */
    public void distributeCards() {
        int cardIndex= 0;

        // 4 cards to each player
        for (int i = 0; i < 4; i++) {
            for (Player player : players) {
                player.addInitialCard(pack.get(cardIndex++));
            }
        }
        
        // give the rest of the cards to the decks
        for (int i = 0; i < numPlayers; i++) {
            CardDeck deck = decks.get(i);
            for (int j = 0; j < 4; j++) {
                deck.addCardToDeck(pack.get(cardIndex++));
            }
        }
    }

    /**
     * start game by starting all threads
     */
    public void startGame() {
        List<Thread> playerThreads = new ArrayList<>();
        for (Player player : players) {
            Thread thread = new Thread(player);
            playerThreads.add(thread);
            thread.start();
        }

        // wait for winner
        boolean gameEnded = false;
        while (!gameEnded) {
            for (Player player : players) {
                if (player.hasWon()) {
                    notifyAllPlayers(player.getPlayerID());
                    gameEnded = true;
                    break;
                }
            }
        }
        
        // join threads
        for (Thread thread : playerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Error waiting for player threads to finish: " + e.getMessage());
            }
        }

        // write final deck contents to file
        for (CardDeck deck : decks) {
            deck.writeDeckContentsToFile(String.format("deck%d_output.txt", deck.getDeckID()));
        }
    }

    /**
    * notifies all players that a player has won and ends game 
        * @param winningPlayerID ID of  winning player
        */

    public void notifyAllPlayers(int winningPlayerID) {
        System.out.println("Player " + winningPlayerID + " has won the game");
        for (Player player : players) {
            player.notifyGameEnd(winningPlayerID);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numPlayers;
        String packFilePath;

        if (args.length !=2) {
            System.out.print("Please enter the number of players: ");
            numPlayers = Integer.parseInt(scanner.nextLine());

            System.out.print("Please enter the location of the pack to load: ");
            packFilePath = scanner.nextLine();
        } else {
            numPlayers = Integer.parseInt(args[0]);
            packFilePath = args[1];
        }

        try {
            CardGame game = new CardGame(numPlayers, packFilePath);
            game.startGame();
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
        }

        scanner.close();
    }

}

    


