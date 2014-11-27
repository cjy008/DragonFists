package com.dragonfist;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
	//public static Sprite sprite;
	private static Sprite enemySprites[];
	
	public static final int ENEMY_LEFT = 0;
	public static final int ENEMY_LEFT2 = 1;
	public static final int ENEMY_UP = 2;
	public static final int ENEMY_RIGHT = 3;
	public static final int ENEMY_RIGHT2 = 4;
	public static final int DEAD_UP_LEFT = 5;
	public static final int DEAD_UP_RIGHT = 6;
	public static final int DEAD_RIGHT = 7;
	public static final int DEAD_DOWN_RIGHT = 8;
	public static final int DEAD_DOWN_LEFT = 9;
	public static final int DEAD_LEFT = 10;
	
	public static void initializeSprites(GameView GV)
	{
		enemySprites = new Sprite[11];
		enemySprites[0] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.enemy4_left)));
		enemySprites[1] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.enemy5_left)));
		enemySprites[2] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.enemy3)));
		enemySprites[3] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.enemy4_right)));
		enemySprites[4] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.enemy5_right)));
		enemySprites[5] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_upleft)));
		enemySprites[6] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_upright)));
		enemySprites[7] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_right)));
		enemySprites[8] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_downright)));
		enemySprites[9] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_downleft)));
		enemySprites[10] = new Sprite(GameView.scaleFactor(BitmapFactory.decodeResource(GV.getResources(), R.drawable.dead_enemy_left)));
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
		spriteIndex = ENEMY_UP;
		Random randNumGenerator = new Random();
		
		if (x > 0 && x < GameView.screenWidth)
		{
			spriteIndex = ENEMY_UP;
		}
		else
		{
			if (x <= 0)
			{ 
				if (randNumGenerator.nextInt(2) == 0)
				{ spriteIndex = ENEMY_LEFT; }
				else
				{ spriteIndex = ENEMY_LEFT2; }
			}
			else
			{ 
				if (randNumGenerator.nextInt(2) == 0)
				{ spriteIndex = ENEMY_RIGHT; }
				else
				{ spriteIndex = ENEMY_RIGHT2; }
			}
		}
		
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
		
		spriteIndex = ENEMY_UP;
		Random randNumGenerator = new Random();
		
		
		if (velx <= vely)
		{
			spriteIndex = ENEMY_UP;
		}
		else
		{
			if (velx >= 0)
			{ 
				if (randNumGenerator.nextInt(2) == 0)
				{ spriteIndex = ENEMY_LEFT; }
				else
				{ spriteIndex = ENEMY_LEFT2; }
			}
			else
			{ 
				if (randNumGenerator.nextInt(2) == 0)
				{ spriteIndex = ENEMY_RIGHT; }
				else
				{ spriteIndex = ENEMY_RIGHT2; }
			}
		}
		
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
		enemySprites[spriteIndex].Draw(canvas,x,y);//,velx,vely,flipped);
	}
	
	/**
	 * @return the x position of the sprite's center
	 */
	public float getCenX()
	{
		return x+(getSprite().getWidth()/2);
	}
	/**
	 * 
	 * @return the y position of the sprite's center
	 */
	public float getCenY()
	{

		return y+(getSprite().getHeight()/2);
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

			Player.killCount += 1;
			setSprite(xFor, yFor);
		}
	}
	
	public Sprite getSprite()
	{ return enemySprites[spriteIndex];	}
	
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
				if (xDir >= 0)
				{
					spriteIndex = DEAD_DOWN_RIGHT; 
				}
				else
				{ 
					spriteIndex = DEAD_DOWN_LEFT; 
				}
			}
			else
			{ 
				if (xDir >= 0)
				{
					spriteIndex = DEAD_UP_RIGHT; 
				}
				else
				{ 
					spriteIndex = DEAD_UP_LEFT; 
				}		
			}
		}
	}
	
	public boolean isCollision(float otherX1, float otherX2, float otherY1, float otherY2)
	{
		float halfSpriteWidth = enemySprites[this.spriteIndex].getWidth()/2;
		float halfSpriteHeight = enemySprites[this.spriteIndex].getHeight()/2;
		if(otherX1<(halfSpriteWidth+this.x))
			{
				//Log.d("Zeta Test",String.format("enemySprites[this.spriteIndex].getWidth()+this.x: %f",(enemySprites[this.spriteIndex].getWidth()+this.x)));
				if(otherX2>this.x-halfSpriteWidth)
				{
				//	Log.d("Zeta Test", String.format("this.x: %f",this.x));
					if(otherY1<(halfSpriteHeight+this.y))
					{
						if(otherY2>this.y-halfSpriteHeight)
						{

							Log.d("Succesful Collision","Succesful Collision");
							return true;
						}
					}
				}
			}
		return false;
	}
	
//	public collision(Enemy enemy){
//		double scalar1 = (Math.pow(this.velx, 2)+Math.pow(this.vely, 2));
//		double scalar2 = (Math.pow(enemy.velx, 2)+Math.pow(enemy.vely, 2));
//		double energy1 = 0.5*(scalar1);
//		double energy2 = 0.5*(scalar2);
//		double mom1x = this.velx;
//		double mom1y = this.vely;
//		double mom2x = enemy.velx;
//		double mom2y = enemy.vely;
////		energy1+energy2 = energyFinal = finalEn1+finalEn2;
////		mom1x+mom2x = momXFinal = finalXVel1+finalXVel2;
////		mom1y+mom2y = momYFinal = finalYVel1+finalYVel2;
//	}
	
	public Vector2 getPosVector(){
		return new Vector2(x,y);
	}
	
	public Vector2 getVelVector(){
		return new Vector2((float)velx,(float)vely);
	}
	
	//http://gamedev.stackexchange.com/questions/7862/is-there-an-algorithm-for-a-pool-game
	public void collision(Enemy enemy)
	{
		Vector2 posVector = getPosVector();
		Vector2 velVector = getVelVector();
		Vector2 otherPosVector = enemy.getPosVector();
		Vector2 otherVelVector = enemy.getVelVector();
		
	    Vector2 delta = (posVector.subtract(otherPosVector));
	    double d = delta.length();
	    delta.divide((float)d);
	    Vector2 normal = new Vector2(delta);

	    Vector2 velocityDelta = posVector.subtract(otherPosVector);

	    float dot = velocityDelta.dotProduct(normal);
	    
	    if (dot > 0) {
	        float coefficient = 0.2f;
	        float impulseStrength = (1 + coefficient) * dot;
	        Vector2 impulse = new Vector2(normal);
	        impulse.multiply(impulseStrength);
	        
	        this.hit(impulse.x,impulse.y);
	        
	        Vector2 otherEndVel = otherVelVector.subtract(impulse);
	        enemy.velx = otherEndVel.x;
	        enemy.vely = otherEndVel.y;
	    }

	}
}
