import models.Question;
import models.QuestionFactory;
import models.Topic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractQuestionFactoryTest {

    protected abstract QuestionFactory getFactory();

    protected abstract Topic getExpectedTopic();

    public void runCommonTests() {
        QuestionFactory factory = getFactory();

        String texte = "Question de test ?";
        List<String> responses = Arrays.asList("Réponse A", "Réponse B", "Réponse C", "Réponse D");
        int correctIndex = 1;
        int difficulty = 2;

        Question q = factory.createQuestion(texte, responses, correctIndex, difficulty);

        assertNotNull(q);
        assertEquals(texte, q.getTexte());
        assertEquals(responses, q.getResponse());
        assertEquals(getExpectedTopic(), q.getTopic());
        assertEquals(difficulty, q.getDifficulty());

        assertTrue(q.isCorrectResponse(correctIndex));
        assertFalse(q.isCorrectResponse(0));
    }
}
