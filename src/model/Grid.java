package model;

import controller.GameController;
import model.ShapeModel.Type;

/*
 * Grid contains the model of the Tetris grid. It contains information
 * and each cell, including its current state and colour. 
 */

public class Grid {
	
	// Instance variables
	private Cell cells[][];
	private ShapeModel shape;
	private ShapeModel shadow;
	private ScoreModel score;
	private int row, column;
	private GameController controller;
	
	// Constructs an instance of Grid with a blank grid of cells with 
	// 1 shape and 1 shadow.
	public Grid(ScoreModel score, GameController controller) {
		this.controller = controller;
		this.score = score;
		shape = new ShapeModel(Type.CURRENT_SHAPE);
		shadow = new ShapeModel(Type.SHADOW);
		cells = new Cell[22][10];
		for(int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				cells[row][column] = new Cell(Cell.EMPTY);
			}
		}
	}
	
	// Returns the current shape model.
	public ShapeModel getShapeModel() {
		return shape;
	}
	
	// Returns a 2D array containing all of the cells in the grid.
	public Cell[][] getCells() {
		return cells;
	}
	
	// Move the tetromino right or left if there is space to do so.
	public void move(int direction) {
		int column = shape.getColumn(direction);
		boolean validColumn = false;
		if(direction == ShapeModel.RIGHT) 
			validColumn = column < 9;
		if(direction == ShapeModel.LEFT) {
			validColumn = column > 0;
		}
		if (validColumn && cellBesideIsEmpty(direction)) {
			column = 0;
			for (int block = 0; block < 4; block++) {
				column = shape.getBlockColumn(block);
				row = shape.getBlockRow(block);
				Cell cell = getCell(row, column);
				cell.setState(Cell.EMPTY);
				cell.setColor(null);
				if(direction == ShapeModel.LEFT)
					shape.setColumn(block, column-1);
				else if (direction == ShapeModel.RIGHT) 
					shape.setColumn(block, column+1);
			}
		}
	}

	// Sets the state of the cells to empty which indicates the 
	// last position of the teromino.
	private void emptyCells() {
		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			column = shape.getBlockColumn(block);
			getCell(row, column).setState(Cell.EMPTY);;
			getCell(row, column).setColor(null);
		}
	}
	
	/**
	 * Rotate the tetromino 90 degrees to the right or left 
	 * if there is space to do so.
	 * @param direction  The direction to rotate the shape 90 degrees.
	 */
	public void rotate(int direction) {
		// Backup old position in case of out of bounds error
		int[][] oldPosition = new int[4][2];
		for (int i = 0; i < 4; i++) {
			oldPosition[i][0] = shape.getBlockRow(i);
			oldPosition[i][1] = shape.getBlockColumn(i);
		}

		try {
			// Try to rotate right or left
			if(direction == ShapeModel.RIGHT) {
				emptyCells();
				shape.rotate(shape.RIGHT, cells);
			} else if (direction == ShapeModel.LEFT) {
				emptyCells();
				shape.rotate(shape.LEFT, cells);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Restore old position
			for (int i = 0; i < 4; i++) {
				shape.setRow(i, oldPosition[i][0]);
				shape.setColumn(i, oldPosition[i][1]);
			}
		}
		update();
	}
	
	// 
	// 
	/**
	 * Checks if the cell to the left or to the right of 
	 * the tetromino is empty or not.
	 * @param direction  the left or right most direction to check for a NOT_EMPTY cell
	 * @return whether the cell is empty or not
	 */
	public boolean cellBesideIsEmpty(int direction) {
		int cellState = Cell.EMPTY;
		int row = 0, column = 0, block = 0;
		while(cellState != Cell.NOT_EMPTY && block < 4) {
			column = shape.getBlockColumn(block);
			row = shape.getBlockRow(block);
			if(direction == ShapeModel.LEFT && column > 0)
				column--;
			else if(direction == ShapeModel.RIGHT && column < 9)
				column++;
			cellState = getCell(row, column).getState();
			block++;
		}
		return cellState != Cell.NOT_EMPTY;
	}
	
	/**
	 * Returns an instance of Cell at a specified row and column.
	 * @param row  row of the cell
	 * @param column column of the cell
	 * @return instance of Cell for these coordinates
	 */
	public Cell getCell(int row, int column) {
		return cells[row][column];
	}
		
	/**
	 * Checks if a row contains 10 empty cells.
	 * @param row  the row to check if it is clear or not
	 * @return whether a specific row is clear.
	 */
	public boolean isRowClear(int row) {
		int clearCount = 0;
		for(int column=0; column<10; column++) {
			if(cells[row][column].getState() == Cell.EMPTY) {
				clearCount++;
			}
		}
		return clearCount == 10;
	}
	
	/**
	 * Clears a row by setting each cell state in that row to EMPTY.
	 * It then shifts each row above down one.
	 * @param r
	 */
	public void clearRow(int r) {
		for (int column=0; column<10; column++) {
			cells[r][column].setState(Cell.EMPTY);
			cells[r][column].setColor(null);
		}
		
		for(int row=r; row>0; row--) {
			for (int column=0; column<10; column++) {
				Cell cell = cells[row][column];
				Cell above = cells[row-1][column];
				if(above.getState() != Cell.TETROMINO) {
					cell.setState(above.getState());
					cell.setColor(above.getColor());
				}
			}
		}
	}
	
	// 
	// 
	/**
	 * Scans for cells which represent the shadow and sets their 
	 * state to EMPTY.
	 */
	private void clearShadow() {
		for (int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				if(getCell(row,column).getState() == Cell.SHADOW) {
					getCell(row,column).setState(Cell.EMPTY);
				}
			}
		}
	}
	
	/**
	 * Updates the position of the shadow, which is a gray 
	 * version of the tetromino at the point where there
	 * the tetromino would go on a hard drop.
	 */
	private void updateShadowPosition() {
		clearShadow();
		shadow.newShape(shape.getShape());

		for (int block = 0; block < 4; block++) {
			shadow.setRow(block, shape.getBlockRow(block));
			shadow.setColumn(block, shape.getBlockColumn(block));
		}

		hardDrop(shadow);
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shadow.getBlockRow(block);
			column = shadow.getBlockColumn(block);
			cell = getCell(row, column);
			cell.setState(Cell.SHADOW);
		}
	}
	
	/**
	 *Sets cell states where tetromino was previously  
	 *to EMPTY and sets the cells on the next row to Cell.TETROMINIO
	 */
	private void updateTetrominoPosition() {
		Cell cell = null;
		
		// Clear old position
		for(int row=0; row<22; row++) {
			for(int column=0; column<10; column++) {
				cell = getCell(row, column);
				if(cell.getState() == cell.TETROMINO) {
					cell.setState(Cell.EMPTY);
				}
				
			}
		}
		
		// Draw shape
		for (int block = 0; block < 4; block++) {
			try {
				row = shape.getBlockRow(block);
				column = shape.getBlockColumn(block);
				cell = getCell(row, column);
				cell.setState(Cell.TETROMINO);
				cell.setColor(shape.getColor());
			} catch(ArrayIndexOutOfBoundsException ex) {
				continue;
			}
		}
	}
	
	/**
	 * Checks for full rows, i.e. rows where each column has a 
	 * NOT_EMPTY cell state, and clears them.
	 */
	private void updateGrid() {
		int clearCount = 0;
		int fullCellCount = 0;
		for(int row=21; row>0; row--) {
			fullCellCount = 0;
			for(int column=0; column<10; column++) {
				if(cells[row][column].getState() == Cell.NOT_EMPTY) {
					fullCellCount++;
				}
			}
			if(fullCellCount == 10) {
				clearRow(row);
				clearCount++;
			}
		}
		
		if(clearCount > 0) {
			// Assign appropriate amount of points
			int clearPoints = score.getClearPoints();
			int bonusPoints = score.getBonusPoints();
			int points = clearPoints*clearCount + bonusPoints*(clearCount-1);
			score.updatePoints(points);
		}
	}

	/**
	* Checks for full rows, clears any and updates tetromino and its shadow position.
	 */
	public void update() {
		updateGrid();
		updateShadowPosition();
		updateTetrominoPosition();
	}
	
	/**
	 * 
	 Moves the tetromino all the way down to a point where it 
	 collides with a dropped piece or the bottom row.
	 * @param shape
	 */
	public void hardDrop(ShapeModel shape) {
		boolean collision = false;
		while(!collision) {
			collision = nextLine(shape);
		}
	}
	
	/**
	 * 
	 Saves the grid colors of the current tetromino position. 
	 For each block it sets its corresponding cell state to NOT_EMPTY 
	 and generates a random tetromino which spawns on the top row.
	 */
	public void newShape() {
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			column = shape.getBlockColumn(block);
			cell = getCell(row, column);
			cell.setState(Cell.NOT_EMPTY);
			cell.setColor(shape.getColor());
		}
		shape.randomShape();
	}
	
	/**
	 * 
	 Try to move the tetromino to the next line.
	 * @param shape  instance to move to next line
	 * @return whether a new shape was generated
	 */
	public boolean nextLine(ShapeModel shape) {
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			try {
				cell = getCell(row+1, shape.getBlockColumn(block));
				if(cell.getState() == Cell.NOT_EMPTY) {
					if(shape.getType() == Type.CURRENT_SHAPE)
						if(row > 2) 
							newShape();
						else
							controller.gameState = GameController.GameStates.STOPPED;
					return true;
				}
			} catch (IndexOutOfBoundsException ex) {
				// Tetromino is at bottom of grid
				if(shape.getType() == Type.CURRENT_SHAPE)
					newShape();
				return true;
			}
		}

		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			cell = getCell(row, shape.getBlockColumn(block));
			shape.setRow(block, row+1);
		}
		
		return false;		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int row=0; row<22; row++) {
			for(int column=0; column<10; column++) {
				sb.append(cells[row][column].getState() + "\t");
			}
			sb.append("\n");
		}
		for(int i=0;i<10;i++) {
			sb.append("\n");
		}
		return sb.toString();
	}
}