package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.IOException;
import java.io.File;
//import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StartPage implements ActionListener {

	// FRAME/BUTTON SIZE
	final int boardWidth = 1200;
	final int boardHeight = 800;
	final int buttonWidth = 150;
	final int buttonHeight = 50;

	// VARIABLES FOR SWING APLLICATION
	JFrame startFrame;
	JButton gameButton;
	JButton TheGame;
	JButton exitButton;
	Image bgimage;

	Clip music;

	public StartPage() {

		playBgMusic();

		// CALCULATE CENTER X-COORDINATE FOR BUTTONS
		int centerX = (boardWidth - buttonWidth) / 2;

		// START GAME BUTTON
		gameButton = new JButton("Start");
		gameButton.setBounds(centerX, 350, buttonWidth, buttonHeight);
		customizeButton(gameButton);
		gameButton.addActionListener(this);

		// SETTINGS BUTTON
		TheGame = new JButton("About");
		TheGame.setBounds(centerX, 420, buttonWidth, buttonHeight);
		customizeButton(TheGame);
		TheGame.addActionListener(this);

		// EXIT BUTTON
		exitButton = new JButton("Exit");
		exitButton.setBounds(centerX, 490, buttonWidth, buttonHeight);
		customizeButton(exitButton);
		exitButton.addActionListener(this);

		// MAIN FRAME
		startFrame = new JFrame("Flying Bee");
		startFrame.setSize(new Dimension(boardWidth, boardHeight));
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startFrame.setLocationRelativeTo(null);
		startFrame.setResizable(false);

		// CUSTOM PANEL FOR BACKGROUNDS
		BG bgPanel = new BG();
		bgPanel.setLayout(null);
		startFrame.setContentPane(bgPanel);

		// ADD BUTTONS TO THE PANEL
		bgPanel.add(gameButton);
		bgPanel.add(TheGame);
		bgPanel.add(exitButton);

		// MAKE THE FRAME VISIBLEB
		startFrame.setVisible(true);
	}

	// UNIFROM BUTTON SIZE, COLOR AND FONTS FOR ALL BUTTONS
	private void customizeButton(JButton button) {
		button.setFocusable(false);
		button.setFont(new Font("Comic Sans", Font.BOLD, 25));
		button.setForeground(Color.white);
		button.setBackground(Color.gray);
		button.setBorder(BorderFactory.createEtchedBorder());
	}

	// PLAY BACKGOUND MUSIC
	public void playBgMusic() {
		new Thread(() -> {
			try {
				File audioFile = new File("audio\\Music.wav");
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				music = AudioSystem.getClip();
				music.open(audioStream);
				music.loop(Clip.LOOP_CONTINUOUSLY);

				while (!music.isRunning()) {
					Thread.sleep(10);
				}

				while (music.isRunning()) {
					Thread.sleep(10);
				}

				music.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}

	// STOP BACKGROUND MUSIC
	public void stopBackgroundAudio() {
		if (music != null && music.isRunning()) {
			music.stop();
		}
	}

	// CUSTOM BACKGROUND PANEL
	public class BG extends JPanel {

		private ImageIcon bgImage;

		public BG() {
			// LOAD GIF IMAGE AS BACKGROUD
			bgImage = new ImageIcon("res/Swarm stingers.gif");
		}

		@Override
		protected void paintComponent(Graphics g) {
			// PAINT THE BACKGROUND
			super.paintComponent(g);
			if (bgImage != null) {
				g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
			}

		}

	}

	// KEY ACTION LISTENERS
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameButton) {

			// MESSGAE MECHANICS OF THE GAME
			JOptionPane.showMessageDialog(startFrame,
					"How to Play:\n" + "- Press Space bar to Fly\n" + "- Press S to Shoot", "Game Mechanics",
					JOptionPane.INFORMATION_MESSAGE);

			// GAME START MESSAGE
			JOptionPane.showMessageDialog(startFrame,
					"Note:\n" + "- It takes 1.5 seconds to\n" + " reload the bullet\n" + " Use it wisely. :)",
					"GET READY!", JOptionPane.INFORMATION_MESSAGE);

			// CREATE A NEW FRAME FOR THE GAME
			JFrame gameFrame = new JFrame("Game Panel");
			gameFrame.add(new GamePanel());
			gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gameFrame.setSize(1200, 800);
			gameFrame.setLocationRelativeTo(null);
			gameFrame.setVisible(true);

			// STOP BACKGROUN MUSIC
			music.stop();

			// CLOSE THE START FRAME
			startFrame.dispose();

		} else if (e.getSource() == TheGame) {

			// STOP BACKGROUND MUSIC
			music.stop();

			// TRANSITION TO THE SETTINGS MENU
			startFrame.dispose();
			new Settings();

		} else if (e.getSource() == exitButton) {

			// SHOW CONFIRMATION DIALAOG
			int confirm = JOptionPane.showConfirmDialog(startFrame, "Are you sure you want to exit?",
					"Exit Confirmation", JOptionPane.YES_NO_OPTION);

			// EXIT IF THE USER CONFRIMS
			if (confirm == JOptionPane.YES_OPTION) {
				System.out.println("Game ended");
				startFrame.dispose();
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		// FOR TESTING
		new StartPage();
	}

}
