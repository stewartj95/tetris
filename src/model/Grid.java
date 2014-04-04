package model;

import java.awt.Color;

/*
 * Grid contains the model of the Tetris grid. It contains information
 * and each cell, including its current state and colour. 
 */

public class Grid {
	
	private Cell cells[][];
	private ShapeModel shapeModel;
	private int row;
	private int column;
	private ScoreModel scoreManager;
	
	public Grid(ScoreModel scoreManager) {
		this.scoreManager = scoreManager;
		shapeModel = new ShapeModel();
		cells = new Cell[22][10];
		for(int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				cells[row][column] = new Cell(Cell.EMPTY);
			}
		}
	}
	
	public ShapeModel getShapeModel() {
		return shapeModel;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public void move(int direction) {
		int column = shapeModel.getColumn(direction);
		boolean validColumn = false;
		if(direction == ShapeModel.RIGHT) 
			validColumn = column < 9;
		if(direction == ShapeModel.LEFT) {
			validColumn = column > 0;
		}
		if (validColumn && cellBesideIsEmpty(direction)) {
			column = 0;
			for (int block = 0; block < 4; block++) {
				column = shapeModel.getBlockColumn(block);
				row = shapeModel.getBlockRow(block);
				Cell cell = getCell(row, column);
				cell.setState(Cell.EMPTY);
				cell.setColor(null);
				if(direction == ShapeModel.LEFT)
					shapeModel.setColumn(block, column-1);
				else if (direction == ShapeModel.RIGHT) 
					shapeModel.setColumn(block, column+1);
			}
		}
	}

	// Sets the state of the cells to empty which indicate the 
	// last position of the teromino.
	private void emptyCells() {
		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			column = shapeModel.getBlockColumn(block);
			getCell(row, column).setState(Cell.EMPTY);;
			getCell(row, column).setColor(null);
		}
	}
	
	public void rotate(int direction) {
		if(direction == ShapeModel.RIGHT) {
			emptyCells();
			shapeModel.rotate(shapeModel.RIGHT);
		} else if (direction == ShapeModel.LEFT) {
			emptyCells();
			shapeModel.rotate(shapeModel.LEFT);
		}
		update();
	}
	
	public boolean cellBesideIsEmpty(int DIRECTION) {
		int cellState = Cell.EMPTY;
		int row = 0, column = 0, block = 0;
		while(cellState != Cell.NOT_EMPTY && block < 4) {
			column = shapeModel.getBlockColumn(block);
			row = shapeModel.getBlockRow(block);
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
	
	private void updateTetrominoPosition() {
		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			column = shapeModel.getBlockColumn(block);

			Cell cell = getCell(row-1, column);
			if(cell.getState() != Cell.NOT_EMPTY) {
				cell.setState(Cell.EMPTY);
				cell.setColor(null);
			}
		}
		
		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			column = shapeModel.getBlockColumn(block);
			
			Cell cell = getCell(row, column);
			cell = getCell(row, column);
			cell.setState(Cell.TETROMINO);
			cell.setColor(shapeModel.getColor());
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
		updateTetrominoPosition();
		updateGrid();
	}
	
	public void hardDrop() {
		boolean collision = false;
		while(!collision) {
			collision = nextLine();
		}
	}
	
	public void newShape() {
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			column = shapeModel.getBlockColumn(block);
			cell = getCell(row, column);
			cell.setState(Cell.NOT_EMPTY);
			cell.setColor(shapeModel.getColor());
		}
		shapeModel.randomShape();
	}
	
	// Try to move the tetromino to the next line.
	// Returns whether a new tetromino was generated.
	public boolean nextLine() {
		Cell cell = null;
		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			try {
				cell = getCell(row+1, shapeModel.getBlockColumn(block));
				if(cell.getState() == Cell.NOT_EMPTY) {
					newShape();
					return true;
				}
			} catch (IndexOutOfBoundsException ex) {
				// Tetromino is at bottom of grid
				newShape();
				return true;
			}
		}

		for (int block = 0; block < 4; block++) {
			row = shapeModel.getBlockRow(block);
			cell = getCell(row, shapeModel.getBlockColumn(block));
			cell.setState(Cell.EMPTY);
			shapeModel.setRow(block, row+1);
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