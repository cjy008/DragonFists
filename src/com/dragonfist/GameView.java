package com.dragonfist;

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

public class GameView extends SurfaceView {
	private Bitmap bmp;
    private SurfaceHolder holder;
    private GameThread GT;
    private int x = 0; 
    private int xSpeed = 1;
    private float startX, startY, currentX, currentY;
    private boolean draggable;
    private Sprite enemies[];
    private int numEnemies;

    public GameView(Context context) {
    	super(context);
        GT = new GameThread(this);
        holder = getHolder();
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruce);
        numEnemies = 10;
        enemies = new Sprite[numEnemies];

        for(int i=0;i<numEnemies;i++)
        {
        	enemies[i] = new Sprite(bmp);
        	enemies[i].setX(i*20);
        	enemies[i].setY(i*20);       	
        }
        
        x = 0; 
        xSpeed = 1;
        startX = 0;
        startY = 0;
        currentX = 0;
        currentY = 0;
        
        holder.addCallback(new SurfaceHolder.Callback() {

               @Override
               public void surfaceDestroyed(SurfaceHolder holder) {
                      boolean retry = true;
                      GT.setRunning(false);
                      while (retry) {
                             try {
                            	 GT.join();
                            	 retry = false;
                             } catch (InterruptedException e) {
                             }
                      }
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) {
            	   GT.setRunning(true);
            	   GT.start();
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {
               }
        });
        
    }

    protected void Draw(Canvas canvas) {
	     if (x == getWidth() - bmp.getWidth()) {
	            xSpeed = -1;
	     }
	     if (x == 0) {
	            xSpeed = 1;
	     }
	     x = x + xSpeed;
	     canvas.drawColor(Color.BLACK);
	     canvas.drawBitmap(bmp, x , 10, null);
	     for (int i=0; i<numEnemies;i++)
	     {
	    	 Log.d("woohX",Integer.toString(enemies[i].getX()));
	    	 Log.d("woohY",Integer.toString(enemies[i].getY()));
	    	 canvas.drawBitmap(bmp, enemies[i].getX() , enemies[i].getY(), null);
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
    
    private void Snap(float x,float y)
    {
    	double smallestDist, currentDist;
    	smallestDist = Double.MAX_VALUE;
    	float centerX,centerY;
    	int width,height;
    	for (int i=0; i<numEnemies;i++)
    	{
    		width = enemies[i].getWidth();
    		height = enemies[i].getHeight();
    		centerX = enemies[i].getX()+(width/2);
    		centerY = enemies[i].getY()+(height/2);
    		if(Math.abs((x-centerX))<width/2)
    		{
    			if(Math.abs((y-centerY))<height/2)
    			{	
    				currentDist = Math.sqrt((Math.pow(x-centerX,2)+(Math.pow(y-centerY,2))));
	    			if(currentDist<smallestDist){
	    				smallestDist = currentDist;	
	    				currentX = centerX;
	    				currentY = centerY;
	    			}
    			}
    		}
    	}
    }
}
