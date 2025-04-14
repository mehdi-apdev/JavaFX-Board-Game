import org.junit.jupiter.api.Test;
import models.InformaticsQuestionFactory;
import models.Question;
import models.QuestionFactory;
import models.Topic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InformaticsQuestionFactoryTest extends AbstractQuestionFactoryTest {

    @Override
    protected QuestionFactory getFactory() {
        return new InformaticsQuestionFactory();
    }

    @Override
    protected Topic getExpectedTopic() {
        return Topic.INFORMATICS;
    }

    @Test
    void testCreateQuestion() {
        // Run common tests for creating the question
        runCommonTests();
    }

    // Test for the getQuestions method to ensure it returns the correct list of questions
    @Test
    void testGetQuestions() {
        InformaticsQuestionFactory factory = new InformaticsQuestionFactory();

        // Create some questions
        String texte1 = "What does CPU stand for?";
        List<String> responses1 = List.of("Central Processing Unit", "Computer Processing Unit", "Central Program Unit", "Central Processor Unit");
        int correctIndex1 = 0;
        int difficulty1 = 2;
        factory.createQuestion(texte1, responses1, correctIndex1, difficulty1);

        String texte2 = "What is the largest programming language by usage?";
        List<String> responses2 = List.of("Java", "Python", "C", "JavaScript");
        int correctIndex2 = 3;
        int difficulty2 = 1;
        factory.createQuestion(texte2, responses2, correctIndex2, difficulty2);

        // Retrieve the list of questions
        List<Question> questions = factory.getQuestions();

        // Assert that the list contains exactly 2 questions
        assertEquals(2, questions.size());

        // Assert that the questions are the ones that were created
        assertEquals(texte1, questions.get(0).getTexte());
        assertEquals(responses1, questions.get(0).getResponse());
        assertEquals(Topic.INFORMATICS, questions.get(0).getTopic());
        assertEquals(difficulty1, questions.get(0).getDifficulty());

        assertEquals(texte2, questions.get(1).getTexte());
        assertEquals(responses2, questions.get(1).getResponse());
        assertEquals(Topic.INFORMATICS, questions.get(1).getTopic());
        assertEquals(difficulty2, questions.get(1).getDifficulty());
    }
}
