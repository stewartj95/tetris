package tetris;

import javax.swing.JFrame;

public class Tetris extends JFrame {
	
	public Tetris() {
		setTitle("Tetris");
		setSize(417, 792);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setResizable(false);
		Board board = new Board();
		add(board);
	}
	
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
	}

}
