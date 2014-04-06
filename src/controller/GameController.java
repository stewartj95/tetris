package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import view.game.GameView;
import view.menu.Tetris;
import model.Cell;
import model.Grid;
import model.ScoreModel;
import model.ShapeModel;

public class GameController implements ActionListener, KeyListener {
	
	private enum GameStates {STARTED, STOPPED};
	private GameView gameView; 
	private Timer timer = new Timer(1, this);
	private Grid grid;
	private ScoreModel scoreModel;
	private ShapeModel shapeModel;
	private GameStates gameState = GameStates.STOPPED;
	private int row = 2, elapsed = 0, speed = 500;
	
	public GameController(Tetris parent) {
		grid = new Grid(scoreModel);
		gameView = new GameView(parent, grid);
		gameView.addKeyListener(this);
		gameView.requestFocusInWindow();
		gameView.requestFocus();
	}
	
	public GameView getGameView() {
		return gameView;
	}
	
	public void startGame() {
		scoreModel = new ScoreModel();
		grid = new Grid(scoreModel);
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
			row = grid.getShapeModel().getRow();
			if (elapsed == speed) {
				grid.nextLine(grid.getShapeModel());
				elapsed = 0;
			}
			speed = scoreModel.getLevelSpeed();
			gameView.repaint();
			elapsed += 10;
		}
	}

}
