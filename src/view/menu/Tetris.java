package view.menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import view.game.GameView;

public class Tetris extends JFrame implements KeyListener {
	
	private GameController gameController;
	private JLabel startLbl;
	private MainMenu menu;
	
	public Tetris() {
		gameController = new GameController(this);
		initGUI();
	}
	
	private void initGUI() {
		startLbl = new JLabel("Press ENTER to start.");
		startLbl.setFont(new Font("Tahoma", Font.BOLD, 25));
		add(startLbl);

		setTitle("Tetris");
		setSize(300, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		addKeyListener(this);
		requestFocus();
		
		menu = new MainMenu();
		GameView gameView = gameController.getGameView();
		gameView.setSize(280, 480);
		add(gameView);
	}

	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
			startLbl.setText("");
			gameController.startGame();
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	public static void main(String[] args) {
		new Tetris();
	}
	
}