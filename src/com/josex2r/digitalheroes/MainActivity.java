package com.josex2r.digitalheroes;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.fragments.AllPostsFragment;
import com.josex2r.digitalheroes.fragments.AuthorPostsFragment;
import com.josex2r.digitalheroes.fragments.CategoryPostsFragment;
import com.josex2r.digitalheroes.model.Blog;


public class MainActivity extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private List<Fragment> fragmentList;
    private List<String> fragmentTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog dialog;
    private static Blog blog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Create blog
		this.blog=new Blog("http://www.gobalo.es/blog/feed/");

		//Init page slider
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		//Create fragments
		fragmentList=new ArrayList<Fragment>();
		fragmentList.add(new AllPostsFragment());
		fragmentList.add(new CategoryPostsFragment());
		fragmentList.add(new AuthorPostsFragment());
		//Create titles
		fragmentTitles=new ArrayList<String>();
		fragmentTitles.add(getString(R.string.title_section1));
		fragmentTitles.add(getString(R.string.title_section2));
		fragmentTitles.add(getString(R.string.title_section3));
		//Add fragments and titles to the slider
		mSectionsPagerAdapter.addFragments(fragmentList, fragmentTitles);
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.slidingMenu);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, fragmentTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

	}
	
	public Blog getBlog(){
		return this.blog;
	}
	
	public void showLoading(){
		if(dialog==null)
			dialog=ProgressDialog.show(this, "", getString(R.string.loading), true);
	}
	
	public void hideLoading(){
		if(dialog!=null)
			dialog.dismiss();
	}
	
	public MainActivity getMainActivity(){
		return this;
	}
	
	public SectionsPagerAdapter getSectionsPageAdapter(){
		return mSectionsPagerAdapter;
	}
	
	public ViewPager getViewPager(){
		return mViewPager;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
	
	/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	Log.d("MyApp",Integer.toString(position));
        	List<String> fragmentTitles=new ArrayList<String>();
    		fragmentTitles.add(getString(R.string.title_section1));
    		fragmentTitles.add(getString(R.string.title_section2));
    		fragmentTitles.add(getString(R.string.title_section3));
    		
        	mDrawerList.setItemChecked(position, true);
            //setTitle(fragmentTitles.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		private static final int NUM_PAGES=3;
		private List<Fragment> fragmentList = new ArrayList<Fragment>();
	    private List<String> tabTitleList = new ArrayList<String>();
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public void changeTitle(int position, String title){
			tabTitleList.set(position, title);
	        notifyDataSetChanged();
		}
		
		public void swapFragment(int position, Fragment fragment, String title) {
	        fragmentList.set(position, fragment);
	        tabTitleList.set(position, title);
	        notifyDataSetChanged();
	    }
		
		public void addFragments(List<Fragment> fragments, List<String> titles) {
	        fragmentList.clear();
	        tabTitleList.clear();
	        fragmentList.addAll(fragments);
	        tabTitleList.addAll(titles);
	        notifyDataSetChanged();
	    }

		@Override
	    public Fragment getItem(int item) {
	        if (item >= fragmentList.size()) {
	            return null;
	        }
	        return fragmentList.get(item);
	    }

		@Override
		public int getCount() {
	        return fragmentList.size();
	    }

		@Override
	    public CharSequence getPageTitle(int position) {
	        return tabTitleList.get(position);
	    }
		
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		
	}

	
	
	

}
