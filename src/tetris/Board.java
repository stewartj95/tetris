package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener, KeyListener {
	Timer timer = new Timer(5, this);
	Grid grid = new Grid();
	
	Shape tetromino;

	int w = 40, h = 36, x = 200;
	double y = h, velY=0.3;
	int row = 0, column = 0;
	JLabel[] debugLabels = {new JLabel("Block 1 row:    column:    "),
							new JLabel("Block 2 row:    column:    "),
							new JLabel("Block 3 row:    column:    "),
							new JLabel("Block 4 row:    column:    ")};
	JLabel gameOverLabel, levelLabel, scoreLabel;
	private Score score;
	
	public Board() {
		setFocusTraversalKeysEnabled(false); 
		setFocusable(true);
		addKeyListener(this);
		setLayout(new BorderLayout());
//		for(JLabel label : debugLabels) {
//			add(label);
//		}
		score = new Score();
		tetromino = new Shape(w, h);
		gameOverLabel = new JLabel();
		gameOverLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		levelLabel = new JLabel("Level : " + score.getLevel());
		levelLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		scoreLabel = new JLabel("Score: " + score.getScore());
		scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,3));
		scorePanel.add(levelLabel);
		scorePanel.add(new JLabel(""));
		scorePanel.add(scoreLabel);
		add(scorePanel, BorderLayout.NORTH);
		add(gameOverLabel, BorderLayout.CENTER);
	}

	public void start() {
		tetromino.randomShape();
		gameOverLabel.setText("");
		timer.start();
		score = new Score();
		velY = score.getLevelSpeed();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		levelLabel.setText("Level: " + score.getLevel());
		scoreLabel.setText("Score: " + score.getScore());
		
		// Draw dropped pieces.
		Cell[][] cells = grid.getCells();
		for(int row=0; row<22; row++) {
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
//			debugLabels[i].setText("Block "+i+" row: "+row+" column: " + column);
			tetromino.setRow(i,row);
			tetromino.setColumn(i,column);
//			System.out.println(grid);
		}
		
		levelLabel.setText("Level: " + score.getLevel());
		
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
		int[][] oldCoordinates = tetromino.getShapeCoordinates();
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
				velY = 10;
				break;
			case KeyEvent.VK_ESCAPE:
				timer.stop();
				grid = new Grid();
				start();
				break;
			case KeyEvent.VK_DOWN:
				velY = 10;
				break;
		}
	}

	public void keyReleased(KeyEvent event) {
	}
	public void keyTyped(KeyEvent event) {
	}
	
	public boolean collision() {
		boolean collision = false;
		for(int block=0; block<4; block++) {
			try {
				int row = tetromino.getRow(block);
				int column = tetromino.getColumn(block);
				int nextCellState = grid.getCell(row+1, column).getState();
				if(nextCellState == Cell.NOT_EMPTY) {
					collision = true;
					break;
				}
			} catch (IndexOutOfBoundsException exception) {
				continue;
			}
		}
		int rowsCleared = grid.updateRows();
		if(rowsCleared > 0) {
			int points = 50 * rowsCleared; 
			score.updateScore(points);
			velY = score.getLevelSpeed();
		}
		return collision;
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
		if (y + tetromino.maxY() * h < (getHeight()-h) && !collision()) {
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
			velY = score.getLevelSpeed();
			x = 200;
			if(grid.getCell(1, 5).getState() == Cell.NOT_EMPTY) {
				// GAME OVER
				timer.stop();
				gameOverLabel.setText("GAME OVER! Press ESC to restart.");
			}
		}
		tetromino.setX(x);
		tetromino.setY((int)y);
		repaint();
	}
}
