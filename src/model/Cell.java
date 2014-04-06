package model;

import java.awt.Color;

public class Cell {
	
	public static final int EMPTY = 0;
	public static final int NOT_EMPTY = 1;
	public static final int TETROMINO = 2;
	public static final int SHADOW = 3;
	private Color color;
	private int state;
	
	public Cell(int state, Color color) {
		this.state = state; 
		this.color = color;
	}
	
	public Cell(int state) {
		this.state = state;
		this.color = Color.PINK;
	}
	
	public Cell() {
		this.state = EMPTY;
		this.color = Color.PINK;
	}
	
	public void setState(int state) {
		this.state = state;
		if(state == Cell.SHADOW)
			color = Color.LIGHT_GRAY;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
}