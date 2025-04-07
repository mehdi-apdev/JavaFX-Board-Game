
package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class QuestionLoader {
	private Map<Topic, QuestionFactory> factories;

	public QuestionLoader() {
		factories = new EnumMap<>(Topic.class);
		factories.put(Topic.IMPROBABLE, new ImprobableQuestionFactory());
		factories.put(Topic.INFORMATICS, new InformaticsQuestionFactory());
		factories.put(Topic.ENTERTAINMENT, new EntertainmentQuestionFactory());
		factories.put(Topic.EDUCATION, new EducationQuestionFactory());
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

				QuestionFactory factory = factories.get(topic);
				factory.createQuestion(text, responses, 0, difficulty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Topic, QuestionFactory> getFactories() {
		return factories;
	}
}
