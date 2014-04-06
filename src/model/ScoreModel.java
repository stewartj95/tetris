package model;

import java.util.HashMap;

public class ScoreModel {
	// This class provides level and scoring functionality.
	private Level[] levels;
	private int points = 0, level = 1, clearPoints = 100, bonusPoints = 50;
	
	// Construct Score on a specified level.
	public ScoreModel(int level) {
		this.level = level;
		initLevels();
	}
	
	// Construct Score on level 1.
	public ScoreModel() {
		initLevels();
	}
	
	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
	
	public int getBonusPoints() {
		return bonusPoints;
	}
	
	public void setClearPoints(int clearPoints) {
		this.clearPoints = clearPoints;
	}
	
	public int getClearPoints() {
		return clearPoints;
	}
	
	private void initLevels() {
		levels = new Level[15];
		levels[0] = new Level(0, 500);
		levels[1] = new Level(100, 480);
		levels[2] = new Level(300, 460);
		levels[3] = new Level(500, 440);
		levels[4] = new Level(700, 400);
		levels[5] = new Level(1000, 350);
		levels[6] = new Level(1200, 300);
		levels[7] = new Level(1500, 250);
		levels[8] = new Level(2000, 180);
		levels[9] = new Level(2500, 150);
		levels[10] = new Level(3000,80);
		levels[11] = new Level(3600, 60);
		levels[12] = new Level(4100, 40);
		levels[13] = new Level(4700, 30);
		levels[14] = new Level(5500, 20);
	}

	public void updatePoints(int points) {
		this.points += points;
		// Check if player can progress to next level
		if(level < 15) {
			if(this.points >= levels[level].getRequiredPoints()) {
				level++;
			}
		}
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLevelSpeed() {
		return levels[level-1].getSpeed();
	}
	
	class Level {
		private int requiredPoints;
		private int speed;
		
		// This class represents a level. A level has a set 
		// speed and a minimum score required to reach this level.
		public Level(int requiredPoints, int speed) {
			this.requiredPoints = requiredPoints;
			this.speed = speed;
		}
		
		public Level() {
			requiredPoints = 0;
			speed = 500;
		}
		
		public int getRequiredPoints() {
			return requiredPoints;
		}
		
		public int getSpeed() {
			return speed;
		}
		
	}
	
}
