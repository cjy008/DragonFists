package com.dragonfist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
 
public class Sprite {
       private int x = 0;
       private int y = 0;
       private int width,height;
       private Bitmap bmp;
      
       public Sprite(Bitmap bmp) {
             this.bmp=bmp;
             width = bmp.getWidth();
             height = bmp.getHeight();
       }
 
       public int getWidth() {
           return width;
       }
       
       public int getHeight() {
           return height;
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
       public void setBmp(Bitmap bmp) 
       {
    	   this.bmp = bmp;
    	   width = bmp.getWidth();
           height = bmp.getHeight();
       }
       public Bitmap getBmp() 
       { return bmp; }
       
       /*public Bitmap transformBmp(Matrix matrix)
       {
    	   return Bitmap.createBitmap(bmp, x, y, getWidth(), getHeight(), matrix, false);
       }*/
       public void Draw(Canvas canvas,int x, int y) 
       {
             canvas.drawBitmap(bmp, x , y, null);
       }
       public void Draw(Canvas canvas,float x, float y) 
       {
           canvas.drawBitmap(bmp, (int)x , (int)y, null);
       }
       
       public void Draw(Canvas canvas,float x, float y,float alpha) 
       {
    	   Paint paint = new Paint();
    	   paint.setAlpha((int)alpha);
           canvas.drawBitmap(bmp, (int)x , (int)y, paint);
       }
       /**
        * 
        * @param canvas
        * @param x
        * @param y
        * @param Matrix that represents 
        */
       public void Draw(Canvas canvas,float x, float y, double velx, double vely,boolean flip) {
    	   Matrix matrix = new Matrix();
    	   if(flip)
    	   {
    		   matrix.postScale(-1, 1);
        	   matrix.postRotate((float)Math.toDegrees(Math.atan((vely)/(velx))), (width / (float)2)-width, (height / (float)2)); //rotate it
        	   matrix.postTranslate(x+width, y); //move it into x, y position
    	   }
    	   else
    	   {
	    	   matrix.postRotate((float)Math.toDegrees(Math.atan((vely)/(velx))), (width / (float)2), (height / (float)2)); //rotate it
	    	   matrix.postTranslate(x, y); //move it into x, y position
    	   }
           canvas.drawBitmap(bmp,matrix, null);
     }
       
    
}  