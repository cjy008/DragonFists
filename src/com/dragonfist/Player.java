package com.dragonfist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * 
 * @author Dylan
 *
 */
public class Player 
{
	public int x,y;
	
	//Drawing variables
	private Sprite body;
	private Bitmap playerStanding; //Player Sprite Image
	private Bitmap playerRightStrike; // angle of attack from 45 to -45 degrees NoE
	private Bitmap playerLeftStrike; // angle of attack from 135 to 45 degrees NoE
	private Bitmap playerFrontStrike; // angle of attack from 225 to 135 degrees NoE
	private Bitmap playerBackStrike; // angle of attack from 225 to 315 (-45) degrees NoE
	

	public static double strength; // 100 if it is full
	public static int killCount;

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
		playerStanding = GameView.scaleFactor((BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bruce)));
		playerRightStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_right));
		playerLeftStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_left));
		playerFrontStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_front));
		playerBackStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_back));
		killCount = 0;
		strength = 100;
	}
	/**
	 * Player is initialized by default 1/2 way along the screen horizontally and 3/4th down vertically
	 * @param gameView the View that the player is created in
	 */
	public Player (GameView gameView)
	{
		//TODO Change the image files for everything except playerStanding
		playerStanding = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bruce));
		playerRightStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_right));
		playerLeftStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_left));
		playerFrontStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_front));
		playerBackStrike = GameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.b_back));
		
		body = new Sprite(playerStanding);
		x = ((int)(gameView.screenWidth/2.0 - body.getWidth()/2.0));
		y = ((int)(gameView.screenHeight*3.0/4.0 - body.getHeight()/2.0));
		
		killCount = 0;
		strength = 100;
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
				x=(int)(enemy.x + enemy.getSprite().getWidth());
				y=((int)(enemy.y + enemy.getSprite().getWidth()*yDir/xDir));
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
				x=((int)(enemy.x + enemy.getSprite().getHeight()*xDir/yDir));
				y=(int)(enemy.y + enemy.getSprite().getHeight());				
			}
		}
		if (x < 0) { x = 0; }
		else if (x > GameView.screenWidth - body.getBmp().getWidth()) { x = GameView.screenWidth - body.getBmp().getWidth(); }
		if (y < 0) { y = 0; }
		else if (y + body.getHeight() > GameView.screenHeight) 
		{ 
			Log.d("Y + HEIGHT", String.format("screenHeight = %d; y-position = %d; height = %d; height2 = %d" , GameView.screenHeight, y, body.getHeight(), body.getHeight()));
			y = GameView.screenHeight - body.getBmp().getHeight();
			Log.d("Y + HEIGHT", String.format("screenHeight = %d; y-position = %d; height = %d; height2 = %d" , GameView.screenHeight, y, body.getHeight(), body.getHeight()));
		}
		
		enemy.hit(xDir, yDir);
		
		//To reduce the strength
		int reduceStrengthVariable = 1; // the larger the reduceStrengthVariable, the smaller the reduction in strength in each hit
		double strengthUsed = Math.sqrt(Math.pow(xDir,2)+Math.pow(yDir,2)) / GameView.screenWidth /reduceStrengthVariable *100;
		Log.d("strengthUsed", String.valueOf(strengthUsed));
		if (strengthUsed > strength){
			strength = 0;
		}else{
			strength -= strengthUsed;
		}
	}
	
}
