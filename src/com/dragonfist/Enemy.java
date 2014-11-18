package com.dragonfist;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Enemy {

	public float x,y, radius;
	public double velx,vely,accx,accy,health;
	public boolean alive,initialized; //These two do different things.
											//alive - If enemy has been killed - they can still hit other enemies!
											//uninitialized - If enemy is uninitialized, they are not drawn and don't effect gameplay.
	public boolean flipped;			  //If enemy spawns on left, it is not flipped, if on right it is.
	public static Sprite sprite;
	private static int enemyBmp[];
	static
	{
		enemyBmp = new int[5];
		
		// TODO add drawable resources to have different looking enemies
		//enemyBmp[0] = R.drawable.enemy0;
		//enemyBmp[1] = R.drawable.enemy1;
		//enemyBmp[2] = R.drawable.enemy2;
		//enemyBmp[3] = R.drawable.enemy3;
		//enemyBmp[4] = R.drawable.enemy4;
	}
	
	/**
	 * 
	 * @param gameView that creates the object
	 * @param x the position that the Enemy object is initialized at
	 * @param y the position that the Enemy object is initialized at
	 * @param velx the initial x direction velocity of the Enemy object
	 * @param vely the initial y direction velocity of the Enemy object
	 */
	public Enemy (GameView gameView, float x,float y,double velx,double vely,boolean flipped)
	{
		// TODO TWO LINES of code below once the enemy images have been integrated
		//Random randNumGen = new Random();
		//body = new Sprite(BitmapFactory.decodeResource(gameView.getResources(), enemyBmp[randNumGen.nextInt(enemyBmp.length)]));
		
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
		Log.d("Initialization", String.format("x: %f y: %f velx: %f vely: %f",x,y,velx,vely));
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
		double mass = 1.2; //This just changes how powerful the hits are. Can give this attribute to enemies
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
		}
	}
}
