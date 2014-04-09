package view.game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ShapeModel.Tetrominoes;

/* This class is responsible for displaying the following information at
 * the top of the screen:
 * - score
 * - level
 * - next shape
 * - held shape
 */

/**
 * @author Jonny
 */


public class InfoView extends JPanel {

private JLabel scoreLbl, levelLbl;
private ShapeView nextShapePnl, heldShapePnl;

	/**
	 * Construct an InfoPanel object which initialises the 
	 * game information view components.
	 */
	public InfoView() {
		// Initialise panel components
		scoreLbl = new JLabel();
		levelLbl = new JLabel();
		nextShapePnl = new ShapeView(50,50, "Next: ");
		heldShapePnl = new ShapeView(50,50, "Held: ");

		// Set layout and add components to JPanel
		GridLayout layout = new GridLayout(2,2);
		layout.setHgap(30);
		setLayout(layout); 
		add(scoreLbl);
		add(levelLbl);
		add(heldShapePnl);
		add(nextShapePnl);
	}
	
	/**
	 * Updates the score JLabel.
	 * @param score to display
	 */
	public void updateScore(int score) {
		scoreLbl.setText("Score: " + score);
	}
	
	/**
	 * Updates the level JLabel.
	 * @param level to display 
	 */
	public void updateLevel(int level) {
		levelLbl.setText("Level: " + level);
	}
	
	public ShapeView getHeldShapeView() {
		return heldShapePnl;
	}
	
	public ShapeView getNextShapeView() {
		return nextShapePnl;
	}
	
}
