package com.dragonfist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Game extends Activity {

	public GameView gameView; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView = new GameView(this,savedInstanceState));
        try 
        {
        	File file = new File(getFilesDir(), GameView.saveFileName);
			FileReader FR = new FileReader(file);
			char[] buffer = new char[10];
			if (FR.ready()) 
			{  
				int length = FR.read(buffer);
				for (int i = length - 1; i >= 0; i--)
				{
					GameView.highScore += buffer[i]*Math.pow(10, length - 1 - i);
					Log.d("HIGH SCORE STUFF!!!!", String.format("High Score retrieved in onCreate with a value of %d", GameView.highScore));
				}
			}
			FR.close();
		} 
        catch (Exception e1) 
        { e1.printStackTrace(); }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        Scanner sc;
		try {
			sc = new Scanner(new File(getFilesDir(), GameView.saveFileName));
			if (sc.hasNextInt())
	        {
	        	int temp = sc.nextInt();
				if (temp > GameView.highScore)
				{ GameView.highScore = temp; }
	        	Log.d("HIGH SCORE STUFF!!!!", String.format("High Score retrieved in onStart with a value of %d", GameView.highScore));
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        gameView.pause(false);
        gameView.stall();
        
        
    }
    
    @Override
    public void onPause() {
        super.onPause();
        gameView.pause(true);
        
    }

    @Override
    public void onStop() 
    {
        super.onStop();
        
    }
    
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        gameView.GT.setRunning(false);
        gameView = null;
        
    }
    
    @Override
    public void onBackPressed()
    {
    	// This will cause the app to exit when the back button is pushed.
    	this.moveTaskToBack(true);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      
  	  gameView.onSaveInstanceState(savedInstanceState);
    }
    
    
}
