package tetris;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tetris extends JFrame implements KeyListener{
	
	private Board board;
	private JLabel startLbl;
	private JPanel menu;
	
	public Tetris() {
		setTitle("Tetris");
		setSize(407, 786);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		addKeyListener(this);
		startLbl = new JLabel("Press ENTER to start.");
		startLbl.setFont(new Font("Tahoma", Font.BOLD, 25));
		add(startLbl);
		board = new Board();
	}
	
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
	}

	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("STARTING");
			startLbl.setText("");
			add(board);
			board.requestFocus();
			board.start();
		}
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {
		System.out.println("typed");
	}

}
