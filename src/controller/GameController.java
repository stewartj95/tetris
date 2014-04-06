package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import view.game.GameView;
import view.game.NextShapeView;
import view.menu.Tetris;
import model.Cell;
import model.Grid;
import model.ScoreModel;
import model.ShapeModel;
import model.ShapeModel.Tetrominoes;

public class GameController implements ActionListener, KeyListener {
	
	public enum GameStates {STARTED, STOPPED};
	private GameView gameView; 
	private Timer timer = new Timer(1, this);
	private Grid grid;
	private ScoreModel score;
	private ShapeModel shapeModel;
	private NextShapeView nextShapeView;
	public GameStates gameState = GameStates.STOPPED;
	private int elapsed = 0;
	private Tetrominoes nextShape = Tetrominoes.NOSHAPE;
	
	public GameController(Tetris parent) {
		nextShapeView = new NextShapeView();
		gameView = new GameView(parent, grid);
		gameView.addKeyListener(this);
		gameView.requestFocusInWindow();
		gameView.requestFocus();
	}
	
	public GameView getGameView() {
		return gameView;
	}
	
	public NextShapeView getNextShapeView() {
		return nextShapeView;
	}
	
	public void startGame() {
		score = new ScoreModel(1);
		grid = new Grid(score, this);

		gameView.setGrid(grid);
		shapeModel = grid.getShapeModel();

		shapeModel.randomShape();
		timer.start();
		gameState = GameStates.STARTED;
	}
	
	public void stopGame() {
		timer.stop();
		gameState = GameStates.STOPPED;
	}

	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				grid.move(ShapeModel.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				grid.move(ShapeModel.RIGHT);
				break;
			case KeyEvent.VK_Z:
				grid.rotate(ShapeModel.RIGHT);
				break;
			case KeyEvent.VK_X:
				grid.rotate(ShapeModel.LEFT);
				break;
			case KeyEvent.VK_UP:
				grid.rotate(ShapeModel.LEFT);
				break;
			case KeyEvent.VK_SPACE:
				grid.hardDrop(grid.getShapeModel());
				break;
			case KeyEvent.VK_DOWN:
				grid.nextLine(grid.getShapeModel());
				break;
			case KeyEvent.VK_ESCAPE:
				stopGame();
			case KeyEvent.VK_ENTER:
				if(gameState == gameState.STOPPED) {
					startGame();
				}
				break;
		}
	}

	public void keyReleased(KeyEvent event) {}
	public void keyTyped(KeyEvent event) {}
	
	// Game loop
	// Update grid. If required time elapsed (represents game speed, elapsed == delay)
	// then move down one row. Repaint the game view. Add 10 to elapsed.
	@Override
	public void actionPerformed(ActionEvent e) {
		if(gameState == GameStates.STARTED) {
			grid.update();
			if (elapsed >= score.getLevelSpeed()) {
				grid.nextLine(grid.getShapeModel());
				elapsed = 0;
			}
			gameView.repaint();
			elapsed += 1;
			gameView.updateScore(score.getPoints());
			gameView.updateLevel(score.getLevel());
			nextShape = Tetrominoes.values()[shapeModel.getNextShapeIndex()];
			nextShapeView.setNextShape(nextShape);
		}
		System.out.println(grid);
	}

}