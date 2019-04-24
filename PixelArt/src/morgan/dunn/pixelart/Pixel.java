package morgan.dunn.pixelart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 * Created: 10/26/18
 * Last Updated: 10/26/18
 * @author Morgan Dunn
 */

public class Pixel 
{
//Constants------------------------------------
	private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.WHITE, Color.BLACK, };
	private static final int SIDE_LENGTH = 40;
	private static final float DEFAULT_SPEED = -5f;
//Attributes-----------------------------------
	private int x;
	private int y;
	private float changeY;
	private float changeX;
	private Color color;
	private Random random;
//Constructor----------------------------------
	public Pixel(int x, int y, int sH)
	{
		random = new Random();
		this.x = x;
		this.y =  y;
		color = COLORS[random.nextInt(COLORS.length)];
		changeY = 0;
		changeX = 0;
	}
	
	public Pixel(int screenWidth, int screenHeight)
	{
		random = new Random();
		x = screenWidth + 2 * SIDE_LENGTH;
		y = random.nextInt(screenHeight - SIDE_LENGTH + 1);
		color = COLORS[random.nextInt(COLORS.length)];
		changeY = 0;
		changeX = DEFAULT_SPEED;
	}
//Methods--------------------------------------
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillRect(x, y, SIDE_LENGTH, SIDE_LENGTH);
	}
	
	public void draw(Graphics g, int x, int y)
	{
		g.setColor(color);
		g.fillRect(x, y, SIDE_LENGTH, SIDE_LENGTH);
	}
	
	public void move()
	{
		x += changeX;
		y += changeY;
	}
	
	public void combine(Pixel pixel)
	{
		Color newColor = pixel.getColor();
		
		int r, g, b;
		
		r = (int)Math.sqrt((Math.pow(color.getRed(), 2) + Math.pow(newColor.getRed(), 2))/2);
		g = (int)Math.sqrt((Math.pow(color.getGreen(), 2) + Math.pow(newColor.getGreen(), 2))/2);
		b = (int)Math.sqrt((Math.pow(color.getBlue(), 2) + Math.pow(newColor.getBlue(), 2))/2);
		
		color = new Color(r, g, b);
	}
	
	public boolean isPastWindow()
	{
		int rightEdgeX = x + SIDE_LENGTH;
		return (rightEdgeX < 0);
	}
	
//Getters and Setters--------------------------
	public void setChangeY(float cY) { changeY = cY; }
	public void setChangeX(float cX) { changeX = cX; }
	public void setY(int newY) { y = newY; }
	public void setX(int newX) { x = newX; }
	public Color getColor() { return color; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getSideLength() { return SIDE_LENGTH; }
	public Rectangle getBounds()
	{
		Rectangle bounds = new Rectangle(x, y, SIDE_LENGTH, SIDE_LENGTH);
		return bounds;
	}
}
