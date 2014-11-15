package com.dragonfist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
 
public class Sprite {
       private int x = 0;
       private int y = 0;
       private GameView gameView;
       private Bitmap bmp;
      
       public Sprite(Bitmap bmp) {
             this.bmp=bmp;
       }
 
       public int getWidth() {
           return bmp.getWidth();
       }
       
       public int getHeight() {
           return bmp.getHeight();
       }
       
       public int getX() {
           return bmp.getWidth();
       }
       
       public int getY() {
           return bmp.getHeight();
       }
       
       public void setX(int x) {
           this.x = x;
       }
       
       public void setY(int y) {
    	   this.y = y;
       }
      
       public void Draw(Canvas canvas) {
             canvas.drawBitmap(bmp, x , y, null);
       }
}  