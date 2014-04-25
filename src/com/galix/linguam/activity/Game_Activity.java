package com.galix.linguam.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.gameengine.GameEngine;
import com.galix.linguam.pojo.GameData;
import com.galix.linguam.pojo.Term;
import com.galix.linguam.util.Util;

public class Game_Activity extends Activity {
	
	private static final String TAG = "Linguam: Game Activity";

	private TextView tvGamingWord;
	private GameEngine gameEngine;
	private Iterator<GameData> iterWords;
	private GameData gameIter;
	
	ArrayList<Term> translateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Get the message from the intent
		getIntent();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		// Init UI
		this.tvGamingWord = (TextView)findViewById(R.id.tvGamingWord);
		
		//Get instance of game engine 
		this.gameEngine = GameEngine.getInstance();
		this.gameEngine.init();
		this.iterWords = gameEngine.getGameDataList().iterator();
		
		//Start game
		startGame();
	}
	
	private void startGame(){
		
		//Next word of game
		this.gameIter = nextWord();
		
		if (gameIter != null){
			//Start the game
			run(gameIter);
		}else{
			//No more words
			Toast endMessage = Toast.makeText(LinguamApplication.getContext(), "END", Toast.LENGTH_LONG * 2);
			endMessage.show();
		}
	}

	private void run(GameData word){
		

		//Setting word to ask
		tvGamingWord.setText(word.getPairWord().getOriginalWord().toUpperCase(Locale.US));
		
		//Setting views for each group of level
		if (word.getPairWord().getLevel() == 1 || word.getPairWord().getLevel() == 2){
			//Set view of group1
			setViewFirstGroup();
			
			//Get id of buttons. (Random)
			int firstRandomNum = Util.randomNumber(20, 21);
			int secondRandomNum = 0;
			if (firstRandomNum == 20){
				secondRandomNum = 21;
			}else{
				secondRandomNum = 20;
			}
			
			Button btn1 = (Button)findViewById(firstRandomNum);
			Button btn2 = (Button)findViewById(secondRandomNum);
			//
			btn1.setText(word.getPaddingWordList().get(0).toUpperCase(Locale.ITALIAN));
			btn2.setText(word.getPairWord().getTranslateWord().toUpperCase(Locale.ITALIAN));
			
		}else if (word.getPairWord().getLevel() == 3 || word.getPairWord().getLevel() == 4){
	
			//Set view of group2
			setViewSecondGroup();
					
			//Get id of buttons. (Random)
			ArrayList<Integer> numbers = Util.generateRandomNumber(22, 25);
			
			//Get id of buttons to set texts
			Button btn1 = (Button)findViewById(numbers.get(0));
			Button btn2 = (Button)findViewById(numbers.get(1));
			Button btn3 = (Button)findViewById(numbers.get(2));
			Button btn4 = (Button)findViewById(numbers.get(3));

			//Set text of possible ans
			btn1.setText(word.getPaddingWordList().get(0).toUpperCase(Locale.ITALIAN));
			btn2.setText(word.getPairWord().getTranslateWord().toUpperCase(Locale.ITALIAN));
			btn3.setText(word.getPaddingWordList().get(1).toUpperCase(Locale.ITALIAN));
			btn4.setText(word.getPaddingWordList().get(2).toUpperCase(Locale.ITALIAN));
	
			
		}else{
			setViewThirdGroup();
		}
			
		
	}
	

	
	private GameData nextWord(){
		if (iterWords.hasNext()) {
			return iterWords.next();
		}else{
			return null;
		}
	}
	

	private void updateStats(boolean correct, GameData gameStat) {
		
		if (correct){
			//TODO ++ update level
			//Update stat info
		}else{
			//TODO -- update level unless it's already 1
		}
		
		//call next word
		startGame();		
	}
		
	private void setViewFirstGroup(){
		
		LinearLayout lp = (LinearLayout)findViewById(R.id.LinearLayout1);
		
		//Reset view
		LinearLayout l1past = (LinearLayout)findViewById(10);
		if(l1past != null && l1past.getChildCount() > 0) {
			lp.removeView(l1past); 
		}

		LinearLayout l1 = new LinearLayout(this);
		//Set params l1 
		l1.setId(10);
		l1.setGravity(Gravity.BOTTOM);
		l1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l1.setOrientation(LinearLayout.HORIZONTAL);
		

		//Set button1
		final Button btn1 = new Button(this);
		btn1.setId(20);
		btn1.setBackgroundResource(R.drawable.buttonshape);
		btn1.setTextColor(getResources().getColor(R.color.White));
		btn1.setTextSize(18);
		btn1.setLayoutParams(new LinearLayout.LayoutParams(158, 80, 2));
        btn1.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        		boolean correct = gameEngine.checkAnswer(btn1.getText().toString(), gameIter.getPairWord().getOriginalWord());
	        		updateStats(correct,gameIter);
	            }           
	    });
        
        //Set button2
        final Button btn2 = new Button(this);
		btn2.setId(21);
        btn2.setBackgroundResource(R.drawable.buttonshape);
		btn2.setTextColor(getResources().getColor(R.color.White));
		btn2.setTextSize(18);
		btn2.setLayoutParams(new LinearLayout.LayoutParams(130, 80, 2));
        btn2.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        		boolean correct = gameEngine.checkAnswer(btn2.getText().toString(), gameIter.getPairWord().getOriginalWord());
	        		updateStats(correct,gameIter);
	            }           
	    });
		
        //Add to parent view
        l1.addView(btn1);
        l1.addView(btn2);
        lp.addView(l1);
        setContentView(lp);
	}
	

	private void setViewSecondGroup(){

		LinearLayout lp = (LinearLayout)findViewById(R.id.LinearLayout1);
		
		LinearLayout l1 = new LinearLayout(this);
		LinearLayout l2 = new LinearLayout(this);
		
		//Reset view
		LinearLayout l1past = (LinearLayout)findViewById(11);
		if(l1past != null && l1past.getChildCount() > 0) {
			lp.removeView(l1past); 
		}
		LinearLayout l2past = (LinearLayout)findViewById(12);
		if(l2past != null && l2past.getChildCount() > 0) {
			lp.removeView(l2past); 
		}

		//Set params l1 
		l1.setId(11);
		l1.setGravity(Gravity.BOTTOM);
		l1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l1.setOrientation(LinearLayout.HORIZONTAL);
		
		final Button btn1 = new Button(this);
		btn1.setId(22);
		btn1.setBackgroundResource(R.drawable.buttonshape);
		btn1.setTextColor(getResources().getColor(R.color.White));
		btn1.setTextSize(18);
		btn1.setLayoutParams(new LinearLayout.LayoutParams(158, 80, 2));
        btn1.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(btn1.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
        
        
        final Button btn2 = new Button(this);
		btn2.setId(23);
        btn2.setBackgroundResource(R.drawable.buttonshape);
		btn2.setTextColor(getResources().getColor(R.color.White));
		btn2.setTextSize(18);
		btn2.setLayoutParams(new LinearLayout.LayoutParams(130, 80, 2));
        btn2.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(btn2.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });

        //Set params l2 
		l2.setId(12);
		l2.setGravity(Gravity.BOTTOM);
		l2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l2.setOrientation(LinearLayout.HORIZONTAL);
		
		final Button btn3 = new Button(this);
		btn3.setId(24);
		btn3.setBackgroundResource(R.drawable.buttonshape);
		btn3.setTextColor(getResources().getColor(R.color.White));
		btn3.setTextSize(18);
		btn3.setLayoutParams(new LinearLayout.LayoutParams(158, 80, 2));
		btn3.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(btn3.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
        
		final Button btn4 = new Button(this);
		btn4.setId(25);
		btn4.setBackgroundResource(R.drawable.buttonshape);
		btn4.setTextColor(getResources().getColor(R.color.White));
		btn4.setTextSize(18);
		btn4.setLayoutParams(new LinearLayout.LayoutParams(130, 80, 2));
		btn4.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(btn4.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
		
        
        l1.addView(btn1);
        l1.addView(btn2);
        l2.addView(btn3);
        l2.addView(btn4);
        lp.addView(l1);
        lp.addView(l2);
		
	}
	
	private void setViewThirdGroup(){
		LinearLayout l1 = new LinearLayout(this);
	}

}