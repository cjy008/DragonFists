package com.dragonfist;

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
        
        /*
        Button game_state = (Button)findViewById(R.id.game_state);
        if (game_state != null)
        {
	        game_state.setText("Start");
	        
	        game_state.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gameView.pause(false);
					v.setVisibility(View.INVISIBLE);
				}
			});
        }
        */
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart","onStart has been called");
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
    public void onStop() {
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
