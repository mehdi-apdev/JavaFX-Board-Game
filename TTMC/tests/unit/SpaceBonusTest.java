package unit;

import models.Game;
import models.Player;
import models.SpaceBonus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceBonusTest {

    @Test
    void testConstructorAndGetSetId() {
        // Create a SpaceBonus with initial id 10
        SpaceBonus space = new SpaceBonus(3, 1, 10);

        // Check the initial ID
        assertEquals(10, space.getId());

        // Change the ID and verify
        space.setId(25);
        assertEquals(25, space.getId());
    }

    @Test
    void testApplyEffectMovesPlayer() {
        // Create a player at position 0
        Player player = new Player("Test Player");

        // Create a SpaceBonus with bonus = 2
        SpaceBonus space = new SpaceBonus(1, 1, 5);

        // Use a dummy Game instance
        Game game = new Game(java.util.List.of(player));

        // Apply the bonus effect
        space.applyEffect(player, game);

        // Verify that the player moved forward by 2
        assertEquals(2, player.getPosition());
    }
}
