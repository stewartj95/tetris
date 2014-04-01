package tetris;

import java.util.ArrayList;
import java.util.List;

public class DroppedTetrominoes {
	private ArrayList<Shape> tetrominoes;
	private Grid grid;
	
	public DroppedTetrominoes(Grid grid) {
		this.grid = grid;
		tetrominoes = new ArrayList<>();
	}
	
	public void addTetromino(Shape t) {
		Shape tetromino = new Shape(t.getWidth(), t.getHeight());
		tetromino.newShape(t.getShape());
		tetromino.setColor(t.getColor());
		tetromino.setX(t.getX());
		tetromino.setY(t.getY());

		for(int i=0; i<4; i++) {
			tetromino.setRow(i, t.getRow(i));
			tetromino.setColumn(i, t.getColumn(i));
		}

		int[][] coordinates = t.getShapeCoordinates();
		int[][] c = new int[4][2];
		for(int i=0; i<4; i++) {
			c[i][0] = coordinates[i][0];
			c[i][1] = coordinates[i][1];
		}
		tetromino.setShapeCoordinates(c);
		tetrominoes.add(tetromino);
	}
	
	public int size() {
		return tetrominoes.size();
	}
	
	public Shape get(int index) {
		return tetrominoes.get(index);
	}
	
	public List<Shape> getTetrominoesOnRow(int row) {
		List<Shape> tetrominoes = new ArrayList<>();
		for(int i=0; i<tetrominoes.size(); i++) {
			Shape tetromino = this.tetrominoes.get(i);
			for(int j=0; j<4; j++) {
				if(tetromino.getRow(j) == row) {
					tetrominoes.add(tetromino);
				}
			}
		}
		return tetrominoes;
	}
	
}
