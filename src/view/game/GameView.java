package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.menu.Tetris;
import model.Cell;
import model.Grid;

public class GameView extends JPanel {
	int boardWidth, boardHeight;
	int shapeWidth, shapeHeight;
	double y = 0;
	int row = 0, column = 0;
	JLabel[] debugLabels = {new JLabel("Block 1 row:    column:    "),
							new JLabel("Block 2 row:    column:    "),
							new JLabel("Block 3 row:    column:    "),
							new JLabel("Block 4 row:    column:    ")};
	JLabel gameOverLabel, levelLabel, scoreLabel;
	Grid grid;
	
	public GameView(Tetris parent, Grid grid) {
		setFocusTraversalKeysEnabled(false); 
		setFocusable(true);
		setLayout(new BorderLayout());

		gameOverLabel = new JLabel();
		gameOverLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		levelLabel = new JLabel();
		levelLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		scoreLabel = new JLabel();
		scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,3));
		scorePanel.add(levelLabel);
		scorePanel.add(new JLabel(""));
		scorePanel.add(scoreLabel);

		add(scorePanel, BorderLayout.NORTH);
		add(gameOverLabel, BorderLayout.CENTER);
	}
	
	public void setSize(int width, int height) {
		shapeWidth = width / 10;
		shapeHeight = height / 22;
		y = shapeHeight;

		boardWidth = width;
		boardHeight = height;

		setMaximumSize(new Dimension(boardWidth, boardHeight));
		setMinimumSize(new Dimension(boardWidth, boardHeight));
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public void updateLevel(int level) {
		levelLabel.setText("Level: " + level);
	}
	
	public void updateScore(int score) {
		scoreLabel.setText("Score: " + score);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			Cell[][] cells = grid.getCells();
			for(int row=0; row<22; row++) {
				for(int column=0; column<10; column++) {
					Cell cell = cells[row][column];
					if(cell.getState() != Cell.EMPTY) {
						int x = column * shapeWidth;
						int y = row * shapeHeight;
						g.setColor(cell.getColor());
						g.fillRect(x, y, shapeWidth, shapeHeight);
						g.setColor(Color.BLACK);
						g.drawRect(x, y, shapeWidth, shapeHeight);
					}
				}
			}
			g.setColor(Color.RED);
		} catch (NullPointerException ex) {
			// Ignore
		}
	}
}