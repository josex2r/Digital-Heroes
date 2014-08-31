package com.josex2r.digitalheroes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.josex2r.digitalheroes.controllers.NewPostAlarm;
import com.josex2r.digitalheroes.controllers.ScrollableMenuAdapter;
import com.josex2r.digitalheroes.fragments.AllPostsFragment;
import com.josex2r.digitalheroes.fragments.AuthorPostsFragment;
import com.josex2r.digitalheroes.fragments.CategoryPostsFragment;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.ScrollableMenu;
import com.josex2r.digitalheroes.utils.TrackerName;
import com.josex2r.digitalheroes.utils.Utils;


public class MainActivity extends FragmentActivity {
	//New post alarm
	private static NewPostAlarm newPostAlarm;
	//ViewPager
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private List<Fragment> fragmentList;
    private List<String> fragmentTitles;
    
    //ActionBar
    private ActionBar actionBar;
    
    //Scrollable menu
    private int mDrawerLastPosition = 1;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    //Blog
    private static Blog blog;
    //Analytics
    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    private final static String TRACKER_ID="UA-49401782-1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionBar = getActionBar();
        
		//Init view pager
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.getChildAt(0).setBackgroundResource(R.color.red);
		
		//Create fragments
		fragmentList=new ArrayList<Fragment>();
		fragmentList.add(new CategoryPostsFragment());
		fragmentList.add(new AllPostsFragment());
		fragmentList.add(new AuthorPostsFragment());
		
		//Create fragment titles
		fragmentTitles=new ArrayList<String>();
		fragmentTitles.add(getString(R.string.title_section2));
		fragmentTitles.add(getString(R.string.title_section1));
		fragmentTitles.add(getString(R.string.title_section3));
		
		//Add fragments and titles to the adapter
		mSectionsPagerAdapter.addFragments(fragmentList, fragmentTitles);
		
		//Init drawer menu
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.slidingMenu);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        //Drawer menu items
        List<ScrollableMenu> dataList= new ArrayList<ScrollableMenu>();
        dataList.add(new ScrollableMenu(getString(R.string.title_section1), getString(R.string.icon_inbox)));
        dataList.add(new ScrollableMenu(getString(R.string.title_section2), getString(R.string.icon_bookmark)));
        dataList.add(new ScrollableMenu(getString(R.string.title_section3), getString(R.string.icon_user)));
        dataList.add(new ScrollableMenu(getString(R.string.title_section4), getString(R.string.icon_star)));
        dataList.add(new ScrollableMenu(getString(R.string.title_section5), getString(R.string.icon_mail_reply_all)));
        
        //Drawer menu header
        View header=getLayoutInflater().inflate(R.layout.drawer_list_header, null);
        mDrawerList.addHeaderView(header, new Object(), false);
        mDrawerList.setAdapter(new ScrollableMenuAdapter(this, R.layout.drawer_list_item, dataList));
        mDrawerList.setOnItemClickListener(new ScrollableMenuClickListener());
        mDrawerList.setItemChecked(mDrawerLastPosition, true);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
                ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
        
	}
	
	/*
	 * GET NAVIGATION ITEMS TO CHANGE TITLES INSIDE FRAGMENTS
	 */
	public SectionsPagerAdapter getSectionsPageAdapter(){
		return mSectionsPagerAdapter;
	}
	
	public ViewPager getViewPager(){
		return mViewPager;
	}
	
	public ListView getDrawerList(){
		return mDrawerList;
	}
	
	/*
	 * CREATES MENU
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
		
		//return true;
	}
	
	/*
	 * RETURNS WHEN LOADER ACTIVITY FINISH
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if( requestCode==Blog.REQUEST_LOAD ){
			switch (resultCode) {
				case Blog.REQUEST_LOADED:
					break;
				case Blog.REQUEST_FAILED:
					Toast.makeText(getApplicationContext(), "No se han podido cargar las noticias", Toast.LENGTH_LONG).show();
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*
	 * SELECT ITEM FROM MENU, ONLY SETTINGS AVAILABLE
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(i, 0);
        
        return false;
    }
    
	/*
	 * SELECT ITEM FROM DRAWER SCROLLABLE MENU
	 */
    public class ScrollableMenuClickListener implements ListView.OnItemClickListener {
    	/*
    	 * CLICK ON ITEM
    	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
    	 */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	switch(position){
        		//SHOW ALL POSTS
        		case 1:
        			if( !blog.getActiveFilter().equals(Blog.FILTER_ALL) ){
	        			blog.setActiveFilter( Blog.FILTER_ALL );
	        			blog.setCurrentPage(1);
        			}
        			mSectionsPagerAdapter.changeTitle(1, getString(R.string.title_section1));
        			mViewPager.setCurrentItem(1);
        			actionBar.setTitle(getString(R.string.app_name));
        			break;
        		//SCROLL TO CATEGORIES
        		case 2:
        			mViewPager.setCurrentItem(0);
        			break;
        		//SCROLL TO AUTHORS
        		case 3:
        			mViewPager.setCurrentItem(2);
        			break;
        		//SHOW FAVOURITES POSTS
        		case 4:
        			if( !blog.getActiveFilter().equals(Blog.FILTER_FAVOURITES) ){
	        			blog.setActiveFilter( Blog.FILTER_FAVOURITES );
	        			blog.setCurrentPage(1);
        			}
        			mSectionsPagerAdapter.changeTitle(1, getString(R.string.title_section4));
        			actionBar.setTitle(getString(R.string.title_section4));
        			mViewPager.setCurrentItem(1);
        			break;
        		//LAUCH DEFAULT EMAIL APP
        		case 5:
        			Intent intent = new Intent(Intent.ACTION_SEND);
        			intent.setType("text/message");
        			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"codex2r@gmail.com"});
        			intent.putExtra(Intent.EXTRA_SUBJECT, "");
        			intent.putExtra(Intent.EXTRA_TEXT, "");
        			Intent mailer = Intent.createChooser(intent, null);
        			startActivity(mailer);
        			break;
        	}
        	//Check item except "contact"
        	if(position!=5){
        		mDrawerList.setItemChecked(position, true);
        		mDrawerLastPosition = position;
        	}else{
        		mDrawerList.setItemChecked(mDrawerLastPosition, true);
        	}
        	//Close the drawer
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
    
    /*
     * PAUSE APP
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	Log.d("MyApp","PAUSE");
    	super.onPause();
    }
    
    /*
     * RESUMEN APP
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
    	//Initialize blog instance if not exists
    	if(MainActivity.blog==null){
    		MainActivity.blog = Blog.getInstance();
    		MainActivity.blog.setContext(this.getApplicationContext());
    		MainActivity.blog.loadFavouritesFromDB();
    	}
    	//Set "All posts" selected
    	mViewPager.setCurrentItem(1);
    	//Reset alarm on app start
    	if( MainActivity.newPostAlarm!=null ){
    		MainActivity.newPostAlarm.cancelAlarm(this);
    	}else{
    		MainActivity.newPostAlarm = new NewPostAlarm();
    	}
		MainActivity.newPostAlarm.setAlarm(this);
    	//Google Analytics track
    	if( Utils.checkPlayServices(this) ){
    		Tracker t = ((MainActivity) this).getTracker(TrackerName.APP_TRACKER);
    		t.setScreenName("capture");
    		t.send(new HitBuilders.AppViewBuilder().build());
    	}
    	super.onResume();
    }
    
    /*
     * GOOGLE ANALYTICS TRACK
     */
    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
          GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
          Tracker t=analytics.newTracker(TRACKER_ID);
          mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    /*
     * CUSTOM PAGE ADAPTER
     */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
