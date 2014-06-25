package com.josex2r.digitalheroes;

import com.josex2r.digitalheroes.model.Blog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.activity_settings);
		
		PreferenceManager.getDefaultSharedPreferences(getBaseContext()).registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
		if( key.equals(Blog.PREFS_NOTIFICATIONS) ){
			SharedPreferences prefs = this.getApplicationContext().getSharedPreferences(Blog.PREFS_NAMESPACE, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(Blog.PREFS_NOTIFICATIONS, sharedPreferences.getBoolean(Blog.PREFS_NOTIFICATIONS, true));
			editor.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.preferencias, menu);
		return true;
	}

}
