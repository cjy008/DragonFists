package com.dragonfist;
import java.util.*;
import java.lang.*;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

public class EnemySpawner {
	private double velocityX;
	private double velocityY;
	private float positionX;
	private float positionY;
	private GameView gameView;
	int edge;
	
	
	final static public int bufferspace = 40;	//TODO Make bufferspace relative to screensize
	static int screenWidth = GameView.screenWidth;
    static int screenHeight = GameView.screenHeight;
	final static int gravity = screenHeight/10;
	
	public float timeCounter;		
	public float spawnTime;		//Enemies spawn every (spawnTime) in-game seconds. 
	Random randomGenerator = new Random();
	
	public EnemySpawner(GameView gameView){
		this.gameView = gameView;
		timeCounter = 0;
		spawnTime = .75f;
		//Enemy.sprite = new Sprite(BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy));
	}
	
	public boolean increment(float timePassed)
	{
		timeCounter+=timePassed;
		if(timeCounter>spawnTime)
		{
			timeCounter = timeCounter-spawnTime;
			return true;
		}
		else return false;
	}
	
	// We're creating a 20 pixel border around the screen of size 100
	public Enemy initializeEnemy(){
		
		
		initializePosition();
		
		
		setVelocity();
		
		if(positionX>screenWidth/2)			//Determines whether we need the enemy sprite to be flipped or not.
		{
			return new Enemy(positionX, positionY, velocityX, velocityY,true);
		}
		else
		{
			return new Enemy(positionX, positionY, velocityX, velocityY,false);
		}
		//Testing purposes
		//System.out.println("X = " + positionX + " Y = " + positionY);
		
	}
	
	private void initializePosition(){
		edge = randomGenerator.nextInt(4);
		
		// On the left edge of the screen
		if(edge == 0){
			positionX = -bufferspace;
			// 20 + screenHeight + 20 
			int edgeLeft = screenHeight/2 + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeLeft) - bufferspace;
			
		}
		// On the top edge of the screen
		if(edge == 1){
			positionY = -bufferspace;
			int edgeTopLeft = screenWidth/2 + (bufferspace) + 1;
			positionX = randomGenerator.nextInt(edgeTopLeft) - bufferspace;
		}
		if(edge == 2){
			positionY = -bufferspace;
			int edgeTopRight = screenWidth/2 + (bufferspace) + 1;
			positionX = randomGenerator.nextInt(edgeTopRight) - bufferspace + screenWidth/2;
		}
		// On the right edge of the screen
		if(edge == 3){
			positionX = screenWidth + bufferspace;
			int edgeRight = screenHeight/2 + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeRight) - bufferspace;
		}
	}

	
	private void setVelocity(){
		/*if (positionY > (screenHeight + bufferspace) /2){
			double velocityY =-10;
			float distanceY = screenHeight + bufferspace- positionY;
			double time = (-velocityY + Math.sqrt( velocityY *velocityY + 2 * gravity *distanceY)) / gravity;
			float distanceX = (screenWidth/2) - positionX;
			velocityX = distanceX / time;
			}else{*/
				double time = Math.sqrt((screenHeight-positionY) *2 / gravity);
				float distance = (screenWidth/2)-positionX;
				velocityX = distance / time;
			//}
	}
	
	
	


}