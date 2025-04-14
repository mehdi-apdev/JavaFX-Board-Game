package unit;
import org.junit.jupiter.api.Test;
import models.EducationQuestionFactory;
import models.Question;
import models.QuestionFactory;
import models.Topic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EducationQuestionFactoryTest extends AbstractQuestionFactoryTest {

    @Override
    protected QuestionFactory getFactory() {
        return new EducationQuestionFactory();
    }

    @Override
    protected Topic getExpectedTopic() {
        return Topic.EDUCATION;
    }

    @Test
    void testCreateQuestion() {
        // Run common tests for creating the question
        runCommonTests();
    }

    // Test for the getQuestions method to ensure it returns the correct list of questions
    @Test
    void testGetQuestions() {
        EducationQuestionFactory factory = new EducationQuestionFactory();

        // Create some questions
        String texte1 = "What is the capital of France?";
        List<String> responses1 = List.of("Paris", "London", "Berlin", "Madrid");
        int correctIndex1 = 0;
        int difficulty1 = 2;
        factory.createQuestion(texte1, responses1, correctIndex1, difficulty1);

        String texte2 = "What is 2 + 2?";
        List<String> responses2 = List.of("3", "4", "5", "6");
        int correctIndex2 = 1;
        int difficulty2 = 1;
        factory.createQuestion(texte2, responses2, correctIndex2, difficulty2);

        // Retrieve the list of questions
        List<Question> questions = factory.getQuestions();

        // Assert that the list contains exactly 2 questions
        assertEquals(2, questions.size());

        // Assert that the questions are the ones that were created
        assertEquals(texte1, questions.get(0).getTexte());
        assertEquals(responses1, questions.get(0).getResponse());
        assertEquals(Topic.EDUCATION, questions.get(0).getTopic());
        assertEquals(difficulty1, questions.get(0).getDifficulty());

        assertEquals(texte2, questions.get(1).getTexte());
        assertEquals(responses2, questions.get(1).getResponse());
        assertEquals(Topic.EDUCATION, questions.get(1).getTopic());
        assertEquals(difficulty2, questions.get(1).getDifficulty());
    }
}
