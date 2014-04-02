package tetris;
import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

public class Shape {
	enum Tetrominoes {NOSHAPE, TSHAPE, SSHAPE, REVERSESSHAPE, LSHAPE, REVERSELSHAPE, LINE, SQUARE};
	public static final int RIGHT = 0, LEFT = 1;
	int[][] coordinates;
	int[][] blockCoordinates;
	Tetrominoes currentShape;
	private int x, y, row, column, width, height;
	private Color color;
	private boolean rotated = false;
	private HashMap<Tetrominoes, int[][][]> rotationCoords;
	private int rotationIndex = 0;
	
	public Shape(int width, int height) {
		this.width = width;
		this.height = height;
		Tetrominoes shape = Tetrominoes.NOSHAPE;
		coordinates = new int[4][2];
		currentShape = shape;
		color = Color.RED;
		blockCoordinates = new int[4][2];
		loadRotationCoordinates();
	}

	public void rotate(int DIRECTION) {
		int oldRotationIndex = rotationIndex;
		if(DIRECTION == Shape.RIGHT) {
			rotationIndex++;
		} else if (DIRECTION == Shape.LEFT) {
			rotationIndex--;
		}
		
		int[][] newCoordinates = new int[4][2];
		
		try {
			newCoordinates = rotationCoords.get(currentShape)[rotationIndex];
		} catch (IndexOutOfBoundsException exception) {
			if(DIRECTION == Shape.LEFT) {
				rotationIndex = rotationCoords.get(currentShape).length-1;
			} else if (DIRECTION == Shape.RIGHT) {
				rotationIndex = 0;
			}
			newCoordinates = rotationCoords.get(currentShape)[rotationIndex];
		}
		
		// Check if this would be a legal rotation, i.e. it would not make 
		// any blocks leave the grid.
		for (int i = 0; i < newCoordinates.length; i++) {
			int column = calculateXPosition(newCoordinates[i][0]) / width;
			if(column < 0 || column > 9) {
				// This would be an illegal rotation, so do not allow it.
				rotationIndex = oldRotationIndex;
				return;
			}
		}
		
		coordinates = newCoordinates;
		
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
				{ {1,-1}, {0,-1}, {0,0}, {1,0} }, // 90 DEG
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
		
	}
	
	public void newShape(Tetrominoes shape) {
		coordinates = rotationCoords.get(shape)[0];
		currentShape = shape;
	}
	
	public int[][] getRotationCoordinates(Tetrominoes shape, int DIRECTION) {
		return null;
	}

	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Tetrominoes getShape() {
		return currentShape;
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
	
	public int getRow(int block) {
		return blockCoordinates[block][0];
	}
	
	public int getColumn(int block) {
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
		rotationIndex = 0;
		newShape(Tetrominoes.values()[1]);
	}
		
}