package models;

import java.util.List;

public interface QuestionFactory {
	
	public Question createQuestion(String texte, List<String> response, int indexOfCorrectResponse, Topic topic, int difficulty);

}
