package com.dragonfist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView 
{
	
	//Android syntax variables
    private SurfaceHolder holder;
    public GameThread GT;
    public static int screenWidth;
    public static int screenHeight;
    public static float testAspectRatio = (float)(1280.0/720.0);
    public static float aspectSkewFactor;
    public int bufferspace = 40;
    
    //Logic Variables
    private int taggedIndex;
    private GameView selfReference;
    private float startX, startY, currentX, currentY;
    private boolean draggable, lineDrawn;
    private Paint circlePaint;
    private Paint linePaint;
    private int circleRadius;
    private boolean punch;				//If the player releases an enemy, punch is set to true, the enemy will get hit, and
    									//draggable will be set to false on the next call to Update().
    
    //Sprites
    private Enemy enemies[];
    private Player player;

    private int numEnemies;
    
    EnemySpawner enemySpawner;

    public GameView(Context context,Bundle savedInstanceState) {
    	super(context);
    	
    	//Load Bitmap Images
    	
    	/* TODO add drawable resources to account for the different directional strikes
    	playerRightStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceRightStrike);
    	playerLeftStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceLeftStrike);
    	playerFrontStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceFrontStrike);
    	playerBackStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceBackStrike);
    	*/
    	
    	
    	//Load Android Syntax Variables
    	selfReference = this;
        holder = getHolder();
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        aspectSkewFactor = ((float)screenWidth/(float)screenHeight)/testAspectRatio;
        
        //Load Game Logic Variables
        numEnemies = 10;
        

    	player = new Player(this);
        //player = new Sprite(playerStandingBmp);
    	//player.setX(screenWidth/2 - player.getHeight()/2);
    	//player.setY((int)(screenHeight*3.0/4 - player.getHeight()/2.0));
    	
        enemies = new Enemy[numEnemies];

    	Log.d("aaaa", String.format("ScreenWidth: %d;  ScreenHeight: %d;", screenWidth, screenHeight));
        enemySpawner = new EnemySpawner(this);
        
        if(savedInstanceState==null)
        {
        	for(int i=0;i<numEnemies;i++)
	        {
	        	//enemies[i] = new Enemy(this, (float)((screenWidth/10.0)*i), (float)((screenHeight/10.0)*i), 1.0, 1.0);
	        	//enemies[i] = new Enemy(this, (float)(i*40), (float)(i*40), 1.0, 1.0);
	        	enemies[i] = enemySpawner.initializeEnemy();
	        }    
        }        
        else        	
        {
        	 // TODO Once we implement player strength: player.strength = savedInstanceState.getDouble("playerStrength", playerStrength);	        	 
        	 // TODO Once we have a score counter: score = savedInstanceState.getInt("score");      	
          	player.x = savedInstanceState.getInt("playerX", player.x);
          	player.y = savedInstanceState.getInt("playerY", player.y);
          	for(int i=0;i<numEnemies;i++)
  	        {
          		enemies[i] = new Enemy(savedInstanceState.getFloat(String.format("enemyX%d",i)),savedInstanceState.getFloat(String.format("enemyY%d",i)),
          		      	  savedInstanceState.getDouble(String.format("enemyVelX%d",i)),
          		      	  savedInstanceState.getDouble(String.format("enemyVelY%d",i)),
        		          savedInstanceState.getBoolean(String.format("enemyFlipped%d",i)),
          		      	  savedInstanceState.getFloat(String.format("enemyHealth%d",i)),
          		          savedInstanceState.getBoolean(String.format("enemyAlive%d",i)),
          		          savedInstanceState.getBoolean(String.format("enemyInitialized%d",i)));
  	        }
        }
        startX = 0;
        startY = 0;
        currentX = 0;
        currentY = 0;
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(6*(aspectSkewFactor));
        circleRadius = (int)(60*aspectSkewFactor);
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(6*((aspectSkewFactor)));
        circlePaint.setColor(Color.CYAN);
        circlePaint.setAlpha((int) 122);	//Half way translucent
        
        
        
        
        holder.addCallback(new SurfaceHolder.Callback() {

               @Override
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
                  boolean retry = true;
                  GT.setRunning(false);
                  while (retry) 
                  {
                     try 
                     {
                    	 GT.join();
                    	 retry = false;
                     } 
                     catch (InterruptedException e) 
                     { e.printStackTrace(); }
                  }
           	   	  GT = null;
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) 
               {
                   GT = new GameThread(selfReference);
            	   GT.setRunning(true);
            	   if(GT.getState()==Thread.State.NEW)
            	   {
            		   GT.start();
            	   }
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {}
        });
        
    }
    /**
     * We pass in the important state variables to the activity's instance state.
     * Intended Function: Everything else can be recreated from these variables.
     * 
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
      // Save UI state changes to the savedInstanceState.
    	

    	
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      for(int i=0; i<numEnemies;i++)
      {	
    	  savedInstanceState.putFloat(String.format("enemyX%d",i), enemies[i].x);
    	  savedInstanceState.putFloat(String.format("enemyY%d",i), enemies[i].y);
    	  savedInstanceState.putDouble(String.format("enemyVelX%d",i), enemies[i].velx);
    	  savedInstanceState.putDouble(String.format("enemyVelY%d",i), enemies[i].vely);
    	  savedInstanceState.putDouble(String.format("enemyHealth%d",i), enemies[i].health);
          savedInstanceState.putBoolean(String.format("enemyAlive%d",i), enemies[i].alive);
          savedInstanceState.putBoolean(String.format("enemyInitialized%d",i),enemies[i].initialized);
          savedInstanceState.putBoolean(String.format("enemyFlipped%d",i),enemies[i].flipped);
      }
      
      savedInstanceState.putFloat("playerX", player.x);
      savedInstanceState.putFloat("playerY", player.y);
	  // TODO Once we implement player strength: savedInstanceState.putDouble("playerStrength", playerStrength);
	  
 
	  // TODO Once we have a score counter: savedInstanceState.putInt("score",score);
      
      // etc.
    }
    
    /**
	 * Resize the Bitmap image to account for screen size and width
	 * @return the new scaled image that accounts for the screen size
	 */
	public Bitmap scaleFactor(Bitmap bmp)
	{
		float thisAspectRatio = ((float)(GameView.screenWidth)/(float)(GameView.screenHeight));
		//Scale y FIRST!!!
		float scaleY = GameView.screenWidth/GameView.screenHeight;
		//Scale the horizontal proportional to the horizontal;
		float scaleX = (thisAspectRatio/GameView.testAspectRatio)*(bmp.getWidth()*(scaleY/bmp.getHeight()));
		
		return Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth()*scaleX), (int)(bmp.getHeight()*scaleY), false);
	}

    protected void Draw(Canvas canvas) 
    {
    	canvas.drawColor(Color.BLACK);
		//canvas.drawBitmap(playerStandingBmp, x , 10, null);
		 
    	player.Draw(canvas);

		//Log.d("Beta Test","draggable: "+Boolean.toString(draggable));
		if(lineDrawn)
		{
			//canvas.drawLine(dragstartx,dragstarty,dragendx,dragendy, null);
			canvas.drawLine(startX,startY,currentX,currentY, linePaint);
			canvas.drawCircle(startX, startY, circleRadius, circlePaint);
		}
		for (int i=0; i<numEnemies;i++)
		{
			if(enemies[i].initialized)
			{
	//			Log.d("woohX",Integer.toString(enemies[i].getBody().getX()));
	//			Log.d("woohY",Integer.toString(enemies[i].getBody().getY()));
				enemies[i].Draw(canvas);
				//canvas.drawBitmap(playerStandingBmp, enemies[i].getBody().getX() , enemies[i].getBody().getY(), null);
			}
		}
		if(lineDrawn)
		{
			canvas.drawCircle(startX, startY, circleRadius, circlePaint);
		}
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) 
    {
        // Let the ScaleGestureDetector inspect all events.

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        switch (action & MotionEvent.ACTION_MASK) 
        {
	        case MotionEvent.ACTION_DOWN: 
	        {
	        	
	        	//Log.d("aaaa",String.format("Screen Coordinates = (%f,%f)",x,y));
//	        	startX = x;
//	        	startY = y;
	        	if(Snap(x,y))
	        	{
	        		draggable=true;
	        	}
	            //mActivePointerId = ev.getPointerId(0);
	            break;
	        }
	
	        case MotionEvent.ACTION_MOVE: 
	        {
	        	if(draggable)
	        	{
		        	lineDrawn = true;
		            //final int pointerIndex = ev.findPointerIndex(mActivePointerId);
		            // Only move if the ScaleGestureDetector isn't processing a gesture.
		            
		            currentX = x;
		            currentY = y;
	        	}
	
	            break;
	        }
	
	        case MotionEvent.ACTION_UP: 
	        {
	        	if(draggable)
	        	{
		        	punch=true;
		            draggable = false;
		            lineDrawn = false;
		            currentX = x;
		            currentY = y;
	        	}
	            break;
	        }
	
	        case MotionEvent.ACTION_CANCEL: 
	        {
	        	lineDrawn = false;
	            draggable = false;
	            //currentX = x;
	            //currentY = y;
	            break;
	        }
        }
        return true;
    }
    
    /**
     * Description: Round the inital player snap to the center of the closest enemy
     * @param x the horizontal coordinate of the initial tap point
     * @param y the vertical coordinate of the initial tap point
     */
    private boolean Snap(float x,float y)
    {
    	boolean change = false;
    	double smallestDist, currentDist;
    	smallestDist = Double.MAX_VALUE;
    	float centerX,centerY;
    	int width,height;
    	for (int i=0; i<numEnemies;i++)
    	{
    		if(enemies[i].alive)
    		{
	    		width = Enemy.sprite.getWidth();
	    		height = Enemy.sprite.getHeight();
	    		centerX = enemies[i].x+(width/2);
	    		centerY = enemies[i].y+(height/2);
	//    		Log.d("brisketbeef2",String.format("centerX: %f", centerX));
	//    		Log.d("brisketbeef2","centerY: "+Float.toString(centerY));
	//    		Log.d("brisketbeef2","width: "+String.format("%d", width));
	//    		Log.d("brisketbeef2","height: "+String.format("%d", height));
	    		if(Math.abs((x-centerX))<width/2)
	    		{
	    			if(Math.abs((y-centerY))<height/2)
	    			{	
	    				currentDist = Math.sqrt((Math.pow(x-centerX,2)+(Math.pow(y-centerY,2))));
		    			if(currentDist<smallestDist)
		    			{
		    				change = true;
		    				smallestDist = currentDist;	
		    				startX = centerX;
		    				startY = centerY;

		    				taggedIndex = i;
		    			}
	    			}
	    		}
    		}
    	}
    	return change;
    }
    
    public void pause(boolean paused)
    {
    	if(GT!=null)
    	{
    		GT.pause(paused);
    	}
    }
    
	public void Update(float timePassed)
	{
		timePassed = timePassed/1000;
		for(int i=0;i<numEnemies;i++)
		{
			if(enemies[i].initialized)
			{
				enemies[i].update(timePassed);
				if(i==taggedIndex)
				{
					if(draggable)
					{
						startX = enemies[i].getCenX();
						startY = enemies[i].getCenY();
					}
					if(punch)
					{
						player.hit(enemies[i],(currentX-startX),(currentY-startY));
						punch=false;
					}
				}
			}
		}
	}
	
	double Distance( Enemy a, Enemy b )
	{
	  return Math.sqrt( (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) );
	}
	 
	boolean isColliding( Enemy a, Enemy b )
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
	
	/*public static void main(String[] args){
		GameView test = new GameView();
	}
	*/
}
