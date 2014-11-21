package com.dragonfist;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Enemy {

	public float x,y, radius;
	public double velx,vely,health;
	public boolean alive,initialized; //These two do different things.
											//alive - If enemy has been killed - they can still hit other enemies!
											//uninitialized - If enemy is uninitialized, they are not drawn and don't effect gameplay.
	public boolean flipped;			  //If enemy spawns on left, it is not flipped, if on right it is.
	private int spriteIndex; 
	public static Sprite sprite;
	private static Sprite enemySprites[];
	
	public static final int DEAD_FRONT =  5;
	public static final int DEAD_BACK = 6;
	public static int DEAD_LEFT = 7;
	public static int DEAD_RIGHT = 8;
	
	static
	{
		enemySprites = new Sprite[9];
		// TODO add drawable resources to have different looking enemies
		//enemySprites[0] = R.drawable.enemy0;
		//enemySprites[1] = R.drawable.enemy1;
		//enemySprites[2] = R.drawable.enemy2;
		//enemySprites[3] = R.drawable.enemy3;
		//enemySprites[4] = R.drawable.enemy4;
		//enemySprites[5] = R.drawable.deadfront;
		//enemySprites[6] = R.drawable.deadback;
		//enemySprites[7] = R.drawable.deadleft;
		//enemySprites[8] = R.drawable.deadright;
	}
	
	
	/**
	 * Use this method for initial construction of enemies - NOT after the game is resumed.
	 * @param x the position that the Enemy object is initialized at
	 * @param y the position that the Enemy object is initialized at
	 * @param velx the initial x direction velocity of the Enemy object
	 * @param vely the initial y direction velocity of the Enemy object
	 */
	public Enemy (float x,float y,double velx,double vely,boolean flipped)
	{
		// TODO TWO LINES of code below once the enemy images have been integrated
		
		Random randNumGen = new Random();
		spriteIndex = randNumGen.nextInt(4);
		
		//Physics Variables
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		this.flipped = flipped;
		alive = true;
		health = Math.sqrt(Math.pow(velx,2)+Math.pow(vely,2));
		initialized = true;
		Log.d("Initialization Health",String.format("Initial health: %f", health));
		Log.d("Initialization Positions", String.format("x: %f y: %f velx: %f vely: %f",x,y,velx,vely));
	}
	
	/**
	 * Use this method for after the game is resumed.
	 * @param x the position that the Enemy object is initialized at
	 * @param y the position that the Enemy object is initialized at
	 * @param velx the initial x direction velocity of the Enemy object
	 * @param vely the initial y direction velocity of the Enemy object
	 */
	public Enemy (float x,float y,double velx,double vely,boolean flipped, float health, boolean alive, boolean initialized)
	{
		// TODO TWO LINES of code below once the enemy images have been integrated
		//Random randNumGen = new Random();
		//body = new Sprite(BitmapFactory.decodeResource(gameView.getResources(), enemyBmp[randNumGen.nextInt(enemyBmp.length)]));
		
		Random randNumGen = new Random();
		spriteIndex = randNumGen.nextInt(4);
		
		//Physics Variables
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
		this.flipped = flipped;
		this.alive = true;
		this.health = health;
		this.initialized = true;
		Log.d("After Resuming Health",String.format("Initial health: %f", health));
		Log.d("After Resuming Positions", String.format("x: %f y: %f velx: %f vely: %f",x,y,velx,vely));
	}

	public void update(float passedTime)
	{
		x += (float)(velx*passedTime);
		vely += (EnemySpawner.gravity*passedTime);
		y+= (float) (vely*passedTime);
		
	}
	
	public void Draw(Canvas canvas)
	{ 
		sprite.Draw(canvas,x,y,velx,vely,flipped);
	}
	
	/**
	 * @return the x position of the sprite's center
	 */
	public float getCenX()
	{
		return x+(sprite.getWidth()/2);
	}
	/**
	 * 
	 * @return the y position of the sprite's center
	 */
	public float getCenY()
	{

		return y+(sprite.getHeight()/2);
	}
	
	/**
	 * @return Sprite object needed for call to draw method
	 */
	/**
	 * 
	 * @param xAcc The amount of horizontal acceleration imparted by the hit
	 * @param yAcc The amount of vertical acceleration imparted by the hit
	 * 
	 * Determines whether the enemy "dies" due to the force from the hit.
	 * Ensures that the player can't just hit each enemy individually
	 * with weak hits to kill all the enemies - too easy.
	 * 
	 * 
	 */
	public void hit(double xFor, double yFor)
	{
		double mass = 1.0; //TODO 
						   //OPTIONAL: This just changes how powerful the hits are. Can give this attribute to enemies
					  	   //if we want enemies with different weights.
		velx += xFor/mass;
		vely += yFor/mass;
		double force = Math.sqrt(Math.pow(xFor,2)+Math.pow(yFor, 2));
		health -= (force);								//Force is dependent on screen size because of pixel-scaled vectors!
														//Therefore damage to health should be relative to screen size.
														//However we already make health relative to initial velocity, which solves this problme.
		Log.d("Hit Method",String.format("Enemy health is now %f",health));
		if(health<0)
		{
			alive=false;
			setSprite(xFor, yFor);
			//this.sprite.setBmp(bmp)
		}
	}
	
	/**
	 * Set the Sprite when the enemy has DIED
	 * @param xDir the horizontal component of the direction that the enemy was hit
	 * @param yDir the vertical component of the direction the the enemy was hit
	 */
	public void setSprite(double xDir, double yDir)
	{
		if(Math.abs(xDir) >= Math.abs(yDir))	//Attack angle is either to the right or left
		{
			if (xDir >= 0)
			{
				spriteIndex = DEAD_RIGHT; 
			}
			else
			{ 
				spriteIndex = DEAD_LEFT; 
			}
		}
		else									//Attack angle is either up or down
		{
			if (yDir >= 0)
			{
				spriteIndex = DEAD_FRONT;
			}
			else
			{ 
				spriteIndex = DEAD_BACK;			
			}
		}
	}
}
