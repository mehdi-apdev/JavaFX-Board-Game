package models;

import java.util.List;

public interface QuestionFactory {
    Question createQuestion(String texte, List<String> response, int indexOfCorrectResponse, int difficulty);
}