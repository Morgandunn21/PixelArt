package morgan.dunn.pixelart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;

import morgan.dunn.mycommonmethods.FileIO;

/**
 * Created: 10/26/18
 * Last Updated: 10/26/18
 * @author Morgan Dunn
 */

public class InfoPanel extends JPanel {
//Constants------------------------------------
	private static final String IMAGE_NAMES[] = {"Mouse_LeftClick.png","Mouse_RightClick.png","Tutorials_LetPass.png","Tutorials_Combine.png"};
	private static final int WIDTH = 600;
	private static final int HEIGHT = 75;
	private static final int IMAGE_SIDE_LENGTH = 64;
//Attributes-----------------------------------
	private BufferedImage images[];
	private PixelArt pixelArt;
//Constructor----------------------------------
	public InfoPanel(PixelArt pa)
	{
		pixelArt = pa;
		
		images = new BufferedImage[IMAGE_NAMES.length];
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		for(int i = 0; i < IMAGE_NAMES.length; i++)
		{
			images[i] = FileIO.readImageFile(this, IMAGE_NAMES[i]);
		}
		
		JButton resetGame = new JButton("Reset Game");
		
		resetGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pixelArt.newGame();
			}
		});
		
		add(resetGame);
	}
//Methods--------------------------------------
	public Dimension getPreferredSize()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT);
		return size;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		int imageY = (HEIGHT - IMAGE_SIDE_LENGTH)/2;
		g.drawImage(images[0], 50, imageY, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH, null);
		g.drawImage(images[2], 125, imageY, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH, null);
		g.drawImage(images[1], 250, imageY, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH, null);
		g.drawImage(images[3], 325, imageY, IMAGE_SIDE_LENGTH, IMAGE_SIDE_LENGTH, null);
	}
//Getters and Setters--------------------------
}
