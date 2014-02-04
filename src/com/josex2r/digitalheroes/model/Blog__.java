package com.josex2r.digitalheroes.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification.Action;
import android.os.AsyncTask;
import android.util.SparseArray;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.controllers.RssXmlPullParser;

public class Blog__ {
	
	private SparseArray<List<Post>> pages;
	private int currentPage;
	private String feedUrl, extraUrl;
	private PostsAdapter adapter;
	private MainActivity mainActivity;
	
	public Blog__(Activity activity, String url, PostsAdapter adapter, String extraUrl){
		this.feedUrl=url;
		this.extraUrl=extraUrl;
		this.adapter=adapter;
		this.currentPage=1;
		this.pages=new SparseArray<List<Post>>();
		this.mainActivity=(MainActivity) activity;
		this.loadCurrentPage();
	}
	
	public void prev(){
		if(currentPage>1){
			currentPage--;
			//Clear screen
			adapter.clear();
			adapter.notifyDataSetChanged();
			//Load data
			List<Post> cachedPage=pages.get(currentPage);
			if(cachedPage==null){
				pages.put(currentPage, new ArrayList<Post>());
	    		this.loadCurrentPage();
			}else{
				//Page is on cache
				System.out.println("cached page");
				for(int i=0;i<cachedPage.size();i++){
					adapter.insert(cachedPage.get(i), i);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
	public void next(){
		currentPage++;
		//Clear screen
		adapter.clear();
		adapter.notifyDataSetChanged();
		//Load data
		List<Post> cachedPage=pages.get(currentPage);
		if(cachedPage==null){
			pages.put(currentPage, new ArrayList<Post>());
    		this.loadCurrentPage();
		}else{
			//Page is on cache
			for(int i=0;i<cachedPage.size();i++){
				adapter.insert(cachedPage.get(i), i);
			}
			adapter.notifyDataSetChanged();
			mainActivity.hideLoading();
		}
	}
	public List<Post> getCurrentPage(){
		return null;
	}
	
	public void loadCurrentPage(){
		mainActivity.showLoading();
		PostPageLoader post=new PostPageLoader();
		post.execute(currentPage);
	}
	
	public class PostPageLoader extends AsyncTask<Integer, Integer, Boolean>{
		private Integer currPage;
		private List<Post> news;

		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			currPage=params[0];
			StringBuilder str=new StringBuilder();
			str.append(feedUrl).append("?paged=").append(currPage.toString()).append(extraUrl);
			RssXmlPullParser parser=new RssXmlPullParser(str.toString());
			news=parser.getNews();
			pages.put(currPage, news);
			return news!=null && news.size()>0;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result){
				for(int i=0;i<news.size();i++){
					adapter.insert(news.get(i), i);
					//Log.d("MyApp", news.get(i).getTitle());
				}
				adapter.notifyDataSetChanged();
			}else{
				prev();
			}
			mainActivity.hideLoading();
			super.onPostExecute(result);
		}
	}
}
