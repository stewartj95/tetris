package view.game;

import javax.swing.JPanel;

import model.ShapeModel;
import model.ShapeModel.Tetrominoes;

public class NextShapePanel extends JPanel {
	Tetrominoes tetromino;
	ShapeModel shape;

	public NextShapePanel(ShapeModel shape) {
		tetromino = Tetrominoes.NOSHAPE;
		this.shape = shape;
	}
	
	public void setNextPiece(Tetrominoes tetromino) {
		this.tetromino = tetromino;
	}
	
}
