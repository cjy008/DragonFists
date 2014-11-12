package com.example.dragonfist;
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
      
       public void Draw(Canvas canvas) {
             canvas.drawBitmap(bmp, x , 10, null);
       }
}  