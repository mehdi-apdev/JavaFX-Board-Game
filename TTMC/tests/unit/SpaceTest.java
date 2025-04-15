package unit;

import models.Game;
import models.Player;
import models.Space;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceTest {

    // Minimal subclass of Space for testing purposes
    static class TestSpace extends Space {
        public TestSpace(int number, int road, int id) {
            super(number, road, id);
        }

        // Dummy implementation of the abstract method
        @Override
        public void applyEffect(Player player, Game game) {
            // No effect for test
        }
    }

    @Test
    void testGetAndSetId() {
        // Create a test space with a specific ID
        TestSpace space = new TestSpace(5, 2, 42);

        // Check that the initial ID is set correctly
        assertEquals(42, space.getId());

        // Change the ID and verify the new value
        space.setId(99);
        assertEquals(99, space.getId());
    }
}
