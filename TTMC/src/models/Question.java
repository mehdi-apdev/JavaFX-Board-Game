package models;

import java.util.List;

public class Question {
	
	private String texte;
	private List<String> Response;
	private int indexOfCorrectResponse;
	private Topic topic;
	private int difficulty;
	
	public Question(String texte, List<String> response, int indexOfCorrectResponse, Topic topic, int difficulty) {
		this.texte = texte;
		Response = response;
		this.indexOfCorrectResponse = indexOfCorrectResponse;
		this.topic = topic;
		this.difficulty = difficulty;
	}
	
	// Check if the response is correct
	public boolean isCorrectResponse(int index) {
		return index == indexOfCorrectResponse;
	}
	
	// Getters
	public String getTexte() {
		return texte;
	}
	
	public List<String> getResponse() {
		return Response;
	}
	
	public int getDifficulty() {
        return difficulty;
    }
	
	public Topic getTopic() {
		return topic;
		
	}
	
	
	
	
	

}
