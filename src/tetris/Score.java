package tetris;

import java.util.HashMap;

public class Score {
	// This class provides level and scoring functionality.
	private int score = 0;
	private int level = 1;
	private Level[] levels;
	
	// Construct Score on a specified level.
	public Score(int level) {
		this.level = level;
		initLevels();
	}
	
	// Construct Score on level 1.
	public Score() {
		initLevels();
	}
	
	private void initLevels() {
		levels = new Level[15];
		levels[0] = new Level(0, 0.3);
		levels[1] = new Level(100, 0.4);
		levels[2] = new Level(300, 0.45);
		levels[3] = new Level(500, 0.5);
		levels[4] = new Level(700, 0.55);
		levels[5] = new Level(1000, 0.6);
		levels[6] = new Level(1400, 0.685);
		levels[7] = new Level(2000, 0.75);
		levels[8] = new Level(2600, 0.8);
		levels[9] = new Level( 3500, 0.9);
		levels[10] = new Level(5000, 1);
		levels[11] = new Level(7500, 1.1);
		levels[12] = new Level(12000, 1.2);
		levels[13] = new Level(17000, 1.3);
		levels[14] = new Level(25000, 1.4);
	}

	public void updateScore(int score) {
		this.score += score;
		// Check if player can progress to next level
		System.out.println(level);
		if(level < 15) {
			if(this.score >= levels[level].getRequiredScore()) {
				level++;
			}
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getLevelSpeed() {
		return levels[level-1].getSpeed();
	}
	
	class Level {
		
		private int requiredScore;
		private double speed;
		
		// This class represents a level. A level has a set 
		// speed, and a minimum score required to reach this level.
		public Level(int requiredScore, double speed) {
			this.requiredScore = requiredScore;
			this.speed = speed;
		}
		
		public Level() {
			requiredScore = 0;
			speed = 0.0;
		}
		
		public int getRequiredScore() {
			return requiredScore;
		}
		
		public double getSpeed() {
			return speed;
		}
		
	}
	
}
