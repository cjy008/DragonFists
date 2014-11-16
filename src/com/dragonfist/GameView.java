package com.dragonfist;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView 
{
	
	//Android syntax variables
    private SurfaceHolder holder;
    private GameThread GT;
    public static int screenWidth;
    public static int screenHeight;
    public static float testAspectRatio = (float)(1280.0/720.0);
    public int bufferspace = 40;
    
    //Logic Variables
    private int x;
    private int xSpeed;
    private float startX, startY, currentX, currentY;
    private boolean draggable, lineDrawn;
    
    //Sprites
    private Enemy enemies[];
    private Player player;
    private int numEnemies;
    
    EnemySpawner enemySpawner;

    public GameView(Context context) {
    	super(context);
    	
    	//Load Bitmap Images
    	
    	/* TODO add drawable resources to account for the different directional strikes
    	playerRightStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceRightStrike);
    	playerLeftStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceLeftStrike);
    	playerFrontStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceFrontStrike);
    	playerBackStrikeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruceBackStrike);
    	*/
    	
    	
    	//Load Android Syntax Variables
        GT = new GameThread(this);
        holder = getHolder();
        
        //Load Game Logic Variables
        numEnemies = 10;
        
        player = new Player(this);
        //player = new Sprite(playerStandingBmp);
    	//player.setX(screenWidth/2 - player.getHeight()/2);
    	//player.setY((int)(screenHeight*3.0/4 - player.getHeight()/2.0));
    	
        enemies = new Enemy[numEnemies];
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    	Log.d("aaaa", String.format("ScreenWidth: %d;  ScreenHeight: %d;", screenWidth, screenHeight));
        enemySpawner = new EnemySpawner(this);
        for(int i=0;i<numEnemies;i++)
        {
        	//enemies[i] = new Enemy(this, (float)((screenWidth/10.0)*i), (float)((screenHeight/10.0)*i), 1.0, 1.0);
        	//enemies[i] = new Enemy(this, (float)(i*40), (float)(i*40), 1.0, 1.0);
        	enemies[i] = enemySpawner.initializeEnemy();
        	
        	Log.d("Alpha Test", "enemies[i].x: "+Float.toString(enemies[i].x));
        }
        x = 0; 
        xSpeed = 1;
        startX = 0;
        startY = 0;
        currentX = 0;
        currentY = 0;
        
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
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) 
               {
            	   GT.setRunning(true);
            	   GT.start();
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {}
        });
        
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
		 
		for (int i=0; i<numEnemies;i++)
		{
			if(enemies[i].alive)
			{
	//			Log.d("woohX",Integer.toString(enemies[i].getBody().getX()));
	//			Log.d("woohY",Integer.toString(enemies[i].getBody().getY()));
				enemies[i].Draw(canvas);
				//canvas.drawBitmap(playerStandingBmp, enemies[i].getBody().getX() , enemies[i].getBody().getY(), null);
			}
		}
		//Log.d("Beta Test","draggable: "+Boolean.toString(draggable));
		if(lineDrawn)
		{
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			//canvas.drawLine(dragstartx,dragstarty,dragendx,dragendy, null);
			canvas.drawLine(startX,startY,currentX,currentY, paint);
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
	            draggable = false;
	            lineDrawn = false;
	            currentX = x;
	            currentY = y;
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
	    		width = enemies[i].getBody().getWidth();
	    		height = enemies[i].getBody().getHeight();
	    		centerX = enemies[i].getBody().getX()+(width/2);
	    		centerY = enemies[i].getBody().getY()+(height/2);
	//    		Log.d("brisketbeef2",String.format("centerX: %f", centerX));
	//    		Log.d("brisketbeef2","centerY: "+Float.toString(centerY));
	//    		Log.d("brisketbeef2","width: "+String.format("%d", width));
	//    		Log.d("brisketbeef2","height: "+String.format("%d", height));
	    		if(Math.abs((x-centerX))<width/2)
	    		{
	    			Log.d("brisket","I'm in da loop mah");
	    			if(Math.abs((y-centerY))<height/2)
	    			{	
	    				currentDist = Math.sqrt((Math.pow(x-centerX,2)+(Math.pow(y-centerY,2))));
		    			if(currentDist<smallestDist)
		    			{
		    				change = true;
		    				smallestDist = currentDist;	
		    				startX = centerX;
		    				startY = centerY;
		    			}
	    			}
	    		}
    		}
    	}
    	return change;
    }
    
	public void Update(float timePassed)
	{
		timePassed = timePassed/1000;
		for(int i=0;i<numEnemies;i++)
		{
			if(enemies[i].alive)
			{
				enemies[i].update(timePassed);
				if (enemies[i].y > screenHeight+bufferspace || enemies[i].y < -bufferspace || enemies[i].x < -bufferspace || enemies[i].x > screenWidth + bufferspace){
					enemies[i].alive = false;
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
