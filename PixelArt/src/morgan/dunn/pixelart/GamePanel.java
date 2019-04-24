package morgan.dunn.pixelart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

import morgan.dunn.mycommonmethods.FileIO;

/**
 * Created: 10/26/18
 * Last Updated: 10/26/18
 * @author Morgan Dunn
 */

public class GamePanel extends JPanel {
//Constants------------------------------------
//Serial ID for recreation
	private static final long serialVersionUID = 1L;
	static final int WIDTH = 600;
	static final int HEIGHT = 525;
	private static final int SEPERATION = 40;
	private static final String MUSIC_FILE = "PixelArt.wav";
	private Clip backgroundMusic;
//Attributes-----------------------------------
	private PixelArt pixelArt;
	private Timer timer;
	private Painter painter;
	private ArrayList<Pixel> spawnedPixels;
	private int count;
	private boolean full = false;
//Constructor----------------------------------
	public GamePanel(PixelArt pa)
	{
		pixelArt = pa;
		painter = new Painter(HEIGHT, WIDTH);
		spawnedPixels = new ArrayList<Pixel>();
		count = 0;
		
		backgroundMusic = FileIO.playClip(this, MUSIC_FILE);
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		//timer called every 40 ms
		timer = new Timer(40, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				timedAction();
			}
		});
	}
//Methods--------------------------------------
	public Dimension getPreferredSize()
	{
		Dimension size = new Dimension(WIDTH, HEIGHT);
		return size;
	}
	
	public void paintComponent(Graphics g)
	{
		//background
		g.setColor(new Color(127, 197, 240));
		g.fillRect(0,  0,  WIDTH, HEIGHT);
		//draw picture
		painter.draw(g);
		//draw spawned Pixels
		for(Pixel b: spawnedPixels)
		{
			b.draw(g);
		}
	}
	
	public void timedAction()
	{
		painter.move();
		
		//move walls
		for(int i = 0; i < spawnedPixels.size(); i++)
		{
			Pixel b = spawnedPixels.get(i);
			boolean collided = false;
			//if the picture isn't passing through
			if(!painter.getPassThrough())
			{
				//check Collisions
				collided = painter.checkCollision(b);	
			}
			//if wall is off screen, remove it
			if(b.isPastWindow() || collided)
			{	
				spawnedPixels.remove(b);
				i--;
			}
			else { b.move(); }
		}
		//check to spawn next Pixel
		count++;
		if(count >= SEPERATION)
		{
			spawnedPixels.add(new Pixel(WIDTH, HEIGHT));
			count = (int)(Math.random() * (SEPERATION / 2));
		}
		//check if painting is full
		if(painter.full() && !full)
		{
			pixelArt.fullPainting();
			full = !full;
		}
		repaint();
	}
	
	//Listen for certain keys
	public void setKeyPressed(char key)
	{
		if(key == ' ')
		{
			if(painter.grounded())
		{
				painter.setJumpReady(true);
			}
		}
		else if(key == 'a')
		{
			painter.run(true);
		}
		else if(key == 'd')
		{
			painter.run(false);
		}
	}
		
	public void setKeyReleased(char key)
	{
		if(key == 'a' || key == 'd')
		{
			painter.stop();
		}
		else if(painter.grounded() && key == ' ')
		{
			painter.jump();
		}
	}
	//Listen for mouse input
	//called when mouse is pressed
	public void setMousePressed(int mB)
	{
		if(mB == MouseEvent.BUTTON1)
		{
			painter.setPassThrough(true);
		}
		else
		{
			painter.setCombine(true);
		}
	}
	//called when mouse is released	
	public void setMouseReleased(int mB)
	{	
		if(mB == MouseEvent.BUTTON1)
		{
			if(painter.getPassThrough()==true)
			{
				painter.setPassThrough(false);
			}
		}
		else
		{
			if(painter.getCombine()==true)
			{
				painter.setCombine(false);
			}
		}
	}
			
	public void endMusic()
	{
		backgroundMusic.close();
	}
	
	public void startTimer() { timer.start(); }
	public void stopTimer() { timer.stop(); }
//Getters and Setters--------------------------
	public Picture getPicture()
	{
		return painter.getPicture();
	}
}
