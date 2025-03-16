package view;

import javafx.scene.shape.Rectangle;
import models.Space;

public class SpaceView {
	private Rectangle rectangle; //visual representation of the space
	private Space space; //space object
	
	//Constructor
	public SpaceView(Space space, Rectangle rectangle) {
		this.space = space;
		this.rectangle = rectangle;
	}
	
	
	//Getters of rectangle
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	//Getters of space
	public Space getSpace() {
		return space;
	}
	

}
