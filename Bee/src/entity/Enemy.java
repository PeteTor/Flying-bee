package entity;

import java.awt.Image;

public class Enemy {
	
	public int x;
	public int y;
	public int width;
	public int height;
	int speed;
	
	public boolean alive = true;
	public Image img;

	public Enemy(Image img, int x, int y, int width, int height, int speed) {
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = 60;
		this.height = 80;
		this.speed = speed;
	}

	// Move the enemy towards the bee from right to left
	public void move(Bee bee) {
		// Move the enemy closer to the bee's x position (right to left movement)
		if (x > bee.x) {
			x -= speed; // Move left towards the bee
		}

		// Move the enemy closer to the bee's y position (vertical movement)
		if (y < bee.y) {
			y += speed; // Move down towards the bee
		} else if (y > bee.y) {
			y -= speed; // Move up towards the bee
		}
	}
}
