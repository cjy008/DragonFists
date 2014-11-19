package com.dragonfist;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Game extends Activity {

	GameView gameView; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(gameView = new GameView(this,savedInstanceState));
    }
    
    @Override
    public void onStart(Bundle savedInstanceState) {
        super.onStart();
        setContentView(gameView = new GameView(this,savedInstanceState));
        Log.d("onStart","onStart has been called");
    }
    
    @Override
    public void onResume() {
        super.onResume();

        Log.d("onResume","onResume has been called");
    }

    @Override
    public void onStop() {
        super.onStop();
        gameView.GT.setRunning(false);
        gameView = null;
        Log.d("onStop","onStop has been called");
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
