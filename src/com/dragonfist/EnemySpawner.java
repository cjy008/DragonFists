package com.dragonfist;
import java.util.*;
import java.lang.*;
import android.content.Context;

public class EnemySpawner {
	private float acceleration;
	private double velocityX;
	private double velocityY =0;
	private float positionX;
	private GameView gameView;
	private float positionY;
	int edge;
	
	final int bufferspace = 20;
	int screenWidth = GameView.screenWidth;
    int screenHeight = GameView.screenHeight;
	final static int gravity = 2;
	
	Random randomGenerator = new Random();
	
	public EnemySpawner(GameView gameView){
		this.gameView = gameView;
	}
	
	// We're creating a 20 pixel border around the screen of size 100
	public Enemy initializeEnemy(){
		
		acceleration = 0;
		velocityX = 0;
		velocityY = 0;
		positionX = 0;
		positionY = 0;

		initializePosition();
		
		
		setVelocity();
		
		//Testing purposes
		return new Enemy(gameView, positionX, positionY, velocityX, velocityY);
		//System.out.println("X = " + positionX + " Y = " + positionY);
	}
	
	private void initializePosition(){
		edge = randomGenerator.nextInt(3);
		
		// On the left edge of the screen
		if(edge == 0){
			positionX = -bufferspace;
			// 20 + screenHeight + 20 
			int edgeLeft = screenHeight + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeLeft) - bufferspace;
			
		}
		// On the top edge of the screen
		if(edge == 1){
			positionY = -bufferspace;
			int edgeTop = screenWidth + (bufferspace * 2) + 1;
			positionX = randomGenerator.nextInt(edgeTop) - bufferspace;
		}
		// On the right edge of the screen
		if(edge == 2){
			positionX = screenHeight + bufferspace;
			int edgeRight = screenHeight + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeRight) - bufferspace;
		}
	}

	
	private void setVelocity(){
		if (positionY > (screenHeight + bufferspace) /2){
			double velocityY =10;
			float distanceY = screenHeight + bufferspace- positionY;
			double time = (-velocityY + Math.sqrt( velocityY *velocityY + 2 * gravity *distanceY)) / gravity;
			float distanceX = (screenWidth/2) - positionX;
			velocityX = distanceX / time;
			}else{
				double time = Math.sqrt((screenHeight + bufferspace) *2 / gravity);
				float distance = (screenWidth/2)-positionX;
				velocityX = distance / time;
			}
	}
	
	
	


}