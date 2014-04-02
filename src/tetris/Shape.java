package tetris;
import java.awt.Color;
import java.util.Random;

public class Shape {
	enum Tetrominoes {NOSHAPE, TSHAPE, SSHAPE, REVERSESSHAPE, LSHAPE, REVERSELSHAPE, LINE, SQUARE};
	public static final int RIGHT = 0, LEFT = 1, DESTROYED = 9999;
	int[][] coordinates;
	int[][] blockCoordinates;
	Tetrominoes currentShape;
	private int x, y, row, column, width, height;
	private Color color;
	private boolean rotated = false;
	
	public Shape(int width, int height) {
		this.width = width;
		this.height = height;
		Tetrominoes shape = Tetrominoes.NOSHAPE;
		coordinates = new int[4][2];
		currentShape = shape;
		color = Color.RED;
		blockCoordinates = new int[4][2];
	}
	
	public void newShape(Tetrominoes shape) {
		int[][][] coordinatesTable = {
				{ {0,0}, {0,0}, {0,0}, {0,0} },	   // NO SHAPE 
				{ {-1,0}, {0,0}, {1,0}, {0,1} },   // T SHAPE
				{ {-1,1}, {-1,0}, {0,0}, {0,-1} }, // S SHAPE
				{ {1,1}, {1,0}, {0,0}, {0,-1} },   // REVERSE S SHAPE
				{ {-1,1}, {-1,0}, {0,0}, {1,0} },  // L SHAPE
				{ {1,1}, {1,0}, {0,0}, {-1,0}},    // REVERSE L SHAPE
				{ {0,-1}, {0,0}, {0,1}, {0,2} },   // LINE SHAPE
				{ {0,-1}, {0,0}, {1,0}, {1,-1} }   // SQUARE SHAPE
		};
		
		for(int i=0; i<4; i++) {
			coordinates[i][0] = coordinatesTable[shape.ordinal()][i][0];
			coordinates[i][1] = coordinatesTable[shape.ordinal()][i][1];
		}

		currentShape = shape;
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
	
		
	public void rotate(int DIRECTION) {
		if(currentShape == Tetrominoes.LINE) {
			if(!rotated) {
				DIRECTION = RIGHT;
				rotated = true;
			} else {
				newShape(Tetrominoes.LINE);
				rotated = false;
				return;
			}
		}
		if(currentShape == Tetrominoes.SQUARE) {
			return;
		}
		for(int i=0; i<4; i++) {
			int x = coordinates[i][0];
			int y = coordinates[i][1];
			
			if(DIRECTION == RIGHT) {
				coordinates[i][0] = y;
				coordinates[i][1] = -x;
			} else if(DIRECTION == LEFT) {
				coordinates[i][0] = -y;
				coordinates[i][1] = x;
			}
		}
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
		newShape(Tetrominoes.values()[index]);
	}
		
}