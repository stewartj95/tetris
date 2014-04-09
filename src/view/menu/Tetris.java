package view.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import view.game.GameView;
import view.game.InfoView;
import view.game.ShapeView;

public class Tetris extends JFrame implements KeyListener {
	
	private GameController gameController;
	private JLabel startLbl;
	
	public Tetris() {
		gameController = new GameController(this);
		initGUI();
	}
	
	private void initGUI() {
		// Initialise GUI
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		// Initialise & add to JFrame a GridBagLayout component panel 
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		
		// Add key listener
		addKeyListener(this);
		requestFocus();
		
		// Set layout
		setLocationRelativeTo(null);

		// Load game info view
		InfoView infoView = gameController.getInfoView();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(infoView, gbc);
		
		// Load game view
		GameView gameView = gameController.getGameView();
		gameView.setSize(300, 600);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(gameView, gbc);
		
		this.pack();
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
			startLbl.setText("");
			remove(startLbl);
			gameController.startGame();
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	public static void main(String[] args) {
		new Tetris();
	}
	
}