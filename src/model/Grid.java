package model;

import java.awt.Color;

import model.ShapeModel.Type;

/*
 * Grid contains the model of the Tetris grid. It contains information
 * and each cell, including its current state and colour. 
 */

public class Grid {
	
	private Cell cells[][];
	private ShapeModel shape;
	private ShapeModel shadow;
	private int row;
	private int column;
	private ScoreModel scoreManager;
	
	public Grid(ScoreModel scoreManager) {
		this.scoreManager = scoreManager;
		shape = new ShapeModel(Type.CURRENT_SHAPE);
		shadow = new ShapeModel(Type.SHADOW);
		cells = new Cell[22][10];
		for(int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				cells[row][column] = new Cell(Cell.EMPTY);
			}
		}
	}
	
	public ShapeModel getShapeModel() {
		return shape;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
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

	// Sets the state of the cells to empty which indicate the 
	// last position of the teromino.
	private void emptyCells() {
		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			column = shape.getBlockColumn(block);
			getCell(row, column).setState(Cell.EMPTY);;
			getCell(row, column).setColor(null);
		}
	}
	
	public void rotate(int direction) {
		if(direction == ShapeModel.RIGHT) {
			emptyCells();
			shape.rotate(shape.RIGHT, cells);
		} else if (direction == ShapeModel.LEFT) {
			emptyCells();
			shape.rotate(shape.LEFT, cells);
		}
		update();
	}
	
	public boolean cellBesideIsEmpty(int DIRECTION) {
		int cellState = Cell.EMPTY;
		int row = 0, column = 0, block = 0;
		while(cellState != Cell.NOT_EMPTY && block < 4) {
			column = shape.getBlockColumn(block);
			row = shape.getBlockRow(block);
			if(DIRECTION == ShapeModel.LEFT && column > 0)
				column--;
			else if(DIRECTION == ShapeModel.RIGHT && column < 9)
				column++;
			cellState = getCell(row, column).getState();
			block++;
		}
		return cellState != Cell.NOT_EMPTY;
	}
	
	public Cell getCell(int row, int column) {
		return cells[row][column];
	}
		
	// Checks if a row is clear.
	public boolean isRowClear(int row) {
		int clearCount = 0;
		for(int column=0; column<10; column++) {
			if(cells[row][column].getState() == Cell.EMPTY) {
				clearCount++;
			}
		}
		return clearCount == 10;
	}
	
	// Clears a row by setting each cell state to EMPTY.
	// It then shifts each row above down one.
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
	
	private void clearShadow() {
		for (int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				if(getCell(row,column).getState() == Cell.SHADOW) {
					getCell(row,column).setState(Cell.EMPTY);
				}
			}
		}
	}
	
	// Updates the position of the shadow, which is a gray
	// version of the tetromino at the point where there
	// would be a collision for the current row.
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
	
	private void updateTetrominoPosition() {
		for (int block = 0; block < 4; block++) {
			try {
				row = shape.getBlockRow(block);
				column = shape.getBlockColumn(block);
	
				Cell cell = getCell(row-1, column);
				if(cell.getState() != Cell.NOT_EMPTY) {
					cell.setState(Cell.EMPTY);
					cell.setColor(null);
				}
			} catch(ArrayIndexOutOfBoundsException ex) {
				continue;
			}
		}
		
		for (int block = 0; block < 4; block++) {
			try {
				row = shape.getBlockRow(block);
				column = shape.getBlockColumn(block);
				
				Cell cell = getCell(row, column);
				cell = getCell(row, column);
				cell.setState(Cell.TETROMINO);
				cell.setColor(shape.getColor());
			} catch (ArrayIndexOutOfBoundsException ex) {
				continue;
			}
		}
	}
	
	private void updateGrid() {
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
			}
		}
	}
	
	// 1. Updates tetromino grid position.
	// 2. Checks for full rows. If it finds a full row it clears it 
	//    and shifts the contents of every other row down one.
	public void update() {
		updateShadowPosition();
		updateTetrominoPosition();
		updateGrid();
	}
	
	public void hardDrop(ShapeModel shape) {
		boolean collision = false;
		while(!collision) {
			collision = nextLine(shape);
		}
	}
	
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
	
	// Try to move the tetromino to the next line.
	// Returns whether a new tetromino was generated.
	public boolean nextLine(ShapeModel shape) {
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shape.getBlockRow(block);
			try {
				cell = getCell(row+1, shape.getBlockColumn(block));
				if(cell.getState() == Cell.NOT_EMPTY) {
					if(shape.getType() == Type.CURRENT_SHAPE)
						newShape();
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
			cell.setState(Cell.EMPTY);
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