package com.josex2r.digitalheroes.model;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.AsyncTaskListener;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.controllers.RssXmlPullParser;

public class Blog {

	//private List<Post> loadedPosts;
	private SparseArray<SparseArray<List<Post>>> filteredPagedPosts;
	public int currentPage;
	public boolean isLoading;
	private String feedUrl;
	private PostsAdapter adapter;
	private MainActivity mainActivity;
	private int activeFilter;
	private ProgressBar pbPostLoader;
	private int[] currentDisplaying; //0->Page, 1->Filter
	
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
	public static final int FILTER_NUMBERS=25;
	public static final int FILTER_PENCIL=26;
	public static final int FILTER_PIXEL=27;
	public static final int FILTER_SEM=28;
	public static final int FILTER_SOCIAL=29;
	public static final int FILTER_SPEED=30;
	public static final int FILTER_TRIX=31;
	
	public Blog(String url){
		this.feedUrl=url;
		this.currentPage=1;
		this.activeFilter=Blog.FILTER_ALL;
		this.currentDisplaying=new int[]{999999, 999999}; //Page undefined, filter unknown
		this.filteredPagedPosts=new SparseArray<SparseArray<List<Post>>>();
		this.isLoading=false;
	}
	
	public void setPostLoader(ProgressBar pb){
		this.pbPostLoader=pb;
	}
	
	public void setAdapter(PostsAdapter adapter){
		this.adapter=adapter;
	}
	
	public void setActivity(Activity activity){
		this.mainActivity=(MainActivity) activity;
	}
	
	public void setFeedUrl(String url){
		this.feedUrl=url;
	}
	
	public PostsAdapter getAdapter(){
		return adapter;
	}
	
	public MainActivity getActivity(){
		return mainActivity;
	}
	
	public SparseArray<SparseArray<List<Post>>> getFilteredPagedPosts(){
		return filteredPagedPosts;
	}
	
	public void setFilter(int filter){
		adapter.clear();
		adapter.notifyDataSetChanged();
		this.activeFilter=filter;
	}
	
	public int getFilter(){
		return activeFilter;
	}
	
	public int getPage(){
		return currentPage;
	}
	
	public void loadNextPage(){
		currentPage++;
		loadCurrentPage(false);
	}
	
	public void nextPage(){
		if(!isLoading && filteredPagedPosts.get(this.activeFilter)!=null
				&& filteredPagedPosts.get(this.activeFilter).get(this.currentPage)!=null){
			this.currentPage++;
			loadCurrentPage(true);
		}
	}
	
	public void loading(Boolean action, Boolean hideLoading){
		//Está cargando
		if(action){
			if(!hideLoading)
				mainActivity.showLoading();
			if(pbPostLoader!=null)
				pbPostLoader.setVisibility(View.VISIBLE);
			isLoading=true;
		//No está cargando
		}else{
			if(pbPostLoader!=null)
				pbPostLoader.setVisibility(View.GONE);
			isLoading=false;
			mainActivity.hideLoading();
		}
	}
	
	public void loadCurrentPage(Boolean hideLoading){
		
		if(currentDisplaying[0]!=this.currentPage || currentDisplaying[1]!=this.activeFilter){
			
			Log.d("MyApp","Filter: "+Integer.toString(activeFilter));
			loading(true, hideLoading);
			
			if(filteredPagedPosts.get(this.activeFilter)==null)
				filteredPagedPosts.put(this.activeFilter, new SparseArray<List<Post>>());
			Log.d("MyApp",Boolean.toString(filteredPagedPosts.get(this.activeFilter).get(this.currentPage)==null));
			if(filteredPagedPosts.get(this.activeFilter).get(this.currentPage)==null){
				FeedPageLoader page=new FeedPageLoader(new AsyncTaskListener() {
					@Override
					public void onTaskComplete(List<Post> loadedPosts) {
						// TODO Auto-generated method stub
						filteredPagedPosts.get(activeFilter).put(currentPage, loadedPosts);
						Log.d("MyApp","Callback displayPosts()");
						displayPosts();
					}
					public void onTaskFailed() {
						//No more posts
						loading(false, false);
						isLoading=true;
					}
				});
				page.execute(this.currentPage);
			}else{
				displayPosts();
				Log.d("MyApp","displayPosts()");
			}
		
		}
	}
	
	public void displayPosts(){
		//adapter.clear();
		//adapter.notifyDataSetChanged();
		List<Post> currentPosts=filteredPagedPosts.get(this.activeFilter).get(this.currentPage);
		for(int i=0;i<currentPosts.size();i++){
			Log.d("MyApp","Isertando al adaptador: "+currentPosts.get(i).getTitle());
			adapter.add(currentPosts.get(i));
		}
		adapter.getListView().setSelection(0);
		adapter.notifyDataSetChanged();
		loading(false, false);
		currentDisplaying[0]=this.currentPage;
		currentDisplaying[1]=this.activeFilter;
	}
	
	public class FeedPageLoader extends AsyncTask<Integer, Integer, List<Post>>{
		
		private Integer page;
		private AsyncTaskListener callback;
		
		public FeedPageLoader() {
			// TODO Auto-generated constructor stub
			callback=new AsyncTaskListener() {
				public void onTaskComplete(List<Post> loadedPosts) {}
				public void onTaskFailed() {}
			};
		}
		
		public FeedPageLoader(AsyncTaskListener onComplete) {
			// TODO Auto-generated constructor stub
			callback=onComplete;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//mainActivity.showLoading();
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
			if(result!=null)
				callback.onTaskComplete(result);
			else
				callback.onTaskFailed();
			//mainActivity.hideLoading();
			super.onPostExecute(result);
		}
	}
	
	
	
}
