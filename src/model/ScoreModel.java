package model;

import java.util.HashMap;

public class ScoreModel {
	// This class provides level and scoring functionality.
	private int score = 0;
	private int level = 1;
	private Level[] levels;
	private int clearScore = 50;
	
	// Construct Score on a specified level.
	public ScoreModel(int level) {
		this.level = level;
		initLevels();
	}
	
	// Construct Score on level 1.
	public ScoreModel() {
		initLevels();
	}
	
	public void setClearScore(int clearScore) {
		this.clearScore = clearScore;
	}
	
	public int getClearScore() {
		return clearScore;
	}
	
	private void initLevels() {
		levels = new Level[15];
		levels[0] = new Level(0, 500);
		levels[1] = new Level(100, 480);
		levels[2] = new Level(300, 460);
		levels[3] = new Level(500, 440);
		levels[4] = new Level(700, 420);
		levels[5] = new Level(1000, 400);
		levels[6] = new Level(1200, 380);
		levels[7] = new Level(1500, 360);
		levels[8] = new Level(2000, 340);
		levels[9] = new Level(2500, 320);
		levels[10] = new Level(3000,300);
		levels[11] = new Level(3600, 280);
		levels[12] = new Level(4100, 260);
		levels[13] = new Level(4700, 240);
		levels[14] = new Level(5500, 220);
	}

	public void updateScore(int score) {
		this.score += score;
		// Check if player can progress to next level
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
	
	public int getLevelSpeed() {
		return levels[level-1].getSpeed();
	}
	
	class Level {
		private int requiredScore;
		private int speed;
		
		// This class represents a level. A level has a set 
		// speed and a minimum score required to reach this level.
		public Level(int requiredScore, int speed) {
			this.requiredScore = requiredScore;
			this.speed = speed;
		}
		
		public Level() {
			requiredScore = 0;
			speed = 500;
		}
		
		public int getRequiredScore() {
			return requiredScore;
		}
		
		public int getSpeed() {
			return speed;
		}
		
	}
	
}
