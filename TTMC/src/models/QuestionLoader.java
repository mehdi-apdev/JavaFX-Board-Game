
package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class QuestionLoader {
	private Map<Topic, QuestionFactory> factories;
	
	/**
	 * Handles loading and organizing questions from JSON files for the game.
	 * This class is responsible for:
	 * - Loading question data from JSON files
	 * - Parsing and validating question content
	 * - Organizing questions by topic and difficulty
	 * - Distributing questions to appropriate QuestionFactory implementations
	 */
	public QuestionLoader() {
		factories = new EnumMap<>(Topic.class);
		factories.put(Topic.IMPROBABLE, new ImprobableQuestionFactory());
		factories.put(Topic.INFORMATICS, new InformaticsQuestionFactory());
		factories.put(Topic.ENTERTAINMENT, new EntertainmentQuestionFactory());
		factories.put(Topic.EDUCATION, new EducationQuestionFactory());
	}

	
	/**
	 * Loads questions from a JSON file and organizes them by topic and difficulty.
	 * 
	 * This method:
	 * 1. Parses the JSON file using Jackson's ObjectMapper
	 * 2. Extracts question data including theme, text, difficulty, and response options
	 * 3. Creates Question objects using appropriate factory classes
	 * 4. Organizes questions to ensure balanced distribution across difficulty levels
	 * 5. Assigns the organized questions to their respective factory implementations
	 *
	 * @param jsonFilePath Path to the JSON file containing question data
	 * @throws RuntimeException If there is an error reading or parsing the JSON file
	 */
	public void loadQuestions(String jsonFilePath) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
	        
	        
	        Map<Topic, Map<Integer, List<Question>>> questionsByTopicAndDifficulty = new HashMap<>();
	        for (Topic topic : Topic.values()) {
	            questionsByTopicAndDifficulty.put(topic, new HashMap<>());
	            for (int i = 1; i <= 4; i++) {
	                questionsByTopicAndDifficulty.get(topic).put(i, new ArrayList<>());
	            }
	        }

	        
	        for (JsonNode questionNode : rootNode) {
	            String themeName = questionNode.get("theme_name").asText();
	            Topic topic = Topic.valueOf(themeName.toUpperCase());
	            String text = questionNode.get("question").asText();
	            int difficulty = questionNode.get("priority").asInt();

	            List<String> responses = new ArrayList<>();
	            JsonNode responsesNode = questionNode.get("responses");
	            
	            
	            int correctIndex = 0;
	            
	            for (int i = 0; i < responsesNode.size(); i++) {
	                responses.add(responsesNode.get(i).asText());
	            }

	            QuestionFactory factory = factories.get(topic);
	            Question question = factory.createQuestion(text, responses, correctIndex, difficulty);
	            
	            if (difficulty >= 1 && difficulty <= 4) {
	                questionsByTopicAndDifficulty.get(topic).get(difficulty).add(question);
	            }
	        }
	        
	        
	        for (Topic topic : Topic.values()) {
	            QuestionFactory factory = factories.get(topic);
	            
	            if (factory != null) {
	                Map<Integer, List<Question>> questionsByDifficulty = questionsByTopicAndDifficulty.get(topic);
	                List<Question> organizedQuestions = new ArrayList<>();
	                
	                
	                int maxCompleteCards = Integer.MAX_VALUE;
	                for (int difficulty = 1; difficulty <= 4; difficulty++) {
	                    int questionsCount = questionsByDifficulty.get(difficulty).size();
	                    maxCompleteCards = Math.min(maxCompleteCards, questionsCount);
	                }
	                
	                
	                for (int cardIndex = 0; cardIndex < maxCompleteCards; cardIndex++) {
	                    for (int difficulty = 1; difficulty <= 4; difficulty++) {
	                        organizedQuestions.add(questionsByDifficulty.get(difficulty).get(cardIndex));
	                    }
	                }
	                
	                
	                if (factory instanceof ImprobableQuestionFactory) {
	                    ((ImprobableQuestionFactory) factory).setQuestions(organizedQuestions);
	                } else if (factory instanceof InformaticsQuestionFactory) {
	                    ((InformaticsQuestionFactory) factory).setQuestions(organizedQuestions);
	                } else if (factory instanceof EntertainmentQuestionFactory) {
	                    ((EntertainmentQuestionFactory) factory).setQuestions(organizedQuestions);
	                } else if (factory instanceof EducationQuestionFactory) {
	                    ((EducationQuestionFactory) factory).setQuestions(organizedQuestions);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public Map<Topic, QuestionFactory> getFactories() {
		return factories;
	}
}
