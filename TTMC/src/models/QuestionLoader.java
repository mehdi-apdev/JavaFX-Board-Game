
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
	        
	        // Map pour stocker les questions par thème et difficulté
	        Map<Topic, Map<Integer, List<Question>>> questionsByTopicAndDifficulty = new HashMap<>();
	        for (Topic topic : Topic.values()) {
	            questionsByTopicAndDifficulty.put(topic, new HashMap<>());
	            for (int i = 1; i <= 4; i++) {
	                questionsByTopicAndDifficulty.get(topic).put(i, new ArrayList<>());
	            }
	        }

	        // Charger toutes les questions et les classer par thème et difficulté
	        for (JsonNode questionNode : rootNode) {
	            String themeName = questionNode.get("theme_name").asText();
	            Topic topic = Topic.valueOf(themeName.toUpperCase());
	            String text = questionNode.get("question").asText();
	            int difficulty = questionNode.get("priority").asInt();

	            List<String> responses = new ArrayList<>();
	            JsonNode responsesNode = questionNode.get("responses");
	            
	            // Déterminer la réponse correcte (première réponse dans le JSON)
	            int correctIndex = 0;
	            
	            for (int i = 0; i < responsesNode.size(); i++) {
	                responses.add(responsesNode.get(i).asText());
	            }

	            QuestionFactory factory = factories.get(topic);
	            Question question = factory.createQuestion(text, responses, correctIndex, difficulty);
	            
	            // Stocker la question par thème et difficulté
	            if (difficulty >= 1 && difficulty <= 4) {
	                questionsByTopicAndDifficulty.get(topic).get(difficulty).add(question);
	            }
	        }
	        
	        // Réorganiser les questions pour chaque thème
	        for (Topic topic : Topic.values()) {
	            QuestionFactory factory = factories.get(topic);
	            
	            if (factory != null) {
	                Map<Integer, List<Question>> questionsByDifficulty = questionsByTopicAndDifficulty.get(topic);
	                List<Question> organizedQuestions = new ArrayList<>();
	                
	                // Déterminer combien de cartes complètes on peut créer (une carte = 4 questions de difficultés différentes)
	                int maxCompleteCards = Integer.MAX_VALUE;
	                for (int difficulty = 1; difficulty <= 4; difficulty++) {
	                    int questionsCount = questionsByDifficulty.get(difficulty).size();
	                    maxCompleteCards = Math.min(maxCompleteCards, questionsCount);
	                }
	                
	                // Créer des cartes complètes (1 question de chaque difficulté)
	                for (int cardIndex = 0; cardIndex < maxCompleteCards; cardIndex++) {
	                    for (int difficulty = 1; difficulty <= 4; difficulty++) {
	                        organizedQuestions.add(questionsByDifficulty.get(difficulty).get(cardIndex));
	                    }
	                }
	                
	                // Remplacer les questions dans la factory
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
