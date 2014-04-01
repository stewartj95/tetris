package tetris;

public class Coordinates {
	
	private int row;
	private int column;
	
	public Coordinates(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public Coordinates() {
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.row = column;
	}
	
}
