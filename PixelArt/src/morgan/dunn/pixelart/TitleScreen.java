package morgan.dunn.pixelart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import morgan.dunn.mycommonmethods.FileIO;

public class TitleScreen extends JPanel
{
	private static final String TITLE_ART = "Title_Graphic.png";
	private static final int HEIGHT = 512;
	private static final int WIDTH = 512;
	private static final int BUTTON_HEIGHT = 30;
	
	private PixelArt pixelArt;
	private Color color;
	private BufferedImage backgroundImage;
	
	public TitleScreen(PixelArt pa, Color c)
	{
		pixelArt = pa;
		setLayout(new BorderLayout());
		setBackground(c);
		color = c;
		
		backgroundImage = FileIO.readImageFile(this, TITLE_ART);
		
		JButton startButton = new JButton("Start Game");
		startButton.setPreferredSize(new Dimension(WIDTH, BUTTON_HEIGHT));
		startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pixelArt.startGame();
			}
		});
		
		add(startButton, BorderLayout.SOUTH);
	}
	
	public Dimension getPreferredSize()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT+BUTTON_HEIGHT);
		return size;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(color);
		g.fillRect(0, 0, WIDTH, HEIGHT+BUTTON_HEIGHT);
		
		g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
	}
}
