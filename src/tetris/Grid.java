package tetris;

public class Grid {
	
	public static final int EMPTY = 0;
	public static final int NOT_EMPTY = 1;
	
	private int coordinates[][];
	private int currentRow;
	
	public Grid() {
		coordinates = new int[21][10];
		for(int row = 0; row < 21; row++) {
			for (int column = 0; column < 10; column++) {
				coordinates[row][column] = EMPTY;
			}
		}
	}
	
	public int[][] getGrid() {
		return this.coordinates;
	}
	
	public void setNotEmpty(int row, int column) {
		coordinates[row][column] = NOT_EMPTY;
	}
	
	public void setEmpty(int row, int column) {
		coordinates[row][column] = EMPTY;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int row=0; row<21; row++) {
			for(int column=0; column<10; column++) {
				sb.append(getCellState(row, column) + "\t");
			}
			sb.append("\n");
		}
		for(int i=0;i<10;i++) {
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public int getCellState(int row, int column) {
		return coordinates[row][column];
	}
	
	public boolean isClearRow(int row) {
		int clearCount = 0;
		for(int column=0; column<10; column++) {
			if(coordinates[row][column] == 0) {
				clearCount++;
			}
		}
		return clearCount == 10;
	}
	
	public void clearRow(int r) {
		// Set current row to row-1
//		for(int r=row; r>0; r--) {
//			for(int column = 0; column < 10; column++) {
//				coordinates[row][column] = coordinates[row-1][column];
//			}	
//		}
		
		for (int column=0; column<10; column++) {
			coordinates[r][column] = 0;
		}
		
		for(int row=r; row>0; row--) {
			for (int column=0; column<10; column++) {
				coordinates[row][column] = coordinates[row-1][column];
			}
		}
		
	}
	
	public int updateRows() {
		int total = 0;
		int clearCount = 0;
		for(int row=20; row>0; row--) {
			total = 0;
			for(int column=0; column<10; column++) {
				if(coordinates[row][column] == NOT_EMPTY) {
					total++;
				}
			}
			if(total == 10) {
				clearRow(row);
				clearCount++;
			}
		}
		return clearCount;
	}
	
}
