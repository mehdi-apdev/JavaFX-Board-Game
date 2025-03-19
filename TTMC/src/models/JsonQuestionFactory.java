
package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonQuestionFactory implements QuestionFactory {

	private Map<Topic, List<Question>> questions;

	public JsonQuestionFactory() {
		this.questions = new HashMap<>();
		for (Topic topic : Topic.values()) {
			questions.put(topic, new ArrayList<>());
		}
	}

	@Override
	public Question createQuestion(String texte, List<String> response, int indexOfCorrectResponse, Topic topic,
			int difficulty) {
		Question question = new Question(texte, response, indexOfCorrectResponse, topic, difficulty);
		questions.get(topic).add(question);
		return question;
	}

	public void loadQuestions(String jsonFilePath) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

			for (JsonNode questionNode : rootNode) {
				String themeName = questionNode.get("theme_name").asText();
				Topic topic = Topic.valueOf(themeName.toUpperCase());
				String text = questionNode.get("question").asText();
				int difficulty = questionNode.get("priority").asInt();
				List<String> responses = new ArrayList<>();
				for (JsonNode responseNode : questionNode.get("responses")) {
					responses.add(responseNode.asText());
				}
				int indexOfCorrectResponse = 0; // Assuming the correct response is always the first one
				createQuestion(text, responses, indexOfCorrectResponse, topic, difficulty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Topic, List<Question>> getQuestions() {
		return questions;
	}
}
