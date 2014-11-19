package com.dragonfist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 
 * @author Dylan
 *
 */
public class Player 
{
	private int x,y;
	
	//Drawing variables
	private Sprite body;
	private Bitmap playerStanding; //Player Sprite Image
	private Bitmap playerRightStrike; // angle of attack from 45 to -45 degrees NoE
	private Bitmap playerLeftStrike; // angle of attack from 135 to 45 degrees NoE
	private Bitmap playerFrontStrike; // angle of attack from 225 to 135 degrees NoE
	private Bitmap playerBackStrike; // angle of attack from 225 to 315 (-45) degrees NoE
	
	/**
	 * Constructor that allows the caller to designate the starting location of the player
	 * @param gameView the View that the player is created in
	 * @param startX the initial horizontal position of player
	 * @param startY the initial verticle position of player
	 */
	public Player (GameView gameView, float startX,float startY)
	{
		x = (int) startX;
		y = (int) startY;
		playerStanding = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bruce);
		playerRightStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_right);
		playerLeftStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_left);
		playerFrontStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_front);
		playerBackStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_back);
	}
	/**
	 * Player is initialized by default 1/2 way along the screen horizontally and 3/4th down vertically
	 * @param gameView the View that the player is created in
	 */
	public Player (GameView gameView)
	{
		//TODO Change the image files for everything except playerStanding
		playerStanding = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bruce);
		playerRightStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_right);
		playerLeftStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_left);
		playerFrontStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_front);
		playerBackStrike = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_back);
		
		body = new Sprite(playerStanding);
		x = ((int)(gameView.screenWidth/2.0 - body.getWidth()/2.0));
		y = ((int)(gameView.screenHeight*3.0/4.0 - body.getHeight()/2.0));
	}
	
	public void update()
	{ }
	
	/**
	 * 
	 * @param canvas the 
	 */
	public void Draw(Canvas canvas)
	{ body.Draw(canvas,x,y); }
	
	/**
	 * Changes the bitmap of the Player to match the direction that he/she is attacking
	 * Also changes the position of the player to be in line with his attack
	 * @param enemy the enemy object that gets attacked
	 * @param xDir the horizontal component of the attack vector
	 * @param yDir the vertical component of the attack vector
	 */
	public void hit(Enemy enemy,float xDir, float yDir)
	{
		//TODO Reduce Player Strength by relative amount (Math.sqrt(Math.pow(xAcc,2)+Math.pow(yAcc,2) is "amount of force")
		//TODO Change Player sprite
		//TODO Change Player position - add from attack
		if(Math.abs(xDir) >= Math.abs(yDir))	//Attack angle is either to the right or left
		{
			if (xDir >= 0)
			{ 
				body.setBmp(playerRightStrike);
				x=(int)(enemy.x - body.getWidth());
				y=((int)(enemy.y- body.getWidth()*yDir/xDir));
			}
			else
			{ 
				body.setBmp(playerLeftStrike);
				x=(int)(enemy.x + body.getWidth());
				y=((int)(enemy.y + body.getWidth()*yDir/xDir));
			}
		}
		else									//Attack angle is either up or down
		{
			if (yDir >= 0)
			{ 
				body.setBmp(playerFrontStrike);
				x=((int)(enemy.x - body.getHeight()*xDir/yDir));
				y=(int)(enemy.y - body.getHeight());
			}
			else
			{ 
				body.setBmp(playerBackStrike);
				x=((int)(enemy.x + body.getHeight()*xDir/yDir));
				y=(int)(enemy.y + body.getHeight());				
			}
		}
		enemy.hit(xDir,yDir);
	}
}
