package entity;

public class Projectile {
	   public int x;
	public int y;
	public int width;
	public int height;
	int speed;
	    boolean active = true;

	    public Projectile(int x, int y, int width, int height, int speed) {
	        this.x = x;
	        this.y = y;
	        this.width = width;
	        this.height = height;
	        this.speed = speed;
	    }

	    public void move() {
	        x += speed; // Move projectile to the right
	    }

	    public boolean collision(Enemy enemy) {
	        return x < enemy.x + enemy.width &&
	               x + width > enemy.x &&
	               y < enemy.y + enemy.height &&
	               y + height > enemy.y;
	    }
}
