package models;

import java.util.List;

public class Board {
	private List<Space> spaces;
	private List<List<Space>> rows; // List of rows of spaces
	
	public Board(List<Space> spaces, List<List<Space>> rows) {
		this.spaces = spaces;
		this.rows = rows;
	}
	
	// Getters
	public List<Space> getSpaces() {
		return spaces;
	}
	
	public List<List<Space>> getRows() {
		return rows;
	}
}
