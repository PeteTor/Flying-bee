package Game;

import entity.Bee;
import entity.Enemy;
import entity.Pipe;
import entity.Projectile;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

	// FRAME SIZE
	public static int boardWidth = 1200;
	public static int boardHeight = 800;

	// IMAGES
	Image backgroundImg;
	Image beeImg;
	Image topPipeImg;
	Image bottomPipeImg;
	Image enemyImg;

	// GAME LOGIC
	Bee bee;
	int velocityX = -4; /* Speed for moving pipes left */
	int velocityY = 0; /* Speed for bee movement */
	int gravity = 1;

	// ARRALYLIST FOR PIPES, ENEMIES AND PROJECTILES
	ArrayList<Pipe> pipes = new ArrayList<>();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<Projectile> projectiles = new ArrayList<>();
	Random random = new Random();

	// GAME LOOP TIMERS
	Timer gameLoop;
	Timer placePipeTimer;
	Timer spawnEnemyTimer;

	// FOR SOUND EFFECT
	Clip Effect;

	// GAME OVER
	boolean gameOver = false;
	double score = 0;
	double enemyKilled = 0;

	// Cool down for shooting
	private long lastShotTime = 0;
	private final long SHOOT_COOLDOWN = 1500;

	// FPS Tracking
	private int fps = 0; // Current FPS
	private int frameCount = 0; // Number of frames rendered in the current second
	private long lastFpsTime = System.currentTimeMillis(); // Last time the FPS was updated

	public GamePanel() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setFocusable(true);
		addKeyListener(this);

		try {
			// Load images from the source folder
			backgroundImg = ImageIO.read(new File("res/BeeBg.png"));
			beeImg = ImageIO.read(new File("res/beeMain.png"));
			topPipeImg = ImageIO.read(new File("res/Pillar1.png"));
			bottomPipeImg = ImageIO.read(new File("res/Pillar2.png"));
			enemyImg = ImageIO.read(new File("res/Hornet2.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1); // Exit the application if images fail to load
		}

		bee = new Bee(beeImg);

		// Place pipes periodically
		placePipeTimer = new Timer(1500, e -> placePipes());
		placePipeTimer.start();

		// Spawn enemies periodically
		spawnEnemyTimer = new Timer(2000, e -> {
			int enemyY = random.nextInt(boardHeight - 50);
			enemies.add(new Enemy(enemyImg, boardWidth, enemyY, 40, 40, 3));
		});
		spawnEnemyTimer.start();

		// Game loop
		gameLoop = new Timer(1000 / 60, this);
		gameLoop.start();
	}

	// PLACE PIPES IN GAME PANEL
	void placePipes() {
		int randomPipeY = (int) (0 - Pipe.PIPE_HEIGHT / 4 - Math.random() * (Pipe.PIPE_HEIGHT / 2));
		int openingSpace = boardHeight / 4;

		Pipe topPipe = new Pipe(topPipeImg, boardWidth, randomPipeY);
		pipes.add(topPipe);

		Pipe bottomPipe = new Pipe(bottomPipeImg, boardWidth, topPipe.y + Pipe.PIPE_HEIGHT + openingSpace);
		pipes.add(bottomPipe);
	}

	// DRAW GRAPHICS IN GAME PANEL
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		// BACKGROUND
		g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

		// BEE
		g.drawImage(bee.img, bee.x, bee.y, bee.width, bee.height, null);

		// PIPE
		for (Pipe pipe : pipes) {
			g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
		}

		// ENEMIES
		for (Enemy enemy : enemies) {
			g.drawImage(enemy.img, enemy.x, enemy.y, enemy.width, enemy.height, null);
		}

		// PROJECTILES
		g.setColor(Color.red);
		for (Projectile p : projectiles) {
			g.fillRect(p.x, p.y, p.width, p.height);
		}

		
		// SCORE
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Pipe passed: " + (int) score, 10, 35);

		// ENEMY KILLED
		g.drawString("Hornets Killed: " + (int) enemyKilled, 300, 35);

		// FPS
		g.drawString("FPS: " + fps, 1000, 35);

		// GAME OVER MESSAGE
		if (gameOver) {

			
			g.setColor(Color.gray); 
			g.fillRect(405, 300, 350, 320); 
			
			// TEXT FONT AND TEXT SIZE FOR GAME OVER MESSAGE
			g.setFont(new Font("Arial", Font.BOLD, 48));
			g.setColor(Color.white);
			g.drawString("Game Over", 450, 400);

			// TEXT FON AND TEXT SIZE FOR GAME MESSAGE
			g.setFont(new Font("Arial", Font.BOLD, 20));
			int yOffset = 440;

			g.drawString("Final Score", 490, yOffset);
			yOffset += 30;
			g.drawString("> Pipe passed: " + (int) score, 490, yOffset);
			yOffset += 30;
			g.drawString("> Hornets Killed: " + (int) enemyKilled, 490, yOffset);
			yOffset += 40;

			g.drawString("Press P to play again", 490, yOffset);
			yOffset += 30;
			g.drawString("Or E to exit the Game", 490, yOffset);
		}
	}

	// MOVEMENT IN GAME PANEL
	public void move() {

		// BEE MOVEMENT
		velocityY += gravity;
		bee.y += velocityY;
		bee.y = Math.max(bee.y, 0);

		// GAME OVER IF THE BEE IS IN THE BUTTOM OF SCREEN
		if (bee.y > boardHeight) {
			gameOver = true;
		}

		// PIPE MOVEMENT AND COLLISION
		for (int i = pipes.size() - 1; i >= 0; i--) {
			Pipe pipe = pipes.get(i);
			pipe.x += velocityX;

			// SCORE 0.5 POINT FROM EACH PIPE TOTAL OF 1 POINT
			if (!pipe.passed && bee.x > pipe.x + pipe.width) {
				score += 0.5;
				pipe.passed = true;
			}

			// IF BEE TOUCH PIPE = GAME OVER
			if (bee.collision(pipe)) {
				gameOver = true;
			}

			// REMOVE PIPE IF COMPLETEY MOVE OFF THE SCREEN
			if (pipe.x + pipe.width < 0) {
				pipes.remove(i);
			}
		}

		// ENEMY MOVEMENT AND COLLISION
		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy enemy = enemies.get(i);
			enemy.move(bee);

			if (enemy.x + enemy.width < 0) {
				enemies.remove(i);
			}

			// IF BEE TOUCH ENEMY = GAME OVER
			if (bee.collision(enemy)) {
				gameOver = true;
			}

			// COUNT ENEMYS KILLED
			if (!enemy.alive) {
				enemies.remove(i);
				enemyKilled++;
			}
		}

		// PROJECTILE MOVEMENT
		for (int i = projectiles.size() - 1; i >= 0; i--) {
			Projectile projectile = projectiles.get(i);
			projectile.move();

			if (projectile.x > boardWidth) {
				projectiles.remove(i);
			}
		}

		// PROJECTILE-ENEMY COLLISIONS
		for (int i = projectiles.size() - 1; i >= 0; i--) {
			Projectile projectile = projectiles.get(i);
			for (int j = enemies.size() - 1; j >= 0; j--) {
				Enemy enemy = enemies.get(j);
				if (projectile.collision(enemy)) {
					enemy.alive = false;
					projectiles.remove(i);
					break;
				}
			}
		}

		// GAMELOOP, PIPETIMER, ENEMYTIMER WILL STOP IS GAME OVER
		if (gameOver) {
			gameLoop.stop();
			placePipeTimer.stop();
			spawnEnemyTimer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameOver) {
			move();
			repaint();
			trackFPS();
		} else {
			placePipeTimer.stop();
			gameLoop.stop();
			spawnEnemyTimer.stop();
		}
	}

	// TRACK FRAME RATE PER SECOND
	private void trackFPS() {
		frameCount++;
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastFpsTime >= 1000) {
			fps = frameCount;
			frameCount = 0;
			lastFpsTime = currentTime;
		}
	}

	public void keyPressed(KeyEvent e) {
		long currentTime = System.currentTimeMillis();

		// FLY BEE
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -9;
		
			// SHOOT PROJECTILE
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			if ((currentTime - lastShotTime) >= SHOOT_COOLDOWN) {
				PlaySoundEffect();
				projectiles.add(new Projectile(bee.x + bee.width, bee.y + bee.height / 2, 10, 5, 6));
				lastShotTime = currentTime;
			} else {
				stopBackgroundAudio();
			}
			// EXIT CONFIRMATION
		} else if (e.getKeyCode() == KeyEvent.VK_E) {
			Component frame = null;
			int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				GamePanel.dispose();
				new StartPage();
			}
			// AGAIN
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			if (gameOver) {
				resetGame();
			}
		}
	}
	
	private void resetGame() {
	    // Reset game variables
	    gameOver = false;
	    score = 0;
	    enemyKilled = 0;
	    velocityY = 0;

	    // Reset the bee position
	    bee.y = boardHeight / 2;

	    // Clear game objects
	    pipes.clear();
	    enemies.clear();
	    projectiles.clear();

	    // Restart timers and game loop
	    gameLoop.start();
	    placePipeTimer.start();
	    spawnEnemyTimer.start();
	}

	private static void dispose() {
		System.out.println("Game Ended");
	}
	
	// PLAY SHOOTING EFFECT
	public void PlaySoundEffect() {
		new Thread(() -> {
			try {
				File audioFile = new File("audio\\pew.wav");
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				Effect = AudioSystem.getClip();
				Effect.open(audioStream);
				Effect.loop(0);

				while (!Effect.isRunning()) {
					Thread.sleep(10);
				}

				while (Effect.isRunning()) {
					Thread.sleep(10);
				}

				Effect.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}

	public void stopBackgroundAudio() {
		if (Effect != null && Effect.isRunning()) {
			Effect.stop();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Bee Game");
		GamePanel game = new GamePanel();
		frame.add(game);
		frame.setSize(1200, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
