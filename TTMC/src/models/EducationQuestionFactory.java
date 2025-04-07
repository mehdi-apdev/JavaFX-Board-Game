
package models;

import java.util.ArrayList;
import java.util.List;

public class EducationQuestionFactory implements QuestionFactory {
	private List<Question> questions = new ArrayList<>();

	@Override
	public Question createQuestion(String texte, List<String> response, int indexOfCorrectResponse, int difficulty) {
		Question question = new Question(texte, response, indexOfCorrectResponse, Topic.EDUCATION, difficulty);
		questions.add(question);
		return question;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
