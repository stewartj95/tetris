package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	Clip audio;
	
	public GameView(Tetris parent, Grid grid) {
		setFocusable(true);
		setLayout(new BorderLayout());

		gameOverLabel = new JLabel();
		gameOverLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		levelLabel = new JLabel();
		levelLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		scoreLabel = new JLabel();
		scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		add(gameOverLabel, BorderLayout.CENTER);
		setDoubleBuffered(true);
		setBackground(new Color(0xCBCBCB));
	}
	
	public void playSound() {
//		try {
//	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("audio/tetris_metal.wav").getAbsoluteFile());
//	        audio = AudioSystem.getClip();
//	        audio.open(audioInputStream);
//	        audio.start();
//	    } catch(Exception ex) {
//	        System.out.println("Error with playing sound.");
//	        ex.printStackTrace();
//	    }
	}
	
	public void stopSound() {
//		audio.stop();
//		audio.drain();
//		audio.close();
	}
	
	@Override
	public void setSize(int width, int height) {
		shapeWidth = width / 10;
		shapeHeight = height / 22;
		y = shapeHeight;

		boardWidth = width;
		boardHeight = height;

		setPreferredSize(new Dimension(boardWidth, boardHeight));
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			Cell[][] cells = grid.getCells();
			for(int row=0; row<22; row++) {
				g.setColor(new Color(0xA2A2A2));
//				g.drawLine(0, row*shapeHeight, boardWidth, row*shapeHeight);
				for(int column=0; column<10; column++) {
					g.setColor(new Color(0xA2A2A2));
					Cell cell = cells[row][column];
					if(cell.getState() != Cell.EMPTY) {
						int x = column * shapeWidth;
						int y = row * shapeHeight;
						g.setColor(cell.getFillColor());
						g.fillRect(x, y, shapeWidth, shapeHeight);
						g.setColor(cell.getOutlineColor().darker().darker());
						g.drawRect(x, y, shapeWidth, shapeHeight);
					}
//					g.drawLine(column*shapeWidth, 0, column*shapeWidth, boardHeight);
				}
			}
		} catch (NullPointerException ex) {
			// Ignore
		}
	}
}