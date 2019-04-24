package morgan.dunn.pixelart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import morgan.dunn.mycommonmethods.FileIO;

public class EndScreen extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private static final String BG_IMAGE = "EndScreen_BG.png";
	private static final String FG_IMAGE = "EndScreen_FG.png";
	private static final int WIDTH = 512;
	private static final int HEIGHT = 512;
	private static final int CREDIT_HEIGHT = 60;
	
	private Picture finalPicture;
	private PixelArt pixelArt;
	private BufferedImage background, foreground;
	
	public EndScreen(PixelArt pa)
	{
		pixelArt = pa;
		
		setLayout(new FlowLayout());
		
		JButton savePicture = new JButton("Save Picture?");
		JButton playAgain = new JButton("Play Again");
		JButton endGame = new JButton("Quit");
		
		savePicture.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String fileName = "picture.png";
				savePicture(fileName);
				remove(savePicture);
				repaint();
				revalidate();
			}
		});
		
		playAgain.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pixelArt.newGame();
			}
		});
		
		endGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		add(savePicture);
		add(playAgain);
		add(endGame);
		
		background = FileIO.readImageFile(this, BG_IMAGE);
		foreground = FileIO.readImageFile(this, FG_IMAGE);
	}
	
	public void setPicture(Picture p)
	{
		finalPicture = p;
	}
	
	public Dimension getPreferredSize()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT + CREDIT_HEIGHT);
		return size;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(97,75,44));
		g.fillRect(0, HEIGHT, WIDTH, CREDIT_HEIGHT);
		g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
		int pictureX = WIDTH/2 - finalPicture.getSideLength()/2;
		int pictureY = HEIGHT/2 + finalPicture.getSideLength()*3/8  + finalPicture.getFrameWeight() - finalPicture.getPixelLength();
		finalPicture.draw(g, pictureX, pictureY);
		g.drawImage(foreground, 0, 0, WIDTH, HEIGHT, null);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		g.setColor(Color.black);
		g.drawString("All third party sound effects credited", 5, HEIGHT + CREDIT_HEIGHT/3);
		g.drawString("in READ_ME file", 5, HEIGHT + CREDIT_HEIGHT*2/3);
	}
	
	public void savePicture(String fileName)
	{
		//create image
		BufferedImage img = new BufferedImage(finalPicture.getSideLength() + finalPicture.getFrameWeight()*2, finalPicture.getSideLength() + finalPicture.getFrameWeight()*2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		finalPicture.draw(g, finalPicture.getFrameWeight(), finalPicture.getSideLength() - finalPicture.getPixelLength() + finalPicture.getFrameWeight());
		//Save Image//////////////////////////////
		//Set Image Location
		File file = new File(fileName);
		//Try to save image
		try
		{
			ImageIO.write(img, "png", file);
		}
		//If file can't save, tell user
		catch(IOException e)
		{
			String message = "Could not save " + fileName;
			JOptionPane.showMessageDialog(null, message);
		}
	}
	
}
