package com.galix.linguam.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

	private InputMethodManager imm;
	private TextView tvGamingWord;
	private TextView tvTotalScore;
	private TextView tvWordLevel;
	private GameEngine gameEngine;
	private Iterator<GameData> iterWords;
	private GameData gameIter;
	
	int totalScore;
	ArrayList<Term> translateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Input Method Manager
		this.imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		// Get the message from the intent
		getIntent();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		// Init UI
		this.tvGamingWord = (TextView)findViewById(R.id.tvGamingWord);
		this.tvTotalScore = (TextView)findViewById(R.id.tvScore);
		this.tvWordLevel = (TextView)findViewById(R.id.tvLevel);
		
		//init game
		initGame();
		//Start game
		startGame();
	}
	
	private void initGame(){
		//Get instance of game engine 
		Log.v(TAG,"Init Game");
		this.gameEngine = GameEngine.getInstance();
		this.gameEngine.init();
		this.iterWords = gameEngine.getGameDataList().iterator();
	}
	
	/**
	 * Init of game
	 */
	private void startGame(){

		//Next word of game
		if (this.iterWords.hasNext()){
			this.gameIter = nextWord();
		}else if(gameEngine.getGameDataList() != null && gameEngine.getGameDataList().size() > 0){
			initGame();
			this.gameIter = nextWord();
		}

		if (gameIter != null){
			//Start the game
			run(gameIter);
		}else{
			//No more words
			Log.v(TAG,"Finish Game (No more words)");
			finishUI();
		}
	}

	private void finishUI() {
		Toast toastMeassage = Toast.makeText(LinguamApplication.getContext(), R.string.no_more_words, Toast.LENGTH_LONG * 3);
		toastMeassage.show();
		Intent i = new Intent(this, Translate_Activity.class);
		startActivity(i); 
		finish();
	}
	

	/**
	 * An individual running instance of word
	 * @param word
	 */
	private void run(GameData word){

		totalScore = LinguamApplication.statisticDB.getScore();
		
		//Setting word to ask
		tvGamingWord.setText(word.getPairWord().getOriginalWord().toUpperCase(Locale.US));
		tvTotalScore.setText(getResources().getString(R.string.score) + " " + totalScore);
		tvWordLevel.setText(getResources().getString(R.string.level) + " " + word.getPairWord().getLevel());
		
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
			btn1.setText(word.getPaddingWordList().get(0).toUpperCase(LinguamApplication.spanish_locale));
			btn2.setText(word.getPairWord().getTranslateWord().toUpperCase(LinguamApplication.spanish_locale));
			
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

			//Set text of possible answers
			btn1.setText(word.getPaddingWordList().get(0).toUpperCase(LinguamApplication.spanish_locale));
			btn2.setText(word.getPairWord().getTranslateWord().toUpperCase(LinguamApplication.spanish_locale));
			btn3.setText(word.getPaddingWordList().get(1).toUpperCase(LinguamApplication.spanish_locale));
			btn4.setText(word.getPaddingWordList().get(2).toUpperCase(LinguamApplication.spanish_locale));
	
			
		}else if (word.getPairWord().getLevel() == 5){
			setViewThirdGroup();
		}else{
			startGame();
		}

	}
	
	/**
	 * Update value of level and save to DB
	 * @param correct
	 * @param gameStat
	 */
	private void updateStats(boolean correct, GameData gameStat) {

		if (correct){
			//Update level
			LinguamApplication.translatedWordDB.updateLevel(gameStat.getPairWord().getTranslateWord(),gameStat.getPairWord().getLevel() + 1);
			//Update score
			int currentScore = LinguamApplication.CONSTANT_SCORE * gameStat.getPairWord().getLevel();
			int scoreToUpdate = totalScore + currentScore;
			LinguamApplication.statisticDB.updateScore(scoreToUpdate);
			//Correct answer
			Log.v(TAG,getResources().getString(R.string.correct) + "(+"+ currentScore +" points)");
			Toast correctToast = Toast.makeText(LinguamApplication.getContext(), getResources().getString(R.string.incorrect) + "(+"+ currentScore +" points)", Toast.LENGTH_SHORT);
			correctToast.show();
			
		}else{
			Toast incorrectToast = null;
			if (gameStat.getPairWord().getLevel() > 1) 
				//Update level
				LinguamApplication.translatedWordDB.updateLevel(gameStat.getPairWord().getTranslateWord(),gameStat.getPairWord().getLevel() - 1);
				//Update score
				int currentScore = LinguamApplication.CONSTANT_SCORE * gameStat.getPairWord().getLevel();
				int scoreToUpdate = totalScore == 0 ? 0 : totalScore - currentScore;
				LinguamApplication.statisticDB.updateScore(scoreToUpdate);
				//Incorrect answer
				Log.v(TAG,getResources().getString(R.string.incorrect)+ "(-"+ currentScore +" points)");
				incorrectToast = Toast.makeText(LinguamApplication.getContext(), getResources().getString(R.string.incorrect)+ "(-"+ currentScore +" points)", Toast.LENGTH_SHORT);
				incorrectToast.show();
		}
		
		//Delete actual UI
		destroyUI(gameStat.getPairWord().getLevel());

		//call next word
		startGame();		
	}
		
	/**
	 * Build 1st level view 
	 */
	private void setViewFirstGroup(){
		
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(130, 70, 2);
		params.setMargins(2, 0, 2, 0);
		
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
		btn1.setLayoutParams(params);
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
		btn2.setLayoutParams(params);
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
	
	/**
	 * Build 2on level view 
	 */
	private void setViewSecondGroup(){
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(130, 70, 2);
		
		params.setMargins(2, 2, 2, 2);
				
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
		btn1.setLayoutParams(params);
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
		btn2.setLayoutParams(params);
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
		btn3.setLayoutParams(params);
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
		btn4.setLayoutParams(params);
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
        setContentView(lp);
	}
	/**
	 * Build 3rth level view 
	 */
	private void setViewThirdGroup(){
		
		LinearLayout lp = (LinearLayout)findViewById(R.id.LinearLayout1);
		
		//Reset view
		LinearLayout l3past = (LinearLayout)findViewById(50);
		if(l3past != null && l3past.getChildCount() > 0) {
			lp.removeView(l3past); 
		}
		
		LinearLayout l3 = new LinearLayout(this);
		//Set params l3 
		l3.setId(50);
		l3.setGravity(Gravity.BOTTOM);
		l3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l3.setOrientation(LinearLayout.HORIZONTAL);
		
		final EditText etCorrectAnswer = new EditText(this) ;
		etCorrectAnswer.setLayoutParams(new LinearLayout.LayoutParams(330,70)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
		etCorrectAnswer.setGravity(Gravity.CENTER_HORIZONTAL);
		etCorrectAnswer.setTextColor(getResources().getColor(R.color.Black));
		etCorrectAnswer.setTextSize(20);
		etCorrectAnswer.setHint(R.string.text_level5_hint);
	
		
		final Button btn1 = new Button(this);
		btn1.setBackgroundResource(R.drawable.buttonshape);
		btn1.setTextColor(getResources().getColor(R.color.White));
		btn1.setTextSize(20);
		btn1.setText(getResources().getString(R.string.check_button));
		btn1.setLayoutParams(new LinearLayout.LayoutParams(100,70));
        btn1.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(etCorrectAnswer.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
        
        l3.addView(etCorrectAnswer);
        l3.addView(btn1);
        lp.addView(l3);
        setContentView(lp);
		/*
		//Reset view
		LinearLayout l3past = (LinearLayout)findViewById(50);
		if(l3past != null && l3past.getChildCount() > 0) {
			lp.removeView(l3past); 
		}
		
		LinearLayout l4past = (LinearLayout)findViewById(51);
		if(l4past != null && l4past.getChildCount() > 0) {
			lp.removeView(l4past); 
		}

		LinearLayout l3 = new LinearLayout(this);
		//Set params l3 
		l3.setId(50);
		l3.setGravity(Gravity.BOTTOM);
		l3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l3.setOrientation(LinearLayout.HORIZONTAL);
		
		final EditText etCorrectAnswer = new EditText(this) ;
		etCorrectAnswer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
		etCorrectAnswer.setGravity(Gravity.CENTER_HORIZONTAL);
		etCorrectAnswer.setPadding(0, 32, 0, 0);
		etCorrectAnswer.setTextColor(getResources().getColor(R.color.Black));
		etCorrectAnswer.setTextSize(20);
		etCorrectAnswer.setHint(R.string.text_level5_hint);
	
		
		//Set params l2 
		LinearLayout l4 = new LinearLayout(this);
		l4.setId(51);
		l4.setGravity(Gravity.BOTTOM);
		l4.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l4.setOrientation(LinearLayout.HORIZONTAL);
		
		final Button btn1 = new Button(this);
		btn1.setBackgroundResource(R.drawable.buttonshape);
		btn1.setTextColor(getResources().getColor(R.color.White));
		btn1.setTextSize(18);
		btn1.setText(getResources().getString(R.string.check_button));
		btn1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,50,2));
        btn1.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	boolean correct = gameEngine.checkAnswer(etCorrectAnswer.getText().toString(), gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
        
        l3.addView(etCorrectAnswer);
        l4.addView(btn1);
        lp.addView(l3);
        lp.addView(l4);
        setContentView(lp);*/
	}

	private GameData nextWord(){
		if (iterWords.hasNext()) {
			return iterWords.next();
		}else{
			return null;
		}
	}
	
	
	private void destroyUI(int level) {

		// Setting views for each group of level
		if (level == 1 || level == 2) {
			// Set view of group1
			resetViewFirstGroup();
		} else if (level == 3 || level == 4) {
			// Set view of group2
			resetViewSecondGroup();
		} else if (level == 5) {
			resetViewThirdGroup();
		}
	}
	
	private void resetViewFirstGroup() {
		
		LinearLayout lp = (LinearLayout) findViewById(R.id.LinearLayout1);

		// Reset view
		LinearLayout l1past = (LinearLayout) findViewById(10);
		if (l1past != null && l1past.getChildCount() > 0) {
			lp.removeView(l1past);
		}
	}

	private void resetViewSecondGroup() {
		
		LinearLayout lp = (LinearLayout) findViewById(R.id.LinearLayout1);

		// Reset view
		LinearLayout l1past = (LinearLayout) findViewById(11);
		if (l1past != null && l1past.getChildCount() > 0) {
			lp.removeView(l1past);
		}
		LinearLayout l2past = (LinearLayout) findViewById(12);
		if (l2past != null && l2past.getChildCount() > 0) {
			lp.removeView(l2past);
		}
	}

	private void resetViewThirdGroup() {
		
		LinearLayout lp = (LinearLayout) findViewById(R.id.LinearLayout1);

		// Reset view
		LinearLayout l3past = (LinearLayout) findViewById(50);
		if (l3past != null && l3past.getChildCount() > 0) {
			lp.removeView(l3past);
		}

		LinearLayout l4past = (LinearLayout) findViewById(51);
		if (l4past != null && l4past.getChildCount() > 0) {
			lp.removeView(l4past);
		}
	}

}