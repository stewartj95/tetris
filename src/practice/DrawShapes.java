package practice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawShapes extends JPanel implements ActionListener, KeyListener {
	
	Timer timer = new Timer(5, this);
	int w = 40, h = 36;
	int x = 200, velX = 0;
	int y = h, velY = 0;
	JLabel xLabel;
	JLabel yLabel;
	
	public DrawShapes() {
		timer.start();
		setFocusTraversalKeysEnabled(true); // Ignores shift and tab keys etc
		setFocusable(true);
		addKeyListener(this);
		xLabel = new JLabel("X: ");
		yLabel = new JLabel("Y: ");
		add(xLabel);
		add(yLabel);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.RED);
		g.fillRect(x-w, y+h, w, h);

//		g.setColor(Color.BLUE);
//		g.fillRect(x-w, y, w, h);
//		
//		g.setColor(Color.YELLOW);
//		g.fillRect(x, y, w, h);
//
//		g.setColor(Color.ORANGE);
//		g.fillRect(x, y-h, w, h);
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		System.out.println("Key pressed!");
		switch(event.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			velY = 2;
			break;
		case KeyEvent.VK_LEFT:
			if(x > 40) {
				x -= w;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(x < 360) {
				x += w;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		System.out.println("Key released");
	}

	@Override
	public void keyTyped(KeyEvent event) {
		System.out.println("Key typed");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(y+h*2 >= getHeight()) {
			velY = 0;
		}
		y += velY;
		xLabel.setText("X: " + x);
		yLabel.setText("Y: " + y);
		repaint();
	}
	
	public static void main(String[] args) {
		DrawShapes shapes = new DrawShapes();
		JFrame frame = new JFrame();

		frame.setTitle("Draw Shapes");
		frame.setSize(417, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(shapes);
	}

}