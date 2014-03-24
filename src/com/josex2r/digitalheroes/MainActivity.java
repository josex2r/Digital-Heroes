package com.josex2r.digitalheroes;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.controllers.DrawerAdapter;
import com.josex2r.digitalheroes.fragments.AllPostsFragment;
import com.josex2r.digitalheroes.fragments.AuthorPostsFragment;
import com.josex2r.digitalheroes.fragments.CategoryPostsFragment;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.DrawerItem;


public class MainActivity extends FragmentActivity {
	//ViewPager
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private List<Fragment> fragmentList;
    private List<String> fragmentTitles;
    
    //Scrollable menu
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    //Loading window
    private ProgressDialog dialog;
    //Blog
    private static Blog blog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//ActionBar ab=getActionBar(); 
        //ab.setBackgroundDrawable( new ColorDrawable(Color.parseColor("#fefefe")) );
        
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
			//Create titles
			fragmentTitles=new ArrayList<String>();
			fragmentTitles.add(getString(R.string.title_section2));
			fragmentTitles.add(getString(R.string.title_section1));
			fragmentTitles.add(getString(R.string.title_section3));
			//Add fragments and titles to the slider
			mSectionsPagerAdapter.addFragments(fragmentList, fragmentTitles);
		
		//Init scrollable menu
			PagerTabStrip pts=(PagerTabStrip)findViewById(R.id.pager_title_strip);
			
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.slidingMenu);
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        
	        List<DrawerItem> dataList= new ArrayList<DrawerItem>();
	        dataList.add(new DrawerItem(getString(R.string.title_section1), getString(R.string.icon_inbox)));
            dataList.add(new DrawerItem(getString(R.string.title_section2), getString(R.string.icon_bookmark)));
            dataList.add(new DrawerItem(getString(R.string.title_section3), getString(R.string.icon_user)));
	        
	        mDrawerList.setAdapter(new DrawerAdapter(this, R.layout.drawer_list_item, dataList));
	        View header=getLayoutInflater().inflate(R.layout.drawer_list_header, null);
	        mDrawerList.addHeaderView(header, new Object(), false);
	        

	        
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	        mDrawerList.setItemChecked(1, true);
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
		return MainActivity.blog;
	}
	
	public void showLoading(){
		if(dialog==null)
			dialog=ProgressDialog.show(this, "", getString(R.string.loading), true);
	}
	
	public void hideLoading(){
		if(dialog!=null){
			dialog.dismiss();
			dialog=null;
		}
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
	
	public ListView getDrawerList(){
		return mDrawerList;
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
        //return super.onOptionsItemSelected(item);
        return false;
    }
    
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
    	

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	
        	switch(position){
        		case 1:
        			if(blog.getActiveFilter() != Blog.FILTER_ALL){
	        			blog.setActiveFilter( Blog.FILTER_ALL );
	        			blog.setFeedUrl( "http://blog.gobalo.es/feed/" );
	        			blog.setCurrentPage(1);
	        			//blog.loadCurrentPage(true);
        			}
        			mSectionsPagerAdapter.changeTitle(1, getString(R.string.title_section1));
        			mViewPager.setCurrentItem(1);
        			
        			break;
        		case 2:
        			mViewPager.setCurrentItem(0);
        			break;
        		case 3:
        			mViewPager.setCurrentItem(2);
        			break;
        	}
        	

        	//((TextView)view.findViewById(R.id.lblItemDrawerIcon)).setTextColor(getResources().getColor(R.color.white));
        	//((TextView)view.findViewById(R.id.lblItemDrawerText)).setTextColor(getResources().getColor(R.color.white));
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
    //---------------------------------------------------------------------------------
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	Log.d("MyApp","PAUSE");
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	Log.d("MyApp","RESUME");
    	if(MainActivity.blog==null){
    		MainActivity.blog=Blog.getInstance();
    		MainActivity.blog.setContext(this.getApplicationContext());
    		MainActivity.blog.loadFavouritesFromDB();
    		//Bitmap noImageBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.no_image);
    		//blog.getImages().put("empty", noImageBitmap);
    	}
    	mViewPager.setCurrentItem(1);
    	super.onResume();
    }
	
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
