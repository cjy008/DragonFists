package com.dragonfist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class GameView extends SurfaceView 
{
	
	//Android syntax variables
    private SurfaceHolder holder;
    public GameThread GT;
    public static int screenWidth;
    public static int screenHeight;
    public static float testAspectRatio = (float)(1280.0/720.0);
    public static int testWidth = 1280;
    public static int testHeight = 720;
    public static int highScore;
    public static final String saveFileName = "HighScore.txt";
    
    public float hitEffectiveness = 1f;
    
    public static float aspectSkewFactor;
    public int bufferspace = 40;
    
    //Logic Variables
    private int taggedIndex;
    private GameView selfReference;
    private Bitmap background;
    
    private float startX, startY, currentX, currentY;
    private boolean draggable, lineDrawn;
    private Paint circlePaint;
    private int circleRadius;
    private Paint linePaint;
    
    private int scoreTextXPos;
    private int scoreTextYPos;
    private Paint scorePaint;
    
    private boolean gameOver;
    private String highScoreText;
    private Paint highScorePaint;
    private int highScoreXPos;
    private int highScoreYPos;
    
    private Paint rectPaint;
    private Paint textPaint;
    private Rect textButton;
    private boolean textVisible;
    
    private Rect strengthBackground;
    private Rect strengthBar;
    private Paint strengthBackPaint;
    private Paint strengthBarPaint;
    private double remainingStrength; // strength adjusted to the screen dimensions
    private boolean isPaused;
    
    private Bitmap treasure;
    private float treasureXPos;
    private float treasureYPos;
    private Paint treasurePaint;
    
    private String text;
    
    private boolean punch;				//If the player releases an enemy, punch is set to true, the enemy will get hit, and
    									//draggable will be set to false on the next call to Update().
    
    //Sprites
    private Enemy enemies[];
    private Player player;

    private int numEnemies;
    
    EnemySpawner enemySpawner;

    public GameView(Context context,Bundle savedInstanceState) {
    	super(context);
    	gameOver = false;
    	//Load Android Syntax Variables
    	selfReference = this;
        holder = getHolder();
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        aspectSkewFactor = ((float)screenWidth/(float)screenHeight)/testAspectRatio;
        
        //Load all Bitmap images and create all Sprite objects
        background = BitmapFactory.decodeResource(getResources(), R.drawable.dragonfistbackground);
        background = scaleFactor(background);
        Enemy.initializeSprites(this);
        player = new Player(this);
    	
        // Strength bar
        remainingStrength = (screenWidth - screenWidth/25*2)* (player.strength)/100;
        
        strengthBackground = new Rect();
        strengthBackground.left = (int) (screenWidth/25);
        strengthBackground.right = (int) (screenWidth - screenWidth/25);
        strengthBackground.top = (int)(screenHeight*7/8);
        strengthBackground.bottom = (screenHeight- screenHeight/15);
        
        strengthBar = new Rect();
        strengthBar.left = (int) (screenWidth/25);
        strengthBar.right = (int) (screenWidth/25 + remainingStrength);
        strengthBar.bottom = (screenHeight- screenHeight/15);
        strengthBar.top =(int)(screenHeight*7/8);
        
        strengthBackPaint = new Paint ();
        strengthBackPaint.setARGB(128, 150, 150, 150);
        
        strengthBarPaint = new Paint();
        strengthBarPaint.setARGB(200, 100, 200, 100);
        isPaused = false;
        
        //Load Game Logic Variables
        numEnemies = 20;
    	
        enemies = new Enemy[numEnemies];

    	Log.d("aaaa", String.format("ScreenWidth: %d;  ScreenHeight: %d;", screenWidth, screenHeight));
        enemySpawner = new EnemySpawner(this);
        
        startX = 0;
        startY = 0;
        currentX = 0;
        currentY = 0;
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(6*(aspectSkewFactor));
        circleRadius = (int)(60*aspectSkewFactor);
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(6*((aspectSkewFactor)));
        circlePaint.setColor(Color.CYAN);
        circlePaint.setAlpha((int) 122);	//Half way translucent
       
        textPaint = new Paint();
        textPaint.setARGB(255, 0, 0, 255);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(60*screenHeight/testHeight);	//Change the Constant Value to increase/decrease the size of the text
        textPaint.setTextAlign(Paint.Align.CENTER);
 
        textVisible = true;
        text = "Start";
        textButton = new Rect();
        
        //This is used to measure the size of the rectangle around the text: DO NOT CHANGE!!!!!
        textPaint.getTextBounds(text, 0, text.length(), textButton);
        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
        textButton.bottom += screenHeight/2;
        textButton.top += screenHeight/2;
        
        rectPaint = new Paint();
        rectPaint.setARGB(255, 255, 255, 51);
        
        
        //Initialization of paint for Score Counter
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);
        float textSize = scorePaint.getTextSize();
        scorePaint.setTextSize(textSize*3*screenHeight/testHeight);
        scoreTextXPos = screenWidth - 275*screenWidth/testWidth;
        scoreTextYPos = screenHeight - 60*screenHeight/testHeight;
        //Initialize Start Button
        
        treasure = BitmapFactory.decodeResource(getResources(), R.drawable.treasure);
        treasure = Bitmap.createScaledBitmap(treasure, (int)(treasure.getWidth()/3), (int)(treasure.getHeight()/4), false);
        treasure = scaleFactor(treasure);
        treasureXPos = (float) (screenWidth/2.0- treasure.getHeight()/2.0);
        treasureYPos = (float) (screenHeight*3/4.0 - treasure.getHeight()/2.0);
        treasurePaint = new Paint();
        
        highScoreText = "";
        highScorePaint = new Paint();
        //highScorePaint.setColor(Color.WHITE);
        highScorePaint.setARGB(255, 00, 200, 00);
        highScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        highScorePaint.setTextSize(100*screenHeight/testHeight);
        highScorePaint.setTextAlign(Align.CENTER);
        highScoreXPos = screenWidth/2;
        highScoreYPos = (int)highScorePaint.getTextSize();
        
        
        
        if(savedInstanceState==null)
        {
        	for(int i=0;i<numEnemies;i++)
	        {
	        	enemies[i] = new Enemy();
	        }
        	/*
        	
        	for(int i=0;i<numEnemies;i++)
	        {
	        	//enemies[i] = new Enemy(this, (float)((screenWidth/10.0)*i), (float)((screenHeight/10.0)*i), 1.0, 1.0);
	        	//enemies[i] = new Enemy(this, (float)(i*40), (float)(i*40), 1.0, 1.0);
	        	enemies[i] = enemySpawner.initializeEnemy();
	        }
        	for(int i=0;i<4;i++)
	        {
	        	//enemies[i] = new Enemy(this, (float)((screenWidth/10.0)*i), (float)((screenHeight/10.0)*i), 1.0, 1.0);
	        	//enemies[i] = new Enemy(this, (float)(i*40), (float)(i*40), 1.0, 1.0);
	        	enemies[i].initialized = false;

	        }    */
        }        
        else        	
        {
        	player.strength = savedInstanceState.getDouble("playerStrength", player.maxStrength);
        	player.killCount = savedInstanceState.getInt("score");      	
          	player.x = savedInstanceState.getInt("playerX", player.x);
          	player.y = savedInstanceState.getInt("playerY", player.y);
          	for(int i=0;i<numEnemies;i++)
  	        {
          		enemies[i] = new Enemy(savedInstanceState.getFloat(String.format("enemyX%d",i)),savedInstanceState.getFloat(String.format("enemyY%d",i)),
          		      	  savedInstanceState.getDouble(String.format("enemyVelX%d",i)),
          		      	  savedInstanceState.getDouble(String.format("enemyVelY%d",i)),
        		          savedInstanceState.getBoolean(String.format("enemyFlipped%d",i)),
          		      	  savedInstanceState.getFloat(String.format("enemyHealth%d",i)),
          		          savedInstanceState.getBoolean(String.format("enemyAlive%d",i)),
          		          savedInstanceState.getBoolean(String.format("enemyInitialized%d",i)));
  	        }
        }
        
        
        holder.addCallback(new SurfaceHolder.Callback() {

               @Override
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
                  boolean retry = true;
                  GT.setRunning(false);
                  while (retry) 
                  {
                     try 
                     {
                    	 GT.join();
                    	 retry = false;
                     } 
                     catch (InterruptedException e) 
                     { e.printStackTrace(); }
                  }
           	   	  GT = null;
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) 
               {
                   GT = new GameThread(selfReference);
            	   GT.setRunning(true);
            	   if(GT.getState()==Thread.State.NEW)
            	   {
            		   GT.start();
            	   }
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {}
        });
        
    }
    /**
     * We pass in the important state variables to the activity's instance state.
     * Intended Function: Everything else can be recreated from these variables.
     * 
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
      // Save UI state changes to the savedInstanceState.
    	

    	
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      for(int i=0; i<numEnemies;i++)
      {	
    	  savedInstanceState.putDouble("playerStrength", player.strength);
    	  savedInstanceState.putInt("score", Player.killCount);
    	  savedInstanceState.putInt("highScore", highScore);
    	  savedInstanceState.putFloat(String.format("enemyX%d",i), enemies[i].x);
    	  savedInstanceState.putFloat(String.format("enemyY%d",i), enemies[i].y);
    	  savedInstanceState.putDouble(String.format("enemyVelX%d",i), enemies[i].velx);
    	  savedInstanceState.putDouble(String.format("enemyVelY%d",i), enemies[i].vely);
    	  savedInstanceState.putDouble(String.format("enemyHealth%d",i), enemies[i].health);
          savedInstanceState.putBoolean(String.format("enemyAlive%d",i), enemies[i].alive);
          savedInstanceState.putBoolean(String.format("enemyInitialized%d",i),enemies[i].initialized);
          savedInstanceState.putBoolean(String.format("enemyFlipped%d",i),enemies[i].flipped);
      }
      
      savedInstanceState.putFloat("playerX", player.x);
      savedInstanceState.putFloat("playerY", player.y);
	  // TODO Once we implement player strength: savedInstanceState.putDouble("playerStrength", playerStrength);
	  
 
	  // TODO Once we have a score counter: savedInstanceState.putInt("score",score);
      
      // etc.
    }
    
    private void updateStrength(float timePassed){
    	
	    if (player.strength <100){
	    		player.strength += (0.33*timePassed*(1000/33)); 
	    }
    	remainingStrength = (screenWidth - screenWidth/25*2)* (player.strength)/100;
    	strengthBar.right = (int) (screenWidth/25 + remainingStrength);
    	
    	if (player.strength < 20){
        strengthBarPaint.setARGB(200, 200, 100, 100);
        strengthBarPaint.setColor(Color.RED);
        linePaint.setColor(Color.RED);}
    	
    	if (player.strength > 20){
        strengthBarPaint.setARGB(255, 100, 200, 100);
        linePaint.setARGB(255, 100, 200, 100);
        linePaint.setColor(Color.CYAN);}
    }
    
    /**
	 * Resize the Bitmap image to account for screen size and width
	 * @return the new scaled image that accounts for the screen size
	 */
	public static Bitmap scaleFactor(Bitmap bmp)
	{
		float scaleY = (float)(GameView.screenHeight)/GameView.testHeight;
		float scaleX = (float)(GameView.screenWidth)/GameView.testWidth;
		
		return Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth()*scaleX), (int)(bmp.getHeight()*scaleY), false);
	}

    protected void Draw(Canvas canvas) 
    {
    	canvas.drawColor(Color.BLACK);
    	canvas.drawBitmap(background, 0, 0, null);
    	
        
        
        canvas.drawBitmap(treasure, treasureXPos, treasureYPos, treasurePaint);
    	
    	player.Draw(canvas);
    	
    	
    	//Score Counter Display
        
		for (int i=0; i<numEnemies;i++)
		{
			if(enemies[i].initialized)
			{
	//			Log.d("woohX",Integer.toString(enemies[i].getBody().getX()));
	//			Log.d("woohY",Integer.toString(enemies[i].getBody().getY()));
				enemies[i].Draw(canvas);
				//canvas.drawBitmap(playerStandingBmp, enemies[i].getBody().getX() , enemies[i].getBody().getY(), null);
			}	
		}
		if(lineDrawn)
		{
			canvas.drawLine(startX,startY,currentX,currentY, linePaint);
			canvas.drawCircle(startX, startY, circleRadius, circlePaint);
		}
		if (textVisible)
		{
			canvas.drawRect(textButton, rectPaint);
			canvas.drawText(text, screenWidth/2, screenHeight/2, textPaint);
		}
		
		//HUD elements: strength bar and score
		canvas.drawRect(strengthBackground, strengthBackPaint);
    	canvas.drawRect(strengthBar, strengthBarPaint);
    	canvas.drawText("Score: " + player.killCount, scoreTextXPos, scoreTextYPos, scorePaint);
    	if (gameOver)
    	{ canvas.drawText(highScoreText, highScoreXPos, highScoreYPos, highScorePaint); }
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) 
    {
        // Let the ScaleGestureDetector inspect all events.

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        switch (action & MotionEvent.ACTION_MASK) 
        {
	        case MotionEvent.ACTION_DOWN: 
	        {
	        	if (textVisible)
	        	{
	        		if (gameOver) { text = "Start"; }
	        		float width = textPaint.measureText(text);
	        		float height = textPaint.getTextSize();
	        		if (!(x >  screenWidth/2 + width/2 || x < screenWidth/2 - width/2 || y > screenHeight/2 + height/2 || y < screenHeight/2 - height/2))
	        		{ 
		        		textPaint.setARGB(255, 255, 0, 0);
		        		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		        		
		        		textPaint.getTextBounds(text, 0, text.length(), textButton);
        		        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
        		        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
        		        textButton.bottom += screenHeight/2;
        		        textButton.top += screenHeight/2;
	        		}
	        		else
	        		{ if (gameOver) {text = "Game Over"; } }
	        	}
	        	//Log.d("aaaa",String.format("Screen Coordinates = (%f,%f)",x,y));
//	        	startX = x;
//	        	startY = y;
	        	if(Snap(x,y))
	        	{
	        		draggable=true;
	        	}
	            //mActivePointerId = ev.getPointerId(0);
	            break;
	        }
	
	        case MotionEvent.ACTION_MOVE: 
	        {
	        	if (textVisible)
	        	{
	        		if (gameOver) { text = "Start"; }
	        		
	        		float width = textPaint.measureText(text);
	        		float height = textPaint.getTextSize();
	        		if (x >  screenWidth/2 + width/2 || x < screenWidth/2 - width/2 || y > screenHeight/2 + height/2 || y < screenHeight/2 - height/2)
	        		{ 
	        			textPaint.setTypeface(Typeface.DEFAULT);
	        			textPaint.setARGB(255, 0, 0, 255);
	        			if (gameOver)
	        			{
	        				//This is used to measure the size of the rectangle around the text: DO NOT CHANGE!!!!!
	        				text = "Game Over";
	        		        textPaint.getTextBounds(text, 0, text.length(), textButton);
	        		        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
	        		        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
	        		        textButton.bottom += screenHeight/2;
	        		        textButton.top += screenHeight/2;
	        			}
	        		}
	        		else
	        		{
	        			textPaint.setARGB(255, 255, 0, 0);
		        		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		        		if (gameOver)
		        		{
		        			text = "Start";

	        		        textPaint.getTextBounds(text, 0, text.length(), textButton);
	        		        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
	        		        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
	        		        textButton.bottom += screenHeight/2;
	        		        textButton.top += screenHeight/2;
		        		}
	        		}
	        		
	        	}
	        	if(draggable)
	        	{
		        	lineDrawn = true;
		            //final int pointerIndex = ev.findPointerIndex(mActivePointerId);
		            // Only move if the ScaleGestureDetector isn't processing a gesture.
		            float diffx = x-startX;
		            float diffy = y-startY;
		            if((Math.sqrt((Math.pow(diffx,2)+(Math.pow(diffy,2))))/ GameView.screenWidth /Player.reduceStrengthVariable *100)>player.strength)
		            {
		            	double allowedDist = (player.strength/(100.0*Player.reduceStrengthVariable))*GameView.screenWidth;
		            	double radians = Math.atan(diffy/diffx);
		            	if(diffx<0)
		            	{
		            		radians+=Math.PI;
		            	}
	            	
		            	currentX = (float)(allowedDist*Math.cos(radians))+startX;
	            		currentY = (float)(allowedDist*Math.sin(radians))+startY;
		            }
		            else
		            {	            	
			            currentX = x;
			            currentY = y;
		            }
	        	}
	
	            break;
	        }
	
	        case MotionEvent.ACTION_UP: 
	        {
	        	if (textVisible)
	        	{
	        		float width = textPaint.measureText(text);
	        		float height = textPaint.getTextSize();
	        		textPaint.setTypeface(Typeface.DEFAULT);
        			textPaint.setARGB(255, 0, 0, 255);
	        		if (!(x >  screenWidth/2 + width/2 || x < screenWidth/2 - width/2 || y > screenHeight/2 + height/2 || y < screenHeight/2 - height/2))
	        		{
	        			textVisible = false;
	        			if (gameOver)
	        			{ 
	        				gameOver = false;
	        				restart();
	        			}
	        		}
	        		else
	        		{
	        			if (gameOver)
	        			{
	        				text = "Game Over";
	        				textPaint.getTextBounds(text, 0, text.length(), textButton);
	        		        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
	        		        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
	        		        textButton.bottom += screenHeight/2;
	        		        textButton.top += screenHeight/2;
	        			}
	        		}
	        			
	        	}
	        	if(draggable)
	        	{
		        	punch=true;
		            draggable = false;
		            lineDrawn = false;
		            
		            //Begin strength calculation cde
		            
		            float diffx = x-startX;
		            float diffy = y-startY;
		            if((Math.sqrt((Math.pow(diffx,2)+(Math.pow(diffy,2))))/ GameView.screenWidth /Player.reduceStrengthVariable *100)>player.strength)
		            {
		            	double allowedDist = (player.strength/(100.0*Player.reduceStrengthVariable))*GameView.screenWidth;
		            	double radians = Math.atan(diffy/diffx);
		            	if(diffx<0)
		            	{
		            		radians+=Math.PI;
		            	}
	            	
		            	currentX = (float)(allowedDist*Math.cos(radians))+startX;
	            		currentY = (float)(allowedDist*Math.sin(radians))+startY;
		            }
		            else
		            {	            	
			            currentX = x;
			            currentY = y;
		            }
		            
		            //end strength calculation cde
	        	}
	            break;
	        }
	
	        case MotionEvent.ACTION_CANCEL: 
	        {
	        	lineDrawn = false;
	            draggable = false;
	            break;
	        }
        }
        return true;
    }
    
    /**
     * Called when the GameOver button is clicked: Reinitialize enemies
     */
    private void restart() 
    {
    	player = new Player(this);
    	text = "Start";
		// TODO Auto-generated method stub
    	for(int i=0;i<numEnemies;i++)
        {
        	//enemies[i] = new Enemy(this, (float)((screenWidth/10.0)*i), (float)((screenHeight/10.0)*i), 1.0, 1.0);
        	//enemies[i] = new Enemy(this, (float)(i*40), (float)(i*40), 1.0, 1.0);
        	enemies[i] = new Enemy();
        }
	}
	/**
     * Description: Round the inital player snap to the center of the closest enemy
     * @param x the horizontal coordinate of the initial tap point
     * @param y the vertical coordinate of the initial tap point
     */
    private boolean Snap(float x,float y)
    {
    	boolean change = false;
    	double smallestDist, currentDist;
    	smallestDist = Double.MAX_VALUE;
    	float centerX,centerY;
    	int width,height;
    	for (int i=0; i<numEnemies;i++)
    	{
    		if(enemies[i].alive)
    		{
	    		width = enemies[i].getSprite().getWidth();
	    		height = enemies[i].getSprite().getHeight();

	    		centerX = enemies[i].x+(width/2);
	    		centerY = enemies[i].y+(height/2);

	    		if(Math.abs((x-centerX))<width/2)
	    		{
	    			if(Math.abs((y-centerY))<height/2)
	    			{	
	    				currentDist = Math.sqrt((Math.pow(x-centerX,2)+(Math.pow(y-centerY,2))));
		    			if(currentDist<smallestDist)
		    			{
		    				change = true;
		    				smallestDist = currentDist;	
		    				startX = centerX;
		    				startY = centerY;

		    				taggedIndex = i;
		    			}
	    			}
	    		}
    		}
    	}
    	return change;
    }
    
    public void pause(boolean paused)
    {
    	if(GT!=null)
    	{
    		GT.pause(paused);
    		isPaused = paused;
    	}
    }
    
	public void Update(float timePassed)
	{
		if (!textVisible)
		{
			boolean spawnEnemy = false;
			
			timePassed = timePassed/1000;
			
			//TODO this slows down time when an enemy is tagged - perhaps make it feel smoother
			if(draggable)
			{
				timePassed = timePassed/4; 
			}
			
			//If true, the enemySpawner is signaling for an enemy to be spawned.
			if (enemySpawner.increment(timePassed))
			{
				spawnEnemy = true;		
			}
			


	    	updateStrength(timePassed);
			
			for(int i=0;i<numEnemies;i++)
			{
				if(enemies[i].initialized)
				{
					enemies[i].update(timePassed);
					if(i==taggedIndex)
					{
						if(draggable)
						{
							startX = enemies[i].getCenX();
							startY = enemies[i].getCenY();
						}
						if(punch)
						{
							player.hit(enemies[i],(currentX-startX)*hitEffectiveness,(currentY-startY)*hitEffectiveness);
							punch=false;
						}
					}
					if((enemies[i].x<0 ||enemies[i].x>screenWidth)&&(enemies[i].y<0||enemies[i].y>screenHeight))
					{
						enemies[i].initialized=false;
					}
				
					if(!enemies[i].alive)
					{
						//Log.d("FadingTest",String.format("%f",enemies[i].fading));
						if(enemies[i].fading==255)
						{
							for(int j=0; j<numEnemies;j++)
							{
								if(i!=j)
								{	
									float fourthSpriteWidth;
									float fourthSpriteHeight;
									if(enemies[j].alive)
									{
										fourthSpriteWidth = enemies[i].getSprite().getWidth()/4;
										fourthSpriteHeight = enemies[i].getSprite().getHeight()/4;
										if(enemies[j].isCollision(enemies[i].x+fourthSpriteWidth,enemies[i].x+3*fourthSpriteWidth,enemies[i].y+fourthSpriteHeight, enemies[i].y+3*fourthSpriteHeight))
										{
											Log.d("Succesful collision!","Succesful collision!");
											enemies[j].collision(enemies[i]);
										}
									}
								}
							}
						}
					}
					else
					{
						if (enemies[i].isCollision(treasureXPos, treasureXPos + treasure.getWidth(), treasureYPos, treasureYPos + treasure.getHeight()))
						{
							
							Log.d("AAAAAA", String.format("Enemy: left: %f; right: %f; up: %f, down: %f", enemies[i].getPosVector().x, enemies[i].getPosVector().x + enemies[i].getSprite().getWidth(), enemies[i].getPosVector().y, enemies[i].getPosVector().y + enemies[i].getSprite().getHeight()));
							Log.d("AAAAAA", String.format("Treasure: left: %f; right: %f; up: %f, down: %f", treasureXPos, + treasureXPos + treasure.getWidth(), treasureYPos, treasureYPos + treasure.getHeight()));
							Log.d("AAAAAA", String.format("screenWidth: %d; screenHeight: %d", screenWidth, screenHeight));
							text = "Game Over";
							gameOver = true;
							textVisible = true;
							
							textVisible = true;
							
							text = "Game Over";
	        		        textPaint.getTextBounds(text, 0, text.length(), textButton);
	        		        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
	        		        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
	        		        textButton.bottom += screenHeight/2;
	        		        textButton.top += screenHeight/2;
	        		        
	        		        if (player.killCount > highScore)
	        		        {
		        		        
								try 
								{
									File file = new File(getContext().getFilesDir(), GameView.saveFileName);
									FileOutputStream FOS = new FileOutputStream(file);
									FOS.write(player.killCount);
									FOS.close();
									Log.d("HIGH SCORE STUFF!!!!", "highsocre.txt successfully written");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.e("URGENT", "File Write Error: IO Exception");
								}
								
								highScoreText = String.format("NEW HIGH SCORE: %d", player.killCount);
								highScore = player.killCount;
	        		        }
	        		        else
	        		        { highScoreText = String.format("HIGH SCORE: %d", highScore); }
						}
					}
				}
				
				else
				{
					if (spawnEnemy)
					{
						enemies[i]=enemySpawner.initializeEnemy();
						spawnEnemy = false;
					}
				}
			}
		}
	}
	
	double Distance( Enemy a, Enemy b )
	{
	  return Math.sqrt( (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) );
	}
	 
	boolean isColliding( Enemy a, Enemy b )
	{
	  float r = a.radius + b.radius;
	  return r < Distance( a, b );
	}
	public void stall() 
	{
		if (!textVisible)
		{
			text = "Continue";
	        textButton.left = (int) (screenWidth/2 - textPaint.measureText(text)/2);
	        textButton.right = (int) (screenWidth/2 + textPaint.measureText(text)/2);
			textVisible = true;
			
		}
	}
	
	
}
