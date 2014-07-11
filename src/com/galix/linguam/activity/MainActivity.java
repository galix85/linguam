package com.galix.linguam.activity;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.adapter.DrawableAdapter;
import com.galix.linguam.pojo.Language;



public class MainActivity extends Activity {

	private List<Language> drawerListViewLang;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout_bckup);
		
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
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
	    	drawerLayout.closeDrawer(drawerListView);

	    }
	}


}