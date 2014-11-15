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
	//BMP
	private Bitmap playerStandingBmp; //Player Sprite Image
	private Bitmap playerRightStrikeBmp; // angle of attack from 45 to -45 degrees NoE
	private Bitmap playerLeftStrikeBmp; // angle of attack from 135 to 45 degrees NoE
	private Bitmap playerFrontStrikeBmp; // angle of attack from 225 to 135 degrees NoE
	private Bitmap playerBackStrikeBmp; // angle of attack from 225 to 315 (-45) degrees NoE
	
	private Bitmap enemyBmp[]; //Enemy Sprite Image (Have several to chose from)
	
	//Android syntax variables
    private SurfaceHolder holder;
    private GameThread GT;
    
    //Logic Variables
    private int x;
    private int xSpeed;
    private float startX, startY, currentX, currentY;
    private boolean draggable;
    
    //Sprites
    private Enemy enemies[];
    private Sprite player;
    private int numEnemies;

    public GameView(Context context) {
    	super(context);
    	
    	//Load Bitmap Images
    	playerStandingBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruce);
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
        
        
        player = new Sprite(playerStandingBmp);
    	player.setX(getWidth()/2 - player.getHeight()/2);
    	player.setY((int)(getHeight()*3.0/4 - player.getHeight()/2.0));
    	
        enemies = new Enemy[numEnemies];
        
        for(int i=0;i<numEnemies;i++)
        {
        	
        	enemies[i] = new Enemy(this, (float)i*getWidth()/10, (float)i*getHeight()/10, 1.0, 1.0);
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

    protected void Draw(Canvas canvas) {
	     if (x == getWidth() - playerStandingBmp.getWidth()) {
	            xSpeed = -1;
	     }
	     if (x == 0) {
	            xSpeed = 1;
	     }
	     x = x + xSpeed;
	     canvas.drawColor(Color.BLACK);
	     canvas.drawBitmap(playerStandingBmp, x , 10, null);
	     for (int i=0; i<numEnemies;i++)
	     {
	    	 Log.d("woohX",Integer.toString(enemies[i].getBody().getX()));
	    	 Log.d("woohY",Integer.toString(enemies[i].getBody().getY()));
	    	 canvas.drawBitmap(playerStandingBmp, enemies[i].getBody().getX() , enemies[i].getBody().getY(), null);
	     }
	     if(draggable)
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
	        	Snap(x,y);
	            //mActivePointerId = ev.getPointerId(0);
	            break;
	        }
	
	        case MotionEvent.ACTION_MOVE: 
	        {
	            //final int pointerIndex = ev.findPointerIndex(mActivePointerId);
	            // Only move if the ScaleGestureDetector isn't processing a gesture.
	            draggable = true;
	            currentX = x;
	            currentY = y;
	
	            break;
	        }
	
	        case MotionEvent.ACTION_UP: 
	        {
	            draggable = false;
	            currentX = x;
	            currentY = y;
	            break;
	        }
	
	        case MotionEvent.ACTION_CANCEL: 
	        {
	            draggable = false;
	            currentX = x;
	            currentY = y;
	            break;
	        }
        }
        return true;
    }
    
    /**
     * Description: Round the inital player snap to the center of the closest enemy
     * @param x is the horizontal coordinate of the initial tap point
     * @param y is the vertical coordinate of the initial tap point
     */
    private void Snap(float x,float y)
    {
    	double smallestDist, currentDist;
    	smallestDist = Double.MAX_VALUE;
    	float centerX,centerY;
    	int width,height;
    	for (int i=0; i<numEnemies;i++)
    	{
    		width = enemies[i].getBody().getWidth();
    		height = enemies[i].getBody().getHeight();
    		centerX = enemies[i].getBody().getX()+(width/2);
    		centerY = enemies[i].getBody().getY()+(height/2);
    		if(Math.abs((x-centerX))<width/2)
    		{
    			if(Math.abs((y-centerY))<height/2)
    			{	
    				currentDist = Math.sqrt((Math.pow(x-centerX,2)+(Math.pow(y-centerY,2))));
	    			if(currentDist<smallestDist)
	    			{
	    				smallestDist = currentDist;	
	    				startX = centerX;
	    				startY = centerY;
	    			}
    			}
    		}
    	}
    }
}
