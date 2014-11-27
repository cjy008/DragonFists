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
        Log.d("onCreate","onCreate has been called");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView = new GameView(this,savedInstanceState));
        try 
        {
			FileReader FR = new FileReader(GameView.saveFileName);
			char[] buffer = new char[10];
			if (FR.ready()) 
			{  
				int length = FR.read(buffer);
				for (int i = length - 1; i >= 0; i--)
				{
					GameView.highScore += buffer[i]*Math.pow(10, length - 1 - i);
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
        Log.d("onStart","onStart has been called");
        Scanner sc;
		try {
			sc = new Scanner(new File(GameView.saveFileName));
			if (sc.hasNextInt())
	        {
	        	GameView.highScore = sc.nextInt();
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("HIGH SCORE STUFF!!!!", "highscore.txt not found");
		}
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        gameView.pause(false);
        gameView.stall();
        
        Log.d("onResume","onResume has been called");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        gameView.pause(true);
        Log.d("onPause","onPause has been called");
    }

    @Override
    public void onStop() 
    {
//    	FileOutputStream FOS = new FileOutputStream(GameView.saveFileName);
//    	FOS.write(GameView.highScore);
//    	FOS.close();
    	//GameView.highScore;
        super.onStop();
        Log.d("onStop","onStop has been called");
    }
    
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        gameView.GT.setRunning(false);
        gameView = null;
        Log.d("onDestroy","onDestroy has been called");
    }

    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      Log.d("onSaveInstanceState","onSaveInstanceState has been called");
  	  gameView.onSaveInstanceState(savedInstanceState);
      // etc.
    }
    
    
}
