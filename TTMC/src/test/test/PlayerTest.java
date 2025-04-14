package test;
import models.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testConstructorAndGetters() {
        Player player = new Player("Alice");
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getPosition());
        assertEquals(0, player.getScore());
        assertEquals(3, player.getHint());
        assertFalse(player.hasUsedHintThisRound());
    }

    @Test
    void testSetNameValidAndInvalid() {
        Player player = new Player("Bob");
        assertEquals("Bob", player.getName());

        assertThrows(IllegalArgumentException.class, () -> player.setName(""));
        assertThrows(IllegalArgumentException.class, () -> player.setName(null));
    }

    @Test
    void testMove() {
        Player player = new Player("Charlie");
        player.move(5);
        assertEquals(5, player.getPosition());

        player.move(-10); // Should not go below 0
        assertEquals(0, player.getPosition());
    }

    @Test
    void testIncreaseScore() {
        Player player = new Player("Dan");
        player.increaseScore(10);
        assertEquals(10, player.getScore());

        assertThrows(IllegalArgumentException.class, () -> player.increaseScore(-5));
    }

    @Test
    void testDecreaseScore() {
        Player player = new Player("Eve");
        player.increaseScore(10);
        player.decreaseScore(5);
        assertEquals(5, player.getScore());

        player.decreaseScore(10); // Should not go below 0
        assertEquals(0, player.getScore());

        assertThrows(IllegalArgumentException.class, () -> player.decreaseScore(-5));
    }

    @Test
    void testAverageScore() {
        Player player = new Player("Test Player");

        // No score has been set yet, so average should be 0
        assertEquals(0, player.averageScore());

        // First score of 100
        player.setScore(100);

        // Average should be 100
        assertEquals(100, player.averageScore());

        // Second score of 50
        player.setScore(50);

        // Average should be (100 + 50) / 2 = 75
        assertEquals(75, player.averageScore());

        // Third score of 150
        player.setScore(150);

        // Average should be (100 + 50 + 150) / 3 = 100
        assertEquals(100, player.averageScore());
    }

    @Test
    void testUseHint() {
        Player player = new Player("Grace");
        player.useHint();
        assertEquals(2, player.getHint());

        player.useHint();
        player.useHint();
        assertEquals(0, player.getHint());

        assertThrows(IllegalStateException.class, player::useHint);
    }

    @Test
    void testStreakLogic() {
        Player player = new Player("Hugo");
        assertEquals(0, player.getStreak());

        player.increaseStreak();
        player.increaseStreak();
        player.increaseStreak();
        assertEquals(3, player.getStreak());
        assertTrue(player.hasThreeStreaks());

        player.resetStreak();
        assertEquals(0, player.getStreak());
        assertFalse(player.hasThreeStreaks());
    }

    @Test
    void testSetScoreValidAndInvalid() {
        Player player = new Player("Ivy");
        player.setScore(15);
        assertEquals(15, player.getScore());

        assertThrows(IllegalArgumentException.class, () -> player.setScore(-1));
    }

    @Test
    void testHintUsedThisRound() {
        Player player = new Player("Jack");
        assertFalse(player.hasUsedHintThisRound());

        player.setUsedHintThisRound(true);
        assertTrue(player.hasUsedHintThisRound());
    }
}
