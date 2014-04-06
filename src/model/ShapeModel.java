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
	
	public enum Type {CURRENT_SHAPE, SHADOW};
	public enum Tetrominoes {NOSHAPE, TSHAPE, SSHAPE, 
		REVERSESSHAPE, LSHAPE, REVERSELSHAPE, LINE, SQUARE};
	Tetrominoes currentShape;

	public static final int RIGHT = 0, LEFT = 1;

	private int row, column, maxColumns = 10;
	private Color color;
	private HashMap<Tetrominoes, int[][][]> rotationCoords;

	private int coordinatesIndex = 0, nextShapeIndex = 0;
	int[][] coordinates;
	int[][] blockCoordinates;
	
	private Type type;
	
	public ShapeModel(Type type) {
		this.type = type;
		Tetrominoes shape = Tetrominoes.NOSHAPE;
		coordinates = new int[4][2];
		currentShape = shape;
		color = Color.RED;
		blockCoordinates = new int[4][2];
		loadRotationCoordinates();
	}
	
	public void setMaxColumns(int maxColumns) {
		this.maxColumns = maxColumns;
	}
	
	public int getMaxColumns() {
		return maxColumns;
	}
	
	public Type getType() {
		return type;
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
	
	public int[][] getCoordinates() {
		return coordinates;
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
	
	public void rotate(int DIRECTION, Cell[][] cells) {
		int indexBackup = coordinatesIndex;
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
		
		if(isLegalRotation(rotationCoordinates, cells)) { 
			coordinates = rotationCoordinates;
			int originColumn = findOriginColumn();
			int originRow = findOriginRow();
			for (int block = 0; block < 4; block++) {
				column = originColumn + coordinates[block][0];
				row = originRow + coordinates[block][1];
				setColumn(block, column);
				setRow(block, row);
			}
		} else {
			coordinatesIndex = indexBackup;
		}
	}
	
	// Returns the index of the origin column, i.e. the block in the middle of the shape.
	public int findOriginColumn() {
		for (int i = 0; i < 4; i++) {
			if(coordinates[i][0] == 0 && coordinates[i][1] == 0) {
				return getBlockColumn(i) + coordinates[i][0];
			}
		}
		return -1;
	}
	
	// Returns the index of the origin row, i.e. the block in the middle of the shape.
	public int findOriginRow() {
		for (int i = 0; i < 4; i++) {
			if(coordinates[i][0] == 0 && coordinates[i][1] == 0) {
				return getBlockRow(i) + coordinates[i][1];
			}
		}
		return -1;
	}
	
	// @param cells  A grid of cells in the tetris board. Used to check for legal rotation
	//               i.e. the rotation won't cause this tetromino to go into other tetrominoes. 
	// @param coordinates  the rotation coordinates, used to check the new location the 
	//                     tetromino will go to is legal
	private boolean isLegalRotation(int[][] coordinates, Cell[][] cells) {
		boolean isLegal = true;
		Cell cell = null;
		for (int i = 0; i < 4; i++) {
			try {
				column = getBlockColumn(i) + coordinates[i][0];
				row = getBlockRow(i) + coordinates[i][1];
				cell = cells[row][column];
				if(column < 0 || column > 9 || cell.getState() == Cell.NOT_EMPTY) {
					// This would be an illegal rotation, so do not allow it.
					isLegal = false;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				// Tried to get invalid cell index, e.g. -1, which is not a legal rotation.
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
			column = maxColumns/2 + coordinates[block][0];
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
		switch (currentShape.ordinal()) {
			case 1:
				setColor(Color.RED);
				break;
			case 2:
				setColor(Color.CYAN);
				break;
			case 3:
				setColor(Color.YELLOW);
				break;
			case 4:
				setColor(Color.GREEN);
				break;
			case 5:
				setColor(Color.ORANGE);
				break;
			case 6:
				setColor(Color.PINK);
				break;
			case 7:
				setColor(Color.MAGENTA);
				break;
			default:
				break;
		}
		return color;
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
	
	public void randomShape() {
		Random r = new Random();
		if(nextShapeIndex == 0) {
			// First shape generation
			nextShapeIndex = r.nextInt(Tetrominoes.values().length-1)+1;
		}
		
		coordinatesIndex = 0;
		newShape(Tetrominoes.values()[nextShapeIndex]);
		nextShapeIndex  = r.nextInt(Tetrominoes.values().length-1)+1;
	}
	
	public int getNextShapeIndex() {
		return nextShapeIndex;
	}
		
}