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
       
       /**
        * Getter Helper Method for X position
        * @return the left most index of the Bitmap rectangle
        */
       public int getX() {
           return x;
       }
       
       /**
        * Getter Helper Method for Y position
        * @return the top most most index of the Bitmap rectangle
        */
       public int getY() {
           return y;
       }
       
       /**
        * Setter Helper Method for X position
        * @param x the left index of the Bitmap rectangle
        */
       public void setX(int x) {
           this.x = x;
       }
       
       /**
        * Setter Helper Method for Y position
        * @param y the right rectangle of the Bitmap rectangle
        */
       public void setY(int y) {
    	   this.y = y;
       }
       public void setBmp(Bitmap bmp) {
    	   this.bmp = bmp;
       }
       public Bitmap getBmp() 
       { return bmp; }
       public void Draw(Canvas canvas) {
             canvas.drawBitmap(bmp, x , y, null);
       }
}  