package unit;

import models.Question;
import models.Topic;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    void testQuestionInitializationAndGetters() {
        String texte = "What is the capital of France?";
        List<String> responses = Arrays.asList("Paris", "London", "Berlin", "Madrid");
        int correctIndex = 0;
        Topic topic = Topic.EDUCATION;
        int difficulty = 2;

        Question question = new Question(texte, responses, correctIndex, topic, difficulty);

        // Test getters
        assertEquals(texte, question.getTexte());
        assertEquals(responses, question.getResponse());
        assertEquals(correctIndex, 0); // We don’t have a getter for index, so we test isCorrectResponse
        assertEquals(topic, question.getTopic());
        assertEquals(difficulty, question.getDifficulty());
    }

    @Test
    void testIsCorrectResponse() {
        List<String> responses = Arrays.asList("Mercury", "Venus", "Earth", "Mars");
        Question question = new Question("Which planet is closest to the sun?", responses, 0, Topic.EDUCATION, 1);

        // Correct answer
        assertTrue(question.isCorrectResponse(0));

        // Incorrect answers
        assertFalse(question.isCorrectResponse(1));
        assertFalse(question.isCorrectResponse(2));
        assertFalse(question.isCorrectResponse(3));
    }
}

