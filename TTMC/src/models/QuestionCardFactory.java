
package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionCardFactory {

	private JsonQuestionFactory jsonQuestionFactory;

	public QuestionCardFactory(JsonQuestionFactory jsonQuestionFactory) {
		this.jsonQuestionFactory = jsonQuestionFactory;
	}

	public List<QuestionCard> createQuestionCards() {
		Map<Topic, List<Question>> questions = jsonQuestionFactory.getQuestions();
		List<QuestionCard> questionCards = new ArrayList<>();

		for (Map.Entry<Topic, List<Question>> entry : questions.entrySet()) {
			List<Question> questionList = entry.getValue();
			for (int i = 0; i < questionList.size(); i += 4) {
				List<Question> cardQuestions = new ArrayList<>();
				for (int j = i; j < i + 4 && j < questionList.size(); j++) {
					cardQuestions.add(questionList.get(j));
				}
				questionCards.add(new QuestionCard(entry.getKey(), cardQuestions));
			}
		}

		return questionCards;
	}
}
