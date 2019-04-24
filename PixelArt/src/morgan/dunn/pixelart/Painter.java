//Class for the player sprite. Will be a painter with running and jumping animation
//the player holds a frame that contains the main painting

package morgan.dunn.pixelart;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import morgan.dunn.mycommonmethods.FileIO;

public class Painter {
//Constants------------------------------------------
	private static final String[] IMAGES = {"Painter_Idle.png", "Painter_Run_0.png", "Painter_Run_1.png", "Painter_Jump_0.png", "Painter_Jump_1.png", ""};
	private static final int PAINTER_IDLE = 0;
	private static final int PAINTER_RUN = 1;
	private static final int PAINTER_JUMP_0 = 3;
	private static final int PAINTER_JUMP_1 = 4;
	private static final int ANIMATION_RATE = 3;
	private static final float JUMP_FORCE = -32f;
	private static final float GRAVITY = 1.5f;
	private static final float MOVE_SPEED = 6f;
//Attributes-----------------------------------------
	private int x, y;
	private float changeY, changeX = 0;
	private int screenHeight, screenWidth;
	private int spriteHeight, spriteWidth;
	private int frameHeight, frameWidth, frameWeight;
	private boolean jumpReady = false;
	private Picture picture;
	private BufferedImage[] painters = new BufferedImage[IMAGES.length];
	private int animationFrame = PAINTER_IDLE;
	private int animationTime = 0;
//Constructor----------------------------------------
	public Painter(int height, int width)
	{
		//initialize sprites
		for(int i = 0; i < IMAGES.length-1; i++)
		{
			if(IMAGES[i] == "")
			{
				painters[i] = null;
			}
			else
			{
				painters[i] = FileIO.readImageFile(this, IMAGES[i]);
			}
		}
		//get sprite height and width
		spriteHeight = painters[0].getHeight()*2;
		spriteWidth = painters[0].getWidth()*2;
		//get screen height and width
		screenHeight = height;
		screenWidth = width;
		//set starting position
		x = 0;
		y = screenHeight + spriteHeight - 1;
		//set frame parameters
		frameHeight = 5;
		frameWidth = 5;
		frameWeight = 8;
		//create Picture
		picture = new Picture((x + spriteWidth/2), (y + spriteHeight), height, width, frameHeight, frameWidth, frameWeight);
	}
//Methods--------------------------------------------
	//jump up
	public void jump()
	{
		changeY = JUMP_FORCE;
	}
	
	//run left or right
	public void run(boolean left)
	{
		if(left) { changeX = -MOVE_SPEED; } //run left
		else	{ changeX = MOVE_SPEED;	}   //run right
	}
	
	public boolean checkCollision(Pixel p)
	{
		return picture.checkCollision(p);
	}
	
	//stop running
	public void stop()
	{
		changeX = 0;
	}
		
	public void move()
	{
		//animate the picture;
		animate();
		//get the needed values for movement
		int changeYInt = (int)(changeY);	//integer change in x
		int changeXInt = (int)(changeX);	//integer change in y
		int distanceFromLeft = x + changeXInt;	//distance of picture from left edge
		int distanceFromRight = x + spriteWidth + frameWidth + changeXInt;	//distance of picture from right edge
		int distanceFromTop = y + spriteHeight + changeYInt;	//distance of picture from top of the screen
	//Bound movement horizontally
		//if picture is too far left
		if(distanceFromLeft <= 0)
		{
			//stop moving
			changeX =  -distanceFromLeft + changeXInt;
		}
		//picture is too far right
		else if (distanceFromRight >= screenWidth)
		{
			//fin out how far off the screen it is
			int dif = distanceFromRight - screenWidth - changeXInt;
			//stop moving
			changeX = -dif;
		}
		//bound movement vertically
		if(distanceFromTop >= screenHeight)
		{
			//find how far below the screen the picture is
			int dif = screenHeight - y - spriteHeight;
			//stop moving vertically
			y = screenHeight - spriteHeight;
			picture.move(0, dif);
			changeY = 0;
		}
		else
		{
			//increase downward motion of picture
			changeY += GRAVITY;
		}
		//move painter
		x += changeX;
		y += changeY;
		//move the picture the same as the painter
		picture.move(changeX, changeY);
	}
	//change the current sprite being drawn
	public void animate()
	{
		if(!grounded())
		{
			animationFrame = PAINTER_JUMP_1;	//Painter is jumping
			jumpReady = false;
		}
		else if(jumpReady)
		{
			animationFrame = PAINTER_JUMP_0;	//Painter is about to jump
		}
		else if(changeX == 0)
		{
			animationFrame = PAINTER_IDLE;		//painter isn't moving
		}
		else
		{
			animationTime += 1;					//Painter is running
			animationTime %= ANIMATION_RATE * 2;
			animationFrame = (animationTime / ANIMATION_RATE) + PAINTER_RUN;
		}
	}
	
	public void draw(Graphics g)
	{
		//Draw picture
		picture.draw(g);
		//Draw painter
		g.drawImage(painters[animationFrame], x, y, spriteWidth, spriteHeight, null);
	}
//Getters and Setters--------------------------------
	public void setPassThrough(boolean pt) { picture.setPassThrough(pt); }
	public boolean getPassThrough() { return picture.getPassThrough(); }
	public void setCombine(boolean c) { picture.setCombine(c); }
	public boolean getCombine() { return picture.getCombine(); }
	public boolean grounded() { return y + spriteHeight >= screenHeight; }
	public boolean full() { return picture.full(); }
	public void setJumpReady(boolean jR) { jumpReady = jR; }
	public Picture getPicture() { return picture; }
}
