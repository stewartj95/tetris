package view.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import view.game.GameView;
import view.game.NextShapeView;

public class Tetris extends JFrame implements KeyListener {
	
	private GameController gameController;
	private JLabel startLbl;
	private MainMenu menu;
	
	public Tetris() {
		gameController = new GameController(this);
		initGUI();
	}
	
	private void initGUI() {
		setTitle("Tetris");
		setSize(220, 580);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		addKeyListener(this);
		requestFocus();
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		menu = new MainMenu();	
		GameView gameView = gameController.getGameView();
		gameView.setSize(210, 500);
		NextShapeView nextShapeView = gameController.getNextShapeView();
		nextShapeView.setWidth(60);
		nextShapeView.setHeight(60);
		nextShapeView.setPreferredSize(new Dimension(60,60));
		add(nextShapeView, BorderLayout.NORTH);
		add(gameView, BorderLayout.CENTER);
		createBufferStrategy(2);
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