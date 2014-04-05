package view.menu;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
	
	private JLabel titleLabel;

	public MainMenu() {
		titleLabel = new JLabel("Tetris");
		add(titleLabel);
	}
	
	public void PaintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.RED);
		g.drawRect(20, 20, 200, 100);
	}
	
}
