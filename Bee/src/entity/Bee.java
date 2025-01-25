package entity;

import java.awt.Image;

import Game.GamePanel;

public class Bee {
	public int x;
	public int y;
	public int width;
	public int height;
	public Image img;

	public Bee(Image img) {
		this.x = GamePanel.boardWidth / 8;
		this.y = GamePanel.boardHeight / 2;
		this.width = 60;
		this.height = 50;
		this.img = img;
	}

	public boolean collision(Pipe pipe) {
		return x < pipe.x + pipe.width && x + width > pipe.x && y < pipe.y + pipe.height && y + height > pipe.y;
	}
	
	public boolean collision(Enemy enemy) {
	    return x < enemy.x + enemy.width && x + width > enemy.x && y < enemy.y + enemy.height && y + height > enemy.y;
	}

}
