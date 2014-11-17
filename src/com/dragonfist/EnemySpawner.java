package com.dragonfist;
import java.util.*;
import java.lang.*;
import android.content.Context;
import android.util.Log;

public class EnemySpawner {
	private double velocityX;
	private double velocityY =0;
	private float positionX;
	private GameView gameView;
	private float positionY;
	int edge;
	
	//TODO Make bufferspace relative to screensize
	final static public int bufferspace = 40;
	static int screenWidth = GameView.screenWidth;
    static int screenHeight = GameView.screenHeight;
	final static int gravity = screenHeight/60;
	
	Random randomGenerator = new Random();
	
	public EnemySpawner(GameView gameView){
		this.gameView = gameView;
	}
	
	// We're creating a 20 pixel border around the screen of size 100
	public Enemy initializeEnemy(){
		

		initializePosition();
		
		
		setVelocity();
		
		//Testing purposes
		return new Enemy(gameView, positionX, positionY, velocityX, velocityY);
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
				Log.d("whoops",String.format("distance: %f",distance));
				Log.d("whoops2",String.format("time: %f",time));
				velocityX = distance / time;
			//}
	}
	
	
	


}