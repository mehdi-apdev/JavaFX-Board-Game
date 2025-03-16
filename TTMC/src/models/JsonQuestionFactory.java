package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonQuestionFactory {

	private Map<Topic, List<Question>> questions;
	
	public JsonQuestionFactory(Map<Topic, List<Question>> questions) {
		this.questions = questions;
	}
	
	public Question createQuestion(String texte, List<String> response, int indexOfCorrectResponse, Topic topic,
			int difficulty) {
		// Create a new question
		Question question = new Question(texte, response, indexOfCorrectResponse, topic, difficulty);
		List<Question> questionList = questions.get(topic); // Get the list of questions for the topic
		questionList.add(question); // Add the question to the list
		questions.put(topic, questionList); // Update the list of questions for the
		return question; // Return the question
	}
	
	//Load question from json

	
	public void loadQuestions(String JSONFile) {
	
		HashMap<Topic, List<Question>> questions = new HashMap<Topic, List<Question>>();
		
		//Initialize the list of questions for each topic
		for (Topic topic : Topic.values()) {
            questions.put(topic, new ArrayList<Question>());
        }
		
		try {
			//load the json file
			ObjectMapper objectMapper = new ObjectMapper();
			
			JsonNode rootNode = objectMapper.readTree(JSONFile);
			
			//Iterate over the topics
			for (Topic topic : Topic.values()) {
                JsonNode topicNode = rootNode.get(topic.toString());
                //Iterate over the questions
                for (JsonNode questionNode : topicNode) {
                	
                    String text = questionNode.get("text").asText(); // Get the text of the question
                    int difficulty = questionNode.get("difficulty").asInt(); // Get the difficulty of the question
                    int indexOfCorrectResponse = questionNode.get("indexOfCorrectResponse").asInt(); // Get the index of the correct response
                    List<String> responses = new ArrayList<String>(); // Create a list of responses
                    for (JsonNode responseNode : questionNode.get("responses")) {
                        responses.add(responseNode.asText());
                    }
                    
                    createQuestion(text, responses, indexOfCorrectResponse, topic, difficulty); // Create the question
                }
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
	
	}
}
