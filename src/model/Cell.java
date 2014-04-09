package model;

import java.awt.Color;

public class Cell {
	
	public static final int EMPTY = 0;
	public static final int NOT_EMPTY = 1;
	public static final int TETROMINO = 2;
	public static final int SHADOW = 3;
	private Color fillColor, outlineColor;
	private int state;
	
	public Cell(int state, Color color) {
		this.state = state; 
		this.fillColor = color;
	}
	
	public Cell(int state) {
		this.state = state;
		this.fillColor = Color.PINK;
	}
	
	public Cell() {
		this.state = EMPTY;
		this.fillColor = Color.PINK;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void setFillColor(Color color) {
		this.fillColor = color;
	}
	
	public Color getFillColor() {
		return this.fillColor;
	}
	
	public void setOutlineColor(Color color) {
		this.outlineColor = color;
	}
	
	public Color getOutlineColor() {
		return outlineColor;
	}
	
}