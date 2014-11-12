package com.dragonfist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private Bitmap bmp;
    private SurfaceHolder holder;
    private GameThread GT;
    private int x = 0; 
    private int xSpeed = 1;

    public GameView(Context context) {
    	super(context);
        GT = new GameThread(this);
        holder = getHolder();

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
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bruce);
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
    }
}
