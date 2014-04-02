package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener, KeyListener {
	Timer timer = new Timer(10, this);
	Grid grid = new Grid();
	
	Shape tetromino;

	int w = 40, h = 36, x = 200, y = h, velY=2;
	int row = 0, column = 0;

	public Board() {
		setFocusTraversalKeysEnabled(false); 
		setFocusable(true);
		addKeyListener(this);
		tetromino = new Shape(w, h);
	}

	public void start() {
		tetromino.randomShape();
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw dropped pieces.
		Cell[][] cells = grid.getCells();
		for(int row=0; row<22; row++) {
//			g.setColor(Color.BLACK);
//			g.drawRect(0, row*h, getWidth(), 0);
			for(int column=0; column<10; column++) {
				Cell cell = cells[row][column];
				if(cell.getState() == Cell.NOT_EMPTY) {
					int x = column * w;
					int y = row * h;
					g.setColor(cell.getColor());
					g.fillRect(x, y, w, h);
					g.setColor(Color.BLACK);
					g.drawRect(x, y, w, h);
				}
			}
		}
		
		int row = 0;
		int column = 0;
		
		// Draw current tetromino
		for (int i = 0; i < 4; i++) {
//			System.out.println("Drawing block " + i);
			// Get X and Y values for current block
			int shapeX = tetromino.blockX(i);
			int shapeY = tetromino.blockY(i);
			
			// Draw block
			g.setColor(tetromino.getColor());
			g.fillRect(shapeX, shapeY, w, h);
			g.setColor(Color.BLACK);
			g.drawRect(shapeX, shapeY, w, h);
			row = shapeY / 36; column = shapeX / 40;
			tetromino.setRow(i,row);
			tetromino.setColumn(i,column);
//			System.out.println(grid);
		}
		
		// Draw bottom border
	}
	
	public boolean cellBesideIsEmpty(int DIRECTION) {
		int cellState = Cell.EMPTY;
		int row = 0, column = 0, block = 0;
		while(cellState == Cell.EMPTY && block < 4) {
			column = tetromino.getColumn(block);
			row = tetromino.getRow(block);
			if(DIRECTION == Shape.LEFT && column > 0)
				column--;
			else if(DIRECTION == Shape.RIGHT && column < 9)
				column++;
			cellState = grid.getCell(row, column).getState();
			block++;
		}
		return cellState == Cell.EMPTY;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (x - Math.abs(w * tetromino.minX()) > 0 && cellBesideIsEmpty(Shape.LEFT)) {
					x -= w;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (x + w * tetromino.maxX() < getWidth() && cellBesideIsEmpty(Shape.RIGHT)) {
					x += w;
				}
				break;
			case KeyEvent.VK_Z:
				tetromino.rotate(Shape.RIGHT);
				break;
			case KeyEvent.VK_X:
				tetromino.rotate(Shape.LEFT);
				break;
			case KeyEvent.VK_UP:
				tetromino.rotate(Shape.LEFT);
				break;
			case KeyEvent.VK_SPACE:
				velY = 20;
				break;
		}
	}

	public void keyReleased(KeyEvent event) {}
	public void keyTyped(KeyEvent event) {}
	
	public boolean collision() {
		for(int block=0; block<4; block++) {
			try {
				int row = tetromino.getRow(block);
				int column = tetromino.getColumn(block);
				int nextCellState = grid.getCell(row+1, column).getState();
				if(nextCellState == Cell.NOT_EMPTY) {
					return true;
				}
			} catch (IndexOutOfBoundsException exception) {
				continue;
			}
		}
		grid.updateRows();
		return false;
	}

	@Override
	// GAME LOOP
	// IF current tetromino can go to next row THEN 
	// Update Y coordinates of current tetromino are updated. 
	// OTHERWISE 
	// Set the state of the cells the tetromino currently 
	// occupies to NOT_EMPTY. Then generate a new tetromino and set 
	// of it to the coordinates to the top of the board.
	public void actionPerformed(ActionEvent event) {
		if (y + tetromino.maxY() * h < (getHeight()-40) && !collision()) {
			y += velY;
		} else {
			// Mark non-empty cells
			int row = 0, column = 0;
			for(int block=0; block<4; block++) {
				row = tetromino.getRow(block);
				column = tetromino.getColumn(block);
				Cell cell = grid.getCell(row, column);
				cell.setState(Cell.NOT_EMPTY);
				cell.setColor(tetromino.getColor());
			}
			tetromino.randomShape();
			y = h;
			velY = 2;
			x = 200;
		}
		tetromino.setX(x);
		tetromino.setY(y);
		repaint();
	}
}
