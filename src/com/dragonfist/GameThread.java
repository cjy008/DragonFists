package com.dragonfist;

import android.graphics.Canvas;

public class GameThread extends Thread {
       static final long FPS = 30;
       private GameView view;
       private boolean running = false;
       private boolean pause = false;
      
       public GameThread(GameView view) {
             this.view = view;
       }
 
       public void setRunning(boolean run) {
             running = run;
       }
       
       public void pause(boolean paused){
    	   pause = paused;
       }
 
       @Override
       public void run() {
             long ticksPS = 1000 / FPS;
             long startTime;
             long sleepTime;
             while (running) {
            	 	if(pause)
            	 	{
            	 		try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            	 	}
            	 	else
            	 	{
	                    Canvas c = null;
	                    startTime = System.currentTimeMillis();
	                    try {
	                           c = view.getHolder().lockCanvas();
	                           synchronized (view.getHolder()) {
	                        	   //Update!
	                        	   view.Draw(c);
	                        	   view.Update(ticksPS);
	                        	   
	                           }
	                    } finally {
	                           if (c != null) {
	                                  view.getHolder().unlockCanvasAndPost(c);
	                           }
	                    }
	                    sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
	                    try {
	                           if (sleepTime > 0)
	                                  sleep(sleepTime);
	                    } catch (Exception e) {}
            	 	}
             }
       }
}