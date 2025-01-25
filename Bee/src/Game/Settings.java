package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Settings implements ActionListener {

	// frame and button size.
	final int boardWidth = 1200;
	final int boardHeight = 800;
	final int buttonWidth = 150;
	final int buttonHeight = 50;

	// Variables for Swing application.
	JFrame about;
	JButton back;
	Image bg;

	public Settings() {
		
		try {
			bg = ImageIO.read(new File("res/Settings.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Back button
		back = new JButton("Back");
		back.setBounds(20, 20, buttonWidth, buttonHeight);
		settingButton(back);
		back.addActionListener(this);

		// Create and configure panel
		SettingsPanel panel = new SettingsPanel();
		panel.setLayout(null);
		panel.add(back);

		// Create settings frame
		about = new JFrame("ABOUT THE GAME");
		about.setSize(new Dimension(boardWidth, boardHeight));
		about.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		about.setLocationRelativeTo(null);
		about.setResizable(false);
		about.add(panel);
		about.setVisible(true);
	}

	// Custom panel for background
	class SettingsPanel extends JPanel {

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Draw background
			if (bg != null) {
				g.drawImage(bg, 0, 0, boardWidth, boardHeight, this);
			} else {
				g.setColor(Color.RED);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.drawString("Background image not found", 50, 50);
			}
		}
	}

	private void settingButton(JButton button) {
		button.setFocusable(false);
		button.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		button.setForeground(Color.white);
		button.setBackground(Color.gray);
		button.setBorder(BorderFactory.createEtchedBorder());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			System.out.println("Back button clicked");
			about.dispose(); // Close the settings frame
			new StartPage(); // Return to the start page
		}
	}

	// Main method for testing
	public static void main(String[] args) {
		// FOR TESTING
		new Settings();
	}
}
