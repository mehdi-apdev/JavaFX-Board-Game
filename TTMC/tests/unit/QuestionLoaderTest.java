package unit;

import models.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionLoaderTest {

    @Test
    void testConstructorAndGetFactories() {
        QuestionLoader loader = new QuestionLoader();
        Map<Topic, QuestionFactory> factories = loader.getFactories();

        // Check that all Topic values have a corresponding factory
        for (Topic topic : Topic.values()) {
            assertNotNull(factories.get(topic), "Factory should not be null for topic: " + topic);
        }
    }

    @Test
    void testLoadQuestionsFromJson() throws Exception {
        // JSON content to test with 1 EDUCATION question
        String jsonContent = """
        [
          {
            "theme_name": "education",
            "question": "What is the capital of France?",
            "priority": 2,
            "responses": ["Paris", "London", "Berlin", "Madrid"]
          },
          {
            "theme_name": "entertainment",
            "question": "Who directed Inception?",
            "priority": 1,
            "responses": ["Nolan", "Tarantino", "Spielberg", "Scorsese"]
          }
        ]
        """;

        // Create temp JSON file
        File tempFile = File.createTempFile("questions", ".json");
        tempFile.deleteOnExit(); // Delete file when test ends

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(jsonContent);
        }

        // Load questions
        QuestionLoader loader = new QuestionLoader();
        loader.loadQuestions(tempFile.getAbsolutePath());

        // Get factories and check loaded questions
        Map<Topic, QuestionFactory> factories = loader.getFactories();

        // EDUCATION
        List<Question> eduQuestions = factories.get(Topic.EDUCATION).getQuestions();
        assertEquals(1, eduQuestions.size());
        assertEquals("What is the capital of France?", eduQuestions.get(0).getTexte());

        // ENTERTAINMENT
        List<Question> entQuestions = factories.get(Topic.ENTERTAINMENT).getQuestions();
        assertEquals(1, entQuestions.size());
        assertEquals("Who directed Inception?", entQuestions.get(0).getTexte());
    }
}
