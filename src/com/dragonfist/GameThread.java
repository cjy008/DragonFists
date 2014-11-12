package com.dragonfist;

import android.graphics.Canvas;

public class GameThread extends Thread {
       static final long FPS = 30;
       private GameView view;
       private boolean running = false;
      
       public GameThread(GameView view) {
             this.view = view;
       }
 
       public void setRunning(boolean run) {
             running = run;
       }
 
       @Override
       public void run() {
             long ticksPS = 1000 / FPS;
             long startTime;
             long sleepTime;
             while (running) {
                    Canvas c = null;
                    startTime = System.currentTimeMillis();
                    try {
                           c = view.getHolder().lockCanvas();
                           synchronized (view.getHolder()) {
                        	   //Update!
                        	   view.Draw(c);
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