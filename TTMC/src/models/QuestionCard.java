
package models;

import java.util.List;

public class QuestionCard {
	private Topic theme;
	private List<Question> questions;

	public QuestionCard(Topic theme, List<Question> questions) {
		this.theme = theme;
		this.questions = questions;
	}

	public Topic getTheme() {
		return theme;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
