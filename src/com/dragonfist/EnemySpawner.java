package com.dragonfist;
import java.util.*;
import java.lang.*;

public class EnemySpawner {
	private float acceleration;
	private double velocityX;
	private double velocityY =0;
	private float positionX;
	private GameView gameView;
	private float positionY;
	int edge;
	
	final int bufferspace = 20;
	final int screensize = 100;
	final int gravity = 10;
	
	Random randomGenerator = new Random();
	
	public void EnemySpawner(GameView gameView){
		this.gameView = gameView;
	}
	
	// We're creating a 20 pixel border around the screen of size 100
	public Enemy initializeEnemy(){
		
		acceleration = 0;
		velocityX = 0;
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
			// 20 + screensize + 20 
			int edgeLeft = screensize + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeLeft) - bufferspace;
			
		}
		// On the top edge of the screen
		if(edge == 1){
			positionY = -bufferspace;
			int edgeTop = screensize + (bufferspace * 2) + 1;
			positionX = randomGenerator.nextInt(edgeTop) - bufferspace;
		}
		// On the right edge of the screen
		if(edge == 2){
			positionX = screensize + bufferspace;
			int edgeRight = screensize + bufferspace + 1;
			positionY = randomGenerator.nextInt(edgeRight) - bufferspace;
		}
	}

	
	private void setVelocity(){
		if (positionY < (screensize + bufferspace) /2){
			double velocityY =10;
			float distanceY = screensize + bufferspace- positionY;
			double time = (-velocityY + Math.sqrt( velocityY *velocityY + 2 * gravity *distanceY)) / gravity;
			float distanceX = positionX - (screensize/2);
			velocityX = distanceX / time;
			}else{
				double time = Math.sqrt((screensize + bufferspace) *2 / gravity);
				float distance = positionX - (screensize/2);
				velocityX = distance / time;
			}
	}
	
	
	


}