package com.josex2r.digitalheroes;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.josex2r.digitalheroes.controllers.NewPostAlarm;
import com.josex2r.digitalheroes.fragments.AllPostsFragment;
import com.josex2r.digitalheroes.fragments.AuthorPostsFragment;
import com.josex2r.digitalheroes.fragments.CategoryPostsFragment;
import com.josex2r.digitalheroes.fragments.NavigationDrawerFragment;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.util.TrackerName;
import com.josex2r.digitalheroes.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    //ViewPager
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> fragmentTitles;

    //New post alarm
    private static NewPostAlarm newPostAlarm;

    //ActionBar
    private ActionBar actionBar;

    //Scrollable menu
    private NavigationDrawerFragment mNavigationDrawerFragment;

    //Analytics
    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    private final static String TRACKER_ID="UA-49401782-1";

    //Blog
    private Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Refresh icon
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.activity_main);

        //Initialize blog
        blog = Blog.getInstance();
        blog.initBlog(this.getApplicationContext());
        blog.loadFavouritesFromDB();

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
        fragmentTitles.add(getString(R.string.view_pager_section1));
        fragmentTitles.add(getString(R.string.view_pager_section2));
        fragmentTitles.add(getString(R.string.view_pager_section3));
        //Add fragments and titles to the adapter
        mSectionsPagerAdapter.addFragments(fragmentList, fragmentTitles);

        //Init drawer menu
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    protected void onResume() {
        super.onResume();

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
            Tracker t = this.getTracker(TrackerName.APP_TRACKER);
            t.setScreenName("capture");
            t.send(new HitBuilders.AppViewBuilder().build());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(getApplicationContext(), getString(R.string.failed_loading_feed), Toast.LENGTH_LONG).show();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ViewPager getViewPager(){
        return mViewPager;
    }
    public SectionsPagerAdapter getSectionsPageAdapter(){
        return mSectionsPagerAdapter;
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
        public android.support.v4.app.Fragment getItem(int i) {
            if (i >= fragmentList.size()) {
                return null;
            }
            return fragmentList.get(i);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch(position){
            //SHOW ALL POSTS
            case 1:
                if( !blog.getActiveFilter().equals(Blog.FILTER_ALL) ){
                    blog.setActiveFilter( Blog.FILTER_ALL );
                    blog.setCurrentPage(1);
                }
                //Force to refresh by changing the title
                mSectionsPagerAdapter.changeTitle(1, getString(R.string.navigation_drawer_section1));
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
                //Force to refresh by changing the title
                mSectionsPagerAdapter.changeTitle(1, getString(R.string.navigation_drawer_section4));
                actionBar.setTitle(getString(R.string.navigation_drawer_section4));
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
    }
}
