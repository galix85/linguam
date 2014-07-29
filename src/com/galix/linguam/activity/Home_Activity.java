package com.galix.linguam.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.adapter.DrawableAdapter;
import com.galix.linguam.pojo.Language;
import com.galix.linguam.util.Util;

public class Home_Activity extends Activity {

	private List<Language> drawerListViewLang;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_layout);
		
		//CONFIG DRAWER
		
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
        // 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true); 
        // just styling option
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawerListView.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	
	    	InputMethodManager imm = (InputMethodManager)getSystemService(LinguamApplication.getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            
	    	Language select_language = drawerListViewLang.get(position);
	    	LinguamApplication.languageDB.setSelectedLanguage(select_language.getId());
	    
	    	//Change Title
	    	android.app.ActionBar ab = getActionBar();
		    ab.setTitle(Util.getActionBarTitle());
		    ab.setSubtitle(Util.getSelectedLanguage().getSubtitle()); 
		    
	    	drawerLayout.closeDrawer(drawerListView);

	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
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


	public void to_translate_activity(View view){
		Intent i = new Intent(this, Translate_Activity.class);
		startActivity(i); 
	}
	
	public void to_play_activity(View view){

		Toast noWords = null;
		
		if (LinguamApplication.originalWordDB.getAllOriginalWords(LinguamApplication.getSelectedLanguage().getId()).size() >= 10){
			Intent i = new Intent(this, Game_Activity.class);
			startActivity(i);
		}else{
			noWords = Toast.makeText(LinguamApplication.getContext(), R.string.no_words, Toast.LENGTH_LONG);
			noWords.show();
		}
		 
	}

}
