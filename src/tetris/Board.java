package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener, KeyListener {
	Timer timer = new Timer(100, this);
	Grid grid = new Grid();
	Shape tetromino;

	int w = 40, h = 36, x = 200, y = h, velY = 2;
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
		g.setColor(Color.RED);
		
		int[][] coordinates = grid.getGrid();
		for(int row=0; row<21; row++) {
			for(int column=0; column<10; column++) {
				if(coordinates[row][column] == Grid.NOT_EMPTY) {
					int x = column * w;
					int y = row * h;
					g.fillRect(x, y, w, h);
					g.drawRect(x, y, w, h);
				}
			}
		}
		
		int row = 0;
		int column = 0;

		// Draw current tetromino
		coordinates = tetromino.getShapeCoordinates();
		for (int i = 0; i < 4; i++) {
			g.setColor(tetromino.getColor());
			
			// Get X and Y values for current block
			int shapeX = tetromino.blockX(i);
			int shapeY = tetromino.blockY(i);
			
			// Draw block
			g.fillRect(shapeX, shapeY, w, h);
			row = shapeY / 36; column = shapeX / 40;
			tetromino.setRow(i,row);
			tetromino.setColumn(i,column);
//			System.out.println(grid);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (x - Math.abs(w * tetromino.minX()) > 0) {
					x -= w;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (x + w * tetromino.maxX() < getWidth()) {
					x += w;
				}
				break;
			case KeyEvent.VK_UP:
				tetromino.rotate(Shape.LEFT);
				break;
			case KeyEvent.VK_ENTER:
				start();
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
				int nextCellState = grid.getCellState(row+1, column);
				if(nextCellState == Grid.NOT_EMPTY) {
					return true;
				}
			} catch (IndexOutOfBoundsException exception) {
				continue;
			}
		}
		int clearCount = grid.updateRows();
		return false;
	}

	@Override
	// Game loop
	public void actionPerformed(ActionEvent event) {
		if (y + tetromino.maxY() * h < (getHeight()-60) && !collision()) {
			y += h;
		} else {
			// Mark non empty cells
			int row = 0, column = 0;
			for(int block=0; block<4; block++) {
				row = tetromino.getRow(block);
				column = tetromino.getColumn(block);
				grid.setNotEmpty(row, column);
			}
			
			tetromino.randomShape();
			y = h;
			x = 200;
		}
		tetromino.setX(x);
		tetromino.setY(y);
		repaint();
	}
}