package unit;

import models.Question;
import models.QuestionCard;
import models.Topic;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionCardTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Question q1 = new Question(
                "What is the capital of France?",
                List.of("Paris", "London", "Berlin", "Madrid"),
                0,
                Topic.EDUCATION,
                2
        );

        Question q2 = new Question(
                "Who painted the Mona Lisa?",
                List.of("Da Vinci", "Picasso", "Van Gogh", "Monet"),
                0,
                Topic.EDUCATION,
                1
        );

        List<Question> questions = Arrays.asList(q1, q2);
        Topic theme = Topic.EDUCATION;

        // Act
        QuestionCard card = new QuestionCard(theme, questions);

        // Assert
        assertEquals(theme, card.getTheme());
        assertEquals(questions, card.getQuestions());
        assertEquals(2, card.getQuestions().size());
        assertSame(q1, card.getQuestions().get(0));
        assertSame(q2, card.getQuestions().get(1));
    }
}
