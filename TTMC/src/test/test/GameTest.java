package test;
import exception.NoPlayersInGameException;
import models.Game;
import models.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // Test the constructor and the initialization of players and the currentPlayerIndex
    @Test
    void testConstructor() {
        // Prepare players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        List<Player> players = Arrays.asList(player1, player2);

        // Create game instance
        Game game = new Game(players);

        // Assert that the players list is initialized correctly
        assertEquals(2, game.getPlayers().size());
        assertEquals(player1, game.getPlayers().get(0));  // Player 1 should be the first player

        // Assert that the current player index is initialized to 0
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    // Test the nextPlayer method to check if it correctly cycles through players
    @Test
    void testNextPlayer() {
        // Prepare players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        List<Player> players = Arrays.asList(player1, player2);

        // Create game instance
        Game game = new Game(players);

        // Check initial current player is Player 1
        assertEquals(player1, game.getCurrentPlayer());

        // Move to next player (should be Player 2)
        game.nextPlayer();
        assertEquals(player2, game.getCurrentPlayer());

        // Move to next player again (should wrap around and be Player 1)
        game.nextPlayer();
        assertEquals(player1, game.getCurrentPlayer());
    }

    // Test the getCurrentPlayer method to ensure it returns the correct player
    @Test
    void testGetCurrentPlayer() {
        // Prepare players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        List<Player> players = Arrays.asList(player1, player2);

        // Create game instance
        Game game = new Game(players);

        // Assert that the current player is Player 1 initially
        assertEquals(player1, game.getCurrentPlayer());
    }

    // Test the getCurrentPlayerIndex method to check the current player index
    @Test
    void testGetCurrentPlayerIndex() {
        // Prepare players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        List<Player> players = Arrays.asList(player1, player2);

        // Create game instance
        Game game = new Game(players);

        // Assert the initial player index is 0 (Player 1)
        assertEquals(0, game.getCurrentPlayerIndex());

        // Move to the next player and check index
        game.nextPlayer();
        assertEquals(1, game.getCurrentPlayerIndex());
    }

    // Test to check the behavior with an empty player list (edge case)
    @Test
    void testEmptyPlayerList() {
        Game game = new Game(Arrays.asList());

        // Assert that the player list is empty
        assertTrue(game.getPlayers().isEmpty());

        // Now assert that accessing current player or index throws the custom exception
        assertThrows(NoPlayersInGameException.class, game::getCurrentPlayer);
        assertThrows(NoPlayersInGameException.class, game::getCurrentPlayerIndex);
    }
}
