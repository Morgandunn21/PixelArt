package morgan.dunn.pixelart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import morgan.dunn.mycommonmethods.FileIO;

/**
 * Created: 10/26/18
 * Last Updated: 10/26/18
 * @author Morgan Dunn
 */

public class Picture {
//Constants---------------------------------------------------
	private static final float JUMP_FORCE = -32f;
	private static final float GRAVITY = 1.5f;
	private static final float MOVE_SPEED = 6f;
//Attributes--------------------------------------------------
	private float changeY, changeX;
	private int x, y;
	private int screenHeight;
	private int screenWidth;
	private boolean combine;
	private boolean passThrough;
	private int pictureHeight;
	private int pictureWidth;
	private Pixel[][] pixels;
	private int numPixels, maxPixels;
	private int frameHeight, frameWidth, frameWeight, frameX, frameY;
//Constructor------------------------------------------------
	public Picture(int x, int y, int sH, int sW, int pH, int pW, int fW)
	{
		//set screen parameters
		screenWidth = sW;
		screenHeight = sH;
		//set picture parameters
		pictureHeight = pH;
		pictureWidth = pW;
		//add create pixel array
		pixels = new Pixel[pictureWidth][pictureHeight];
		//create first pixel
		Pixel p = new Pixel(x, 0, screenHeight);
		//set frame parameters
		frameWeight = fW;
		frameWidth = pW * p.getSideLength() + 2 * frameWeight - 1;
		frameHeight = pH * p.getSideLength() + 2 * frameWeight - 1;
		//set x and y
		this.x = x + frameWeight;
		this.y = y - p.getSideLength() - frameWeight;
		//add first pixel
		p.setY(this.y);
		pixels[0][0] = p;
		//set boolean values
		combine = false;
		passThrough = false;
		//set number of pixels and max pixels
		numPixels++;
		maxPixels = pH * pW;
	}
//Methods-----------------------------------------------------
	//draw the picture
	public void draw(Graphics g)
	{
		frameX = x - frameWeight;
		frameY = y - pixels[0][0].getSideLength() * (pictureHeight-1) - frameWeight;
		//draw Frame
		for(int i = 0; i < frameWeight; i++)
		{
			g.setColor(Color.ORANGE);
			g.drawRect(frameX + i, frameY + i, frameWidth - 2*i, frameHeight - 2*i);
		}
		//for every Pixel in the picture
		for(int i = 0; i < pictureWidth; i ++) 
		{
			for(int j = 0; j < pictureHeight; j++)
			{
				//draw the Pixel
				if(pixels[i][j] != null){	pixels[i][j].draw(g, x+pixels[i][j].getSideLength()*i, y-pixels[i][j].getSideLength()*j);	}
			}
		}
	}
	//draw at a given coordinate
	public void draw(Graphics g, int x, int y)
	{
		frameX = x - frameWeight;
		frameY = y - pixels[0][0].getSideLength() * (pictureHeight-1) - frameWeight;
		//draw Frame
		for(int i = 0; i < frameWeight; i++)
		{
			g.setColor(Color.ORANGE);
			g.drawRect(frameX + i, frameY + i, frameWidth - 2*i, frameHeight - 2*i);
		}
		//for every Pixel in the picture
		for(int i = 0; i < pictureWidth; i ++) 
		{
			for(int j = 0; j < pictureHeight; j++)
			{
				//draw the Pixel
				if(pixels[i][j] != null){	pixels[i][j].draw(g, x+pixels[i][j].getSideLength()*i, y-pixels[i][j].getSideLength()*j);	}
			}
		}
	}
	//move the picture around the screen
	public void move(float cX, float cY)
	{
		changeX = cX;
		changeY = cY;
		
		x += cX;
		y += cY;
		
		frameX += cX;
		frameY += cY;
		
		//for every Pixel in the picture
		for(Pixel[] row: pixels) 
		{
			for(Pixel p: row)
			{
				if(p != null)
				{
					//set its x and y movement
					p.setChangeY(changeY);
					p.setChangeX(changeX);
					//move it that distance
					p.move();
				}
			}
		}
	}
	//check for collisions with the picture from a Pixel
	public boolean checkCollision(Pixel pixel)
	{
		boolean collision = false;
		Rectangle bounds = pixel.getBounds();
		for(int i = 0; i < pictureWidth  && !collision; i++) 
		{
			for(int j = 0; j < pictureHeight  && !collision; j++)
			{
				Pixel p = pixels[i][j];
					
				if(p != null && bounds.intersects(p.getBounds()))
				{
					//if combining pixels
					if(combine)
					{
						collision = true;
						p.combine(pixel);
						FileIO.playClip(this, "Combine.wav");
					}
					//else if adding Pixel
					else
					{
						int direction = findDirection(p, pixel);
						switch(direction)
						{
							//Pixel is above
						case 0:
							if(j < pictureHeight - 1 && addUp(i, j))
							{
								j++;
								collision = true;
							}
							break;
							//Pixel is right
						case 1:
							if(i < pictureWidth - 1 && addRight(i, j))
							{
								i++;
								collision = true;
							}
							break;
						case 2:
							//Pixel is left
							//if its not the first Pixel
							if(i != 0 && addLeft(i,j))
							{
								i--;
								collision = true;
							}
							break;
						case 3:
							//Pixel is below
							if(j != 0 && addDown(i,j))
							{
								j--;
								collision = true;
							}
							break;
						}
					}
				}
				//now add the Pixel if adding
				if(!combine && collision)
				{
					FileIO.playClip(this, "AddPixel.wav");
					pixel.setX(x + pixel.getSideLength()*i);
					pixel.setY(y - pixel.getSideLength()*j);
					pixels[i][j] = pixel;
					numPixels++;
				}
			}
		}
		return collision;
	}
	//finds direction of collision
	private int findDirection(Pixel b, Pixel Pixel)
	{
		int leftNess = b.getX() - Pixel.getX();
		int rightNess = Pixel.getX() - b.getX();
		int upNess = b.getY() - Pixel.getY();
		int downNess = Pixel.getY() - b.getY();
		
		int mostOff = Math.max(Math.max(leftNess, rightNess), Math.max(upNess, downNess));
		
		int direction = 0;
		if(mostOff == upNess)
		{	direction = 0;	}
		else if(mostOff == rightNess)
		{	direction = 1;	}
		else if(mostOff == leftNess)
		{	direction = 2;	}
		else
		{	direction = 3;	}
		return direction;
	}
	//add a Pixel to the left
	private boolean addLeft(int i, int j)
	{
		boolean added = false;
		if(pixels[i-1][j] == null)
		{
			added = true;
		}
		return added;
	}
	//add a Pixel to the right
	private boolean addRight(int i, int j)
	{
		boolean added = false;
		//Pixel is right
		if(pixels[i+1][j] == null)
		{
			added = true;
		}
		return added;
	}
	//add a Pixel to the top
	private boolean addUp(int i, int j)
	{
		boolean added = false;
		//if the array isnt at max size AND the array has a size > j+1 AND the array at j+1 is null
		if(pixels[i][j+1] == null)
		{
			added = true;
		}
		return added;
	}
	//add a Pixel to the bottom
	private boolean addDown(int i, int j)
	{
		boolean added = false;
		
		if(pixels[i][j-1] == null)
		{
			added = true;
		}
		
		return added;
	}
//Getters and Setters--------------------------
	public Pixel[][] getpixels()
	{
		return pixels;
	}
	
	public void setPassThrough(boolean pt) { passThrough = pt; }
	public boolean getPassThrough() { return passThrough; }
	public void setCombine(boolean c) { combine = c; }
	public boolean getCombine() { return combine; }
	public boolean full() { return numPixels == maxPixels; }
	public int getSideLength() { return getPixelLength() * pictureWidth; }
	public int getPixelLength() { return pixels[0][0].getSideLength(); }
	public int getFrameWeight() { return frameWeight; }
}
