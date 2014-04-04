package model;
import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

import view.game.GameView;

public class ShapeModel {
	
	// This class provides a way to create tetromino shapes, keep
	// track of their current location, generate random tetrominoes,
	// and get information about the tetromino such as their colour,
	// their current coordinate
	
	public enum Tetrominoes {NOSHAPE, TSHAPE, SSHAPE, 
		REVERSESSHAPE, LSHAPE, REVERSELSHAPE, LINE, SQUARE};
	public static final int RIGHT = 0, LEFT = 1;
	int[][] coordinates;
	int[][] blockCoordinates;
	Tetrominoes currentShape;
	private int x, y, row, column, width, height;
	private Color color;
	private boolean rotated = false;
	private HashMap<Tetrominoes, int[][][]> rotationCoords;
	private int coordinatesIndex = 0;
	private GameView board;
	
	public ShapeModel() {
		Tetrominoes shape = Tetrominoes.NOSHAPE;
		coordinates = new int[4][2];
		currentShape = shape;
		color = Color.RED;
		blockCoordinates = new int[4][2];
		loadRotationCoordinates();
	}
	
	// Returns the row for the whole shape, which is the row of 
	// the bottom-most block.
	public int getRow() {
		int row = 0;
		for (int i = 0; i < blockCoordinates.length; i++) {
			row = Math.max(row, getBlockRow(i));
		}
		return row;
	}
	
	// Returns the leftmost or rightmost column. E.g. if direction is RIGHT
	// then the max column value is returned.
	public int getColumn(int DIRECTION) {
		int column = getBlockColumn(0);
		for (int i = 0; i < 4; i++) {
			if(DIRECTION == RIGHT) 
				column = Math.max(column, getBlockColumn(i));
			else if(DIRECTION == LEFT)
				column = Math.min(column, getBlockColumn(i));
		}
		return column;
	}
	
	public void rotate(int DIRECTION) {
		int last = rotationCoords.get(currentShape).length - 1;

		if(DIRECTION == ShapeModel.RIGHT ) {
			if(coordinatesIndex < last)
				coordinatesIndex++;
			else
				coordinatesIndex = 0;
		} else if (DIRECTION == ShapeModel.LEFT ) {
			if(coordinatesIndex > 0)
				coordinatesIndex--;
			else
				coordinatesIndex = last;
		}
		
		int[][] rotationCoordinates = new int[4][2];
		rotationCoordinates = rotationCoords.get(currentShape)[coordinatesIndex];
		
		if(isLegalRotation(rotationCoordinates)) { 
			coordinates = rotationCoordinates;
			int originColumn = findOriginColumn();
			int originRow = findOriginRow();
			for (int block = 0; block < 4; block++) {
				column = originColumn + coordinates[block][0];
				row = originRow + coordinates[block][1];
				setColumn(block, column);
				setRow(block, row);
			}
		}
	}
	
	// Returns the index of the origin column, i.e. the block in the middle of the shape.
	private int findOriginColumn() {
		for (int i = 0; i < 4; i++) {
			if(coordinates[i][0] == 0 && coordinates[i][1] == 0) {
				return getBlockColumn(i) + coordinates[i][0];
			}
		}
		return -1;
	}
	
	// Returns the index of the origin row, i.e. the block in the middle of the shape.
	private int findOriginRow() {
		for (int i = 0; i < 4; i++) {
			if(coordinates[i][0] == 0 && coordinates[i][1] == 0) {
				return getBlockRow(i) + coordinates[i][1];
			}
		}
		return -1;
	}
	
	private boolean isLegalRotation(int[][] coordinates) {
		boolean isLegal = true;
		for (int i = 0; i < 4; i++) {
			column = getBlockColumn(i) + coordinates[i][0];
			row = getBlockRow(i) + coordinates[i][1];
			if(column < 0 || column > 9) {
				// This would be an illegal rotation, so do not allow it.
				isLegal = false;
			}
		}
		return isLegal;
	}

	private void loadRotationCoordinates() {
		// ALL ROTATIONS FROM 0, 1, ..., N ARE CLOCKWISE.
		rotationCoords = new HashMap<>();
		int[][][] TSHAPEcoords = {
				{{-1,0}, {0,0}, {1,0}, {0,1}},  // DEFAULT
				{{0,1}, {0,0}, {0,-1}, {1,0}},  // 90 DEG 
				{{1,0}, {0,0}, {-1,0}, {0,-1}}, // 180 DEG  
				{{0,-1}, {0,0}, {0,1}, {-1,0}}, // 270 DEG  
		};
		rotationCoords.put(Tetrominoes.TSHAPE, TSHAPEcoords);
		
		int[][][] SSHAPEcoords = {
				{ {-1,1}, {-1,0}, {0,0}, {0,-1} }, // DEFAULT
				{ {1,1}, {0,1}, {0,0}, {-1,0} },   // 90 DEG 
		};
		rotationCoords.put(Tetrominoes.SSHAPE, SSHAPEcoords);
		
		int[][][] REVERSESSHAPEcoords = {
				{ {1,1}, {1,0}, {0,0}, {0,-1} },  // DEFAULT
				{ {1,-1}, {0,-1}, {0,0}, {-1,0} }, // 90 DEG
		};
		rotationCoords.put(Tetrominoes.REVERSESSHAPE, REVERSESSHAPEcoords);
		
		int[][][] LSHAPEcoords = {
				{ {-1,1}, {-1,0}, {0,0}, {1,0} },  // DEFAULT
				{ {1,1}, {0,1}, {0,0}, {0,-1}  },  // 90 DEG
				{ {1,-1}, {1,0}, {0,0}, {-1,0} },  // 180 DEG
				{ {-1,-1}, {0,-1}, {0,0}, {0,1} }  // 270 DEG
		};
		rotationCoords.put(Tetrominoes.LSHAPE, LSHAPEcoords);
		
		int[][][] REVERSELSHAPEcoords = {
				{ {1,1}, {1,0}, {0,0}, {-1,0}},    // DEFAULT
				{ {1,-1}, {0,-1}, {0,0}, {0,1}},   // 90 DEG  
				{ {-1,-1}, {-1,0}, {0,0}, {1,0}},  // 180 DEG  
				{ {-1,1}, {0,1}, {0,0}, {0,-1}},   // 270 DEG  
		};
		rotationCoords.put(Tetrominoes.REVERSELSHAPE, REVERSELSHAPEcoords);
		
		int[][][] LINESHAPEcoords = {
				{ {0,-1}, {0,0}, {0,1}, {0,2} },   // DEFAULT
				{ {-1,0}, {0,0}, {1,0}, {2,0} },   // 90 DEG
		};
		rotationCoords.put(Tetrominoes.LINE, LINESHAPEcoords);
		
		int[][][] SQUARESHAPEcoords = {
				{ {0,-1}, {0,0}, {1,0}, {1,-1} }   // DEFAULT 
		};
		rotationCoords.put(Tetrominoes.SQUARE, SQUARESHAPEcoords);
		
	}
	
	public void newShape(Tetrominoes shape) {
		coordinates = rotationCoords.get(shape)[0];
		currentShape = shape;
		for (int block = 0; block < 4; block++) {
			column = 5 + coordinates[block][0];
			row = 2 + coordinates[block][1];
			setColumn(block, column);
			setRow(block, row);
		}
	}

	public Tetrominoes getShape() {
		return currentShape;
	}
	
	public int[][] getRotationCoordinates(Tetrominoes shape, int DIRECTION) {
		return null;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	// Returns the X coordinate for one block in this tetromino
	public int blockX(int block) {
		return calculateXPosition(coordinates[block][0]); 
	}
	
	// Returns the Y coordinate for one block in this tetromino
	public int blockY(int block) {
		return calculateYPosition(coordinates[block][1]); 
	}

	private int calculateXPosition(int c) {
		int shapeX = 0;
		if (c < 0) {
			shapeX = x - width;
		} else if (c == 0) {
			shapeX = x;
		} else if (c == 1) {
			shapeX = x + width;
		} else if (c == 2) {
			shapeX = x + (2 * width);
		}
		return shapeX;
	}
	

	private int calculateYPosition(int c) {
		int shapeY = 0;
		if (c < 0) {
			shapeY = y - height;
		} else if (c == 0) {
			shapeY = y;
		} else if (c == 1) {
			shapeY = y + height;
		} else if (c == 2) {
			shapeY = y + (2 * height);
		}
		return shapeY;
	}
		
	public void setRow(int block, int row) {
		blockCoordinates[block][0] = row;
	}
	
	public void setColumn(int block, int column) {
		blockCoordinates[block][1] = column;
	}
	
	public int getBlockRow(int block) {
		return blockCoordinates[block][0];
	}
	
	public int getBlockColumn(int block) {
		return blockCoordinates[block][1];
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setShapeCoordinates(int[][] coordinates) {
		this.coordinates = coordinates;
	}
	
	public int[][] getShapeCoordinates() {
		return coordinates;
	}
	
	public int maxX() {
		int max = 0;
		for(int i=0; i<4; i++) {
			max = Math.max(max, coordinates[i][0]);
		}
		return max + 2;
	}
	
	public int maxY() {
		int max = 0;
		for(int i=0; i<4; i++) {
			max = Math.max(max, coordinates[i][1]);
		}
		return max;
	}
	
	public int minX() {
		int min = 0;
		for(int i=0; i<4; i++) {
			min = Math.min(min, coordinates[i][0]);
		}
		return min;
	}

	public int minY() {
		int min = 0;
		for(int i=0; i<4; i++) {
			min = Math.min(min, coordinates[i][1]);
		}
		return min;
	}
	
	public void randomShape() {
		Random r = new Random();
		int index = r.nextInt(Tetrominoes.values().length-2)+1;
		switch (r.nextInt(3)) {
			case 0:
				setColor(Color.RED);
				break;
			case 1:
				setColor(Color.CYAN);
				break;
			case 2:
				setColor(Color.YELLOW);
				break;
			case 3:
				setColor(Color.GREEN);
				break;
			default:
				setColor(Color.RED);
				break;
		}
		rotated = false;
		coordinatesIndex = 0;
		newShape(Tetrominoes.values()[index]);
	}
		
}