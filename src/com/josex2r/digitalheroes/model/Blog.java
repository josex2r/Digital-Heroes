package com.josex2r.digitalheroes.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.AsyncTaskListener;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.controllers.RssXmlPullParser;

public class Blog {

	private List<Post> loadedPosts;
	public int currentPage;
	private int lastLoadedPage;
	private String feedUrl;
	private PostsAdapter adapter;
	private MainActivity mainActivity;
	private int activeFilter;
	
	public static final int POSTS_PER_FEED=10;
	public static final int MAX_ASYNC_TRIES=3;
	
	public static final int FILTER_ALL=0;
	
	public static final int FILTER_ADVERSITING=1;
	public static final int FILTER_CREATIVIDAD=2;
	public static final int FILTER_INSIDE=3;
	public static final int FILTER_MARKETING=4;
	public static final int FILTER_NEGOCIOS=5;
	public static final int FILTER_SEO=6;
	public static final int FILTER_WEB=7;
	
	public static final int FILTER_BINARY=20;
	public static final int FILTER_CODE=21;
	public static final int FILTER_CRAFT=22;
	public static final int FILTER_CREA=23;
	public static final int FILTER_IDEA=24;
	public static final int FILTER_PENCIL=25;
	public static final int FILTER_PIXEL=26;
	public static final int FILTER_SEM=27;
	public static final int FILTER_SOCIAL=28;
	public static final int FILTER_SPEED=29;
	public static final int FILTER_TRIX=30;
	
	public Blog(String url){
		this.feedUrl=url;
		this.currentPage=1;
		this.lastLoadedPage=1;
		this.activeFilter=this.FILTER_ALL;
		this.loadedPosts=new ArrayList<Post>();
	}
	
	public void setAdapter(PostsAdapter adapter){
		this.adapter=adapter;
	}
	
	public void setActivity(Activity activity){
		this.mainActivity=(MainActivity) activity;
	}
	
	public PostsAdapter getAdapter(){
		return adapter;
	}
	
	public MainActivity getActivity(){
		return mainActivity;
	}
	
	public void loadNextPage(){
		currentPage++;
		loadCurrentPage();
	}
	
	public void loadCurrentPage(){
		Log.d("MyApp","Loaded posts: "+Integer.toString(loadedPosts.size()));
		Log.d("MyApp","Filter: "+Integer.toString(activeFilter));
		if(loadedPosts.size()==0){
			//First Blog load
			FeedPageLoader firstPage=new FeedPageLoader(new AsyncTaskListener() {
				@Override
				public void onTaskComplete(Boolean result) {
					if(result)
						displayPosts( loadedPosts );
				}
			});
			firstPage.execute(currentPage);
		}else{
			if(activeFilter==this.FILTER_ALL)
				displayPosts( this.loadedPosts );
			else{
				List<Post> filteredPosts=filterPosts();
				if(filteredPosts.size()<Blog.POSTS_PER_FEED){
					
					FeedPageLoader firstPage=new FeedPageLoader(new AsyncTaskListener() {
						@Override
						public void onTaskComplete(Boolean result) {
							List<Post> filteredPosts=filterPosts();
							displayPosts( filteredPosts );
						}
					});
					lastLoadedPage++;
					firstPage.execute(lastLoadedPage);
					
				}else
					displayPosts( filteredPosts );
			}
		}
	}
	
	public List<Post> filterPosts(){
		List<Post> filtered=new ArrayList<Post>();
		for(int i=0;i<loadedPosts.size();i++){
			if(loadedPosts.get(i).hasCategory(activeFilter))
				filtered.add(loadedPosts.get(i));
		}
		return filtered;
	}
	
	public void setFilter(int filter){
		this.activeFilter=filter;
	}
	
	public void displayPosts(List<Post> posts){
		adapter.clear();
		adapter.notifyDataSetChanged();
		for(int i=0;i<Blog.POSTS_PER_FEED;i++){
			if(i<posts.size() && posts.get(i)!=null){
				Log.d("MyApp","Isertanto al adaptador: "+posts.get(i).getTitle());
				adapter.insert(posts.get(i), i);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public class FeedPageLoader extends AsyncTask<Integer, Integer, List<Post>>{
		
		private Integer page;
		private AsyncTaskListener callback;
		
		public FeedPageLoader() {
			// TODO Auto-generated constructor stub
			callback=new AsyncTaskListener() {
				@Override
				public void onTaskComplete(Boolean result){}
			};
		}
		
		public FeedPageLoader(AsyncTaskListener onComplete) {
			// TODO Auto-generated constructor stub
			callback=onComplete;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mainActivity.showLoading();
			super.onPreExecute();
		}
		
		@Override
		protected List<Post> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			page=params[0];
			StringBuilder str=new StringBuilder();
			str.append(feedUrl).append("?paged=").append(page.toString());
			Log.d("MyApp",str.toString());
			RssXmlPullParser parser=new RssXmlPullParser(str.toString());
			List<Post> posts=parser.getNews();
			return posts;
		}
		@Override
		protected void onPostExecute(List<Post> result) {
			// TODO Auto-generated method stub
			if(result!=null){
				for(int i=0;i<result.size();i++){
					if(loadedPosts.indexOf(result.get(i))<0){
						//Log.d("MyApp",result.get(i).getTitle());
						loadedPosts.add(result.get(i));
					}
				}
				Log.d("MyApp","Loaded posts: "+Integer.toString(loadedPosts.size()));
			}
			callback.onTaskComplete(result!=null);
			mainActivity.hideLoading();
			super.onPostExecute(result);
		}
	}
	
	
	
}
