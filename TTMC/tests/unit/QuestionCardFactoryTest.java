package unit;

import models.Question;
import models.QuestionCard;
import models.QuestionCardFactory;
import models.Topic;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionCardFactoryTest {

    @Test
    void testCreateQuestionCards() {
        
        QuestionCardFactory factory = new QuestionCardFactory();

        
        List<Question> educationQuestions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            educationQuestions.add(new Question(
                    "Question " + i,
                    List.of("A", "B", "C", "D"),
                    0,
                    Topic.EDUCATION,
                    1
            ));
        }

        
        List<Question> entertainmentQuestions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            entertainmentQuestions.add(new Question(
                    "Entertainment " + i,
                    List.of("X", "Y", "Z", "W"),
                    1,
                    Topic.ENTERTAINMENT,
                    2
            ));
        }

        Map<Topic, List<Question>> questionsMap = new HashMap<>();
        questionsMap.put(Topic.EDUCATION, educationQuestions);       // 5 questions → 2 cartes (4 + 1)
        questionsMap.put(Topic.ENTERTAINMENT, entertainmentQuestions); // 3 questions → 1 carte

        // Act
        List<QuestionCard> cards = factory.createQuestionCards(questionsMap);

        // Assert
        assertEquals(3, cards.size()); // 2 + 1 cards expected

        // Vérifie le contenu des cartes
        int educationCards = 0;
        int entertainmentCards = 0;

        for (QuestionCard card : cards) {
            assertNotNull(card.getQuestions());
            assertTrue(card.getQuestions().size() <= 4); // Max 4 questions par carte

            if (card.getTheme() == Topic.EDUCATION) {
                educationCards++;
            } else if (card.getTheme() == Topic.ENTERTAINMENT) {
                entertainmentCards++;
            }
        }

        assertEquals(2, educationCards);
        assertEquals(1, entertainmentCards);
    }
}
