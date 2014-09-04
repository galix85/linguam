package com.galix.linguam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.adapter.DrawableAdapter;
import com.galix.linguam.gameengine.GameEngine;
import com.galix.linguam.pojo.GameData;
import com.galix.linguam.pojo.Language;
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
	
	private List<Language> drawerListViewLang;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawableAdapter adapter;
    
    //Top buttons
	private Button btn_top1;
	private Button btn_top2;
	private Button btn_top3;
	private Button btn_top4;
	private Button btn_top5;
	
	private HashMap<Integer, Integer> levelButtonMapping;

	private int level;
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    	InputMethodManager imm = (InputMethodManager)getSystemService(LinguamApplication.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	    	
	    	Language select_language = drawerListViewLang.get(position);
	    	LinguamApplication.languageDB.setSelectedLanguage(select_language.getId());
	    	LinguamApplication.setSelectedLanguage(select_language);
	    	
	    	//Change Title
	    	android.app.ActionBar ab = getActionBar();
		    ab.setTitle(Util.getActionBarTitle());
		    ab.setSubtitle(Util.getSelectedLanguage().getSubtitle()); 
		    
		    //Delete current UI
			destroyUI(gameIter.getPairWord().getLevel());
	    	//init game
			initGame();
			//Start game
			startGame();
			//Toast.makeText(LinguamApplication.getContext(),"Select language: "+ select_language.getTitle(), Toast.LENGTH_LONG).show();
	    	drawerLayout.closeDrawer(drawerListView);
	    }
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
    }

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		 // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Input Method Manager
		this.imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		// Get the message from the intent
		getIntent();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		// get list items from db
		drawerListViewLang = LinguamApplication.languageDB.getActiveLanguage();
		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);
		adapter = new DrawableAdapter(LinguamApplication.getContext(), drawerListViewLang);
		 // Set the adapter for the list view
		drawerListView.setAdapter(adapter);
		// 2. App Icon 
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// 2.1 create ActionBarDrawerToggle
		actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );

        // 2.2 Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //Change Title
    	android.app.ActionBar ab = getActionBar();
	    ab.setTitle(Util.getActionBarTitle());
	    ab.setSubtitle(Util.getSelectedLanguage().getSubtitle()); 
        // 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true); 
        // just styling option
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawerListView.setOnItemClickListener(new DrawerItemClickListener());
		
		// Init UI
		this.tvGamingWord = (TextView)findViewById(R.id.tvGamingWord);
		this.tvTotalScore = (TextView)findViewById(R.id.tvScore);
		this.tvWordLevel = (TextView)findViewById(R.id.tvLevel);
		
		//init top buttons
		//Get id of buttons to set texts
		this.btn_top1 = (Button)findViewById(R.id.btn_level1_top);
		this.btn_top2 = (Button)findViewById(R.id.btn_level2_top);
		this.btn_top3 = (Button)findViewById(R.id.btn_level3_top);
		this.btn_top4 = (Button)findViewById(R.id.btn_level4_top);
		this.btn_top5 = (Button)findViewById(R.id.btn_level5_top);
		
		setMapButtonToLevel();	
		
		btn_top1.setOnClickListener(new OnClickListener() {

		    public void onClick(View button) {
		        //Set the button's appearance
		    	btn_top3.setSelected(false);
		    	btn_top2.setSelected(false);
		    	btn_top4.setSelected(false);
		    	btn_top5.setSelected(false);
		        button.setSelected(true);
		        
		        if (button.isSelected()) {
		        	destroyUI(getCurrentLevel());
		        	run(listGoToLevel(1));
		        }
		    }
		});
		
		btn_top2.setOnClickListener(new OnClickListener() {

		    public void onClick(View button) {
		        //Set the button's appearance
		    	btn_top1.setSelected(false);
		    	btn_top3.setSelected(false);
		    	btn_top4.setSelected(false);
		    	btn_top5.setSelected(false);
		    	button.setSelected(true);
		        
		        if (button.isSelected()) {
		        	destroyUI(getCurrentLevel());
		        	run(listGoToLevel(2));
		        }
		    }
		});
		
		
		btn_top3.setOnClickListener(new OnClickListener() {

		    public void onClick(View button) {
		        //Set the button's appearance
		    	btn_top1.setSelected(false);
		    	btn_top2.setSelected(false);
		    	btn_top4.setSelected(false);
		    	btn_top5.setSelected(false);
		    	button.setSelected(true);
		        
		        if (button.isSelected()) {
		        	destroyUI(getCurrentLevel());
		        	run(listGoToLevel(3));
		        }
		    }
		});
		
		btn_top4.setOnClickListener(new OnClickListener() {

		    public void onClick(View button) {
		        //Set the button's appearance
		    	btn_top1.setSelected(false);
		    	btn_top2.setSelected(false);
		    	btn_top3.setSelected(false);
		    	btn_top5.setSelected(false);
		    	button.setSelected(true);
		        
		        if (button.isSelected()) {
		        	destroyUI(getCurrentLevel());
		        	run(listGoToLevel(4));
		        }
		    }
		});
		
		btn_top5.setOnClickListener(new OnClickListener() {

		    public void onClick(View button) {
		        //Set the button's appearance
		    	btn_top1.setSelected(false);
		    	btn_top2.setSelected(false);
		    	btn_top3.setSelected(false);
		    	btn_top4.setSelected(false);
		    	button.setSelected(true);
		        
		        if (button.isSelected()) {
		        	destroyUI(getCurrentLevel());
		        	run(listGoToLevel(5));
		        }
		    }
		});
		
		//init game
		initGame();
		//Start game
		startGame();
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
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
			//Mark label of current button
			setButtonToLevel(gameIter.getPairWord().getLevel());
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

		totalScore = LinguamApplication.statisticDB.getScore(LinguamApplication.getSelectedLanguage().getId());
		
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
			LinguamApplication.translatedWordDB.updateLevel(gameStat.getPairWord().getTranslateWord(),gameStat.getPairWord().getLevel() + 1,LinguamApplication.getSelectedLanguage().getId());
			//Update score
			int currentScore = LinguamApplication.CONSTANT_SCORE * gameStat.getPairWord().getLevel();
			int scoreToUpdate = totalScore + currentScore;
			LinguamApplication.statisticDB.updateScore(scoreToUpdate,LinguamApplication.getSelectedLanguage().getId());
			//Correct answer
			Log.v(TAG,getResources().getString(R.string.correct) + "(+"+ currentScore +" points)");
			
			Toast correctToast = Toast.makeText(LinguamApplication.getContext(), getResources().getString(R.string.correct) + "(+"+ currentScore +" points)", Toast.LENGTH_SHORT);
			View view = correctToast.getView();
			view.setBackgroundColor(getResources().getColor(R.color.GreenKuler));
			correctToast.show();
			
		}else{
			Toast incorrectToast = null;
			if (gameStat.getPairWord().getLevel() > 1) 
				//Update level
				LinguamApplication.translatedWordDB.updateLevel(gameStat.getPairWord().getTranslateWord(),gameStat.getPairWord().getLevel() - 1,LinguamApplication.getSelectedLanguage().getId());
				//Update score
				int currentScore = LinguamApplication.CONSTANT_SCORE * gameStat.getPairWord().getLevel();
				int scoreToUpdate = totalScore == 0 ? 0 : totalScore - currentScore;
				LinguamApplication.statisticDB.updateScore(scoreToUpdate,LinguamApplication.getSelectedLanguage().getId());
				//Incorrect answer
				Log.v(TAG,getResources().getString(R.string.incorrect)+ "(-"+ currentScore +" points)");
				
				incorrectToast = Toast.makeText(LinguamApplication.getContext(), getResources().getString(R.string.incorrect)+ "(-"+ currentScore +" points).\n" +
						getResources().getString(R.string.correct_asnwer) +" "+ gameStat.getPairWord().getTranslateWord().toUpperCase()
						, Toast.LENGTH_SHORT);
				View view = incorrectToast.getView();
				view.setBackgroundColor(getResources().getColor(R.color.RedKuler));
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
		
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(130, 150, 2);
		params.setMargins(2, 0, 2, 0);
		
		DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);
		
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
        setContentView(dl);
	}
	
	/**
	 * Build 2on level view 
	 */
	private void setViewSecondGroup(){
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 170, 2);
		params.setMargins(2, 2, 2, 2);
				
		DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);
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
        setContentView(dl);
	}
	/**
	 * Build 3rth level view 
	 */
	private void setViewThirdGroup(){
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,150);
		params.setMargins(2, 2, 2, 2);
		
		DrawerLayout dl = (DrawerLayout)findViewById(R.id.drawer_layout);
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
		l3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 0));
		l3.setOrientation(LinearLayout.HORIZONTAL);

		final EditText etCorrectAnswer = new EditText(this) ;
		etCorrectAnswer.setLayoutParams(new LinearLayout.LayoutParams((int) (Util.getWidth(LinguamApplication.getContext()) * 0.75),LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
		etCorrectAnswer.setGravity(Gravity.CENTER_HORIZONTAL);
		etCorrectAnswer.setTextColor(getResources().getColor(R.color.Black));
		etCorrectAnswer.setTextSize(20);
		etCorrectAnswer.setHint(R.string.text_level5_hint);
		etCorrectAnswer.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	
		
		final Button btn1 = new Button(this);
		btn1.setBackgroundResource(R.drawable.buttonshape);
		btn1.setTextColor(getResources().getColor(R.color.White));
		btn1.setTextSize(20);
		btn1.setText(getResources().getString(R.string.check_button));
		btn1.setLayoutParams(params);
        btn1.setOnClickListener(new OnClickListener() {         
	        @Override
	        public void onClick(View v) {
	        	// Hide keyboard
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	        	//Trimm trailing withe spaces
	        	String trimmedText = etCorrectAnswer.getText().toString().replaceAll("\\s+$", "");
	        	boolean correct = gameEngine.checkAnswer(trimmedText, gameIter.getPairWord().getOriginalWord());
        		updateStats(correct,gameIter);
	            }           
	    });
        
        l3.addView(etCorrectAnswer);
        l3.addView(btn1);
        lp.addView(l3);
        setContentView(dl);

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

	private synchronized GameData listGoToLevel(int level) {
		
		initGame();
		GameData word;
		while(iterWords.hasNext()){
			 word = nextWord();
			 if (word.getPairWord().getLevel() == level){
				 setButtonToLevel(word.getPairWord().getLevel());
				 return word;
			 }else if (word.getPairWord().getLevel() > level){
				 setButtonToLevel(word.getPairWord().getLevel());
				 return word;
			 }
		}
		//If there are no results, return whole stack
		initGame();
		word = nextWord();
		setButtonToLevel(word.getPairWord().getLevel());
		return word;
		
	}
	
	private void setMapButtonToLevel() {
		levelButtonMapping = new HashMap<Integer, Integer>();
		levelButtonMapping.put(1, R.id.btn_level1_top);
		levelButtonMapping.put(2, R.id.btn_level2_top);	
		levelButtonMapping.put(3, R.id.btn_level3_top);	
		levelButtonMapping.put(4, R.id.btn_level4_top);	
		levelButtonMapping.put(5, R.id.btn_level5_top);	
	}	
	
	private void setButtonToLevel(int level) {
		
		this.level = level;
		for (Integer key : levelButtonMapping.keySet()) {
			if (key != level){
				findViewById(levelButtonMapping.get(key)).setSelected(false);
			}
		}
		
		findViewById(levelButtonMapping.get(level)).setSelected(true);
	}
	
	private int getCurrentLevel() {
		return this.level;
	}
	
}