package tetris;

import java.awt.Color;

public class Grid {
	
	
	private Cell cells[][];
	
	public Grid() {
		cells = new Cell[22][10];
		for(int row = 0; row < 22; row++) {
			for (int column = 0; column < 10; column++) {
				cells[row][column] = new Cell(Cell.EMPTY);
			}
		}
	}
	
	public Cell[][] getCells() {
		return this.cells;
	}
	
	public Cell getCell(int row, int column) {
		return cells[row][column];
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
	
	// Checks if a row is clear.
	public boolean isClearRow(int row) {
		int clearCount = 0;
		for(int column=0; column<10; column++) {
			if(cells[row][column].getState() == Cell.EMPTY) {
				clearCount++;
			}
		}
		return clearCount == 10;
	}
	
	// Clears a row by setting each cell state to EMPTY.
	public void clearRow(int r) {
		// Set current row to row-1
		for (int column=0; column<10; column++) {
			cells[r][column].setState(Cell.EMPTY);
			cells[r][column].setColor(null);
		}
		
		for(int row=r; row>0; row--) {
			for (int column=0; column<10; column++) {
				Cell cell = cells[row][column];
				Cell above = cells[row-1][column];
				cell.setState(above.getState());
				cell.setColor(above.getColor());
			}
		}
	}
	
	// Checks for full rows. If it finds a full row it clears it 
	// and shifts the contents every other row down one.
	public int updateRows() {
		int fullCellCount = 0;
		int clearCount = 0;
		for(int row=20; row>0; row--) {
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
		return clearCount;
	}
	
}
