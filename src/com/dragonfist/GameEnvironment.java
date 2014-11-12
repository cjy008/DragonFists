package com.dragonfist;


public class GameEnvironment {
	
	public Enemy[] enemies;
	public EnemySpawner es;
	
	
	public GameEnvironment()
	{
		es = new EnemySpawner();
		enemies = new Enemy[4];
		for(int i=0;i<4;i++)
		{
			enemies[i] = es.initializeEnemy();
		}
	}
	
	public void Update(float timePassed)
	{
		for(int i=0;i<4;i++)
		{
			enemies[i].update(timePassed);
		}
	}
	
	double Distance( Enemy a, Enemy b )
	{
	  return Math.sqrt( (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) );
	}
	 
	boolean isColiding( Enemy a, Enemy b )
	{
	  float r = a.radius + b.radius;
	  return r < Distance( a, b );
	}
	 
	/*boolean CirclevsCircleOptimized( Enemy a, Enemy b )
	{
	  float r = a.radius + b.radius;
	  r *= r;
	  return r < (a.x + b.x)^2 + (a.y + b.y)^2;
	}*/
	
	public static void main(String[] args){
		GameEnvironment test = new GameEnvironment();
	}
}
