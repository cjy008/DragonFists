package com.dragonfist;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Enemy {

	public float x,y, radius;
	public double velx,vely,accx,accy;
	private Sprite body;
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
	public Enemy (GameView gameView, float x,float y,double velx,double vely)
	{
		//Drawing Variables
		body = new Sprite(gameView.scaleFactor(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bruce)));
		// TODO Replace the line of code above with the TWO LINES of code below once the enemy images have been integrated
		//Random randNumGen = new Random();
		//body = new Sprite(BitmapFactory.decodeResource(gameView.getResources(), enemyBmp[randNumGen.nextInt(enemyBmp.length)]));
		
		//Physics Variables
		this.x = x;
		this.y = y;
		this.body.setX((int)x);
		this.body.setY((int)y);
		this.velx = velx;
		this.vely = vely;
		Log.d("Initialization", String.format("x: %f y: %f velx: %f vely: %f",x,y,velx,vely));
	}

	public void update(float passedTime,int index)
	{
		x= (float) (x+(velx*passedTime));
		velx = velx+(accx*passedTime);
		accy = EnemySpawner.gravity;
		vely = vely+(accy*passedTime);
		y= (float) (y+(vely*passedTime));
		if(index==1)
		{
			Log.d("Updating", x + " " + y);
		}
		body.setX((int)x);
		body.setY((int)y);
	}
	
	public void Draw(Canvas canvas)
	{ body.Draw(canvas); }
	
	/**
	 * @return the x position of the sprite's center
	 */
	public float getCenX()
	{
		return x+(body.getWidth()/2);
	}
	/**
	 * 
	 * @return the y position of the sprite's center
	 */
	public float getCenY()
	{

		return y+(body.getHeight()/2);
	}
	
	/**
	 * @return Sprite object needed for call to draw method
	 */
	public Sprite getBody()
	{ return body; }
}
