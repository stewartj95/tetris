package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import view.game.GameView;
import view.game.InfoView;
import view.game.ShapeView;
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
	private InfoView infoView;
	public GameStates gameState = GameStates.STOPPED;
	private int elapsed = 0;
	private Tetrominoes nextShape = Tetrominoes.NOSHAPE;
	private boolean holdAllowed = true;
	
	public GameController(Tetris parent) {
		gameView = new GameView(parent, grid);
		gameView.addKeyListener(this);
		gameView.requestFocusInWindow();
		gameView.requestFocus();
		infoView = new InfoView();
	}
	
	public GameView getGameView() {
		return gameView;
	}
	
	public InfoView getInfoView() {
		return infoView;
	}
	
	public void startGame() {
		score = new ScoreModel(1);
		grid = new Grid(score, this);

		gameView.setGrid(grid);
		shapeModel = grid.getShapeModel();

		shapeModel.randomShape();
		timer.start();
		gameState = GameStates.STARTED;
		gameView.playSound();
	}
	
	public void stopGame() {
		timer.stop();
		gameState = GameStates.STOPPED;
		gameView.stopSound();
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
				elapsed = 0;
				break;
			case KeyEvent.VK_DOWN:
				grid.nextLine(grid.getShapeModel());
				elapsed = 0;
				break;
			case KeyEvent.VK_ESCAPE:
				gameState = GameStates.STOPPED;
				infoView.getHeldShapeView().setShape(Tetrominoes.NOSHAPE);
				gameView.stopSound();
				break;
			case KeyEvent.VK_ENTER:
				if(gameState == gameState.STOPPED) {
					startGame();
				}
				break;
			case KeyEvent.VK_SHIFT:
				// Hold shape
				if(grid.canHoldShape()) {
					grid.holdShape();
					infoView.getHeldShapeView().setShape(grid.getHeldShape());
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
			elapsed += 1;
			nextShape = Tetrominoes.values()[shapeModel.getNextShapeIndex()];
			infoView.getNextShapeView().setShape(nextShape);
			gameView.repaint();
			infoView.updateLevel(score.getLevel());
			infoView.updateScore(score.getPoints());
		} else {
			gameView.stopSound();
		}
	}

}