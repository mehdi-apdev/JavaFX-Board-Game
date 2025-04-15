package unit;

import models.Game;
import models.Player;
import models.SpaceMalus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceMalusTest {

    @Test
    void testConstructorAndGetSetId() {
        // Create a SpaceMalus with ID 12
        SpaceMalus space = new SpaceMalus(4, 2, 12);

        // Check that the initial ID is correct
        assertEquals(12, space.getId());

        // Change the ID and verify the new value
        space.setId(88);
        assertEquals(88, space.getId());
    }

    @Test
    void testApplyEffectMovesPlayerBackwards() {
        // Create a player and move them forward to position 5
        Player player = new Player("Test Player");
        player.move(5);
        assertEquals(5, player.getPosition());

        // Create a SpaceMalus with malus = 3
        SpaceMalus space = new SpaceMalus(2, 1, 99);

        // Use a dummy game instance (not used in logic)
        Game game = new Game(java.util.List.of(player));

        // Apply the malus effect
        space.applyEffect(player, game);

        // Verify that the player moved back by 3 (5 - 3 = 2)
        assertEquals(2, player.getPosition());
    }

    @Test
    void testApplyEffectDoesNotGoBelowZero() {
        // Player at position 0
        Player player = new Player("Test Player");

        // SpaceMalus applies -3
        SpaceMalus space = new SpaceMalus(1, 1, 33);
        Game game = new Game(java.util.List.of(player));

        // Apply malus
        space.applyEffect(player, game);

        // Position should stay at 0 (not negative)
        assertEquals(0, player.getPosition());
    }
}

