package com.josex2r.digitalheroes.controllers;

import java.util.List;

import android.os.AsyncTask;

import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;
import com.josex2r.digitalheroes.util.RssXmlPullParser;

public class FeedPageLoader extends AsyncTask<Integer, Integer, List<Post>>{
	
	private Integer page;
	private AsyncTaskListener<List<Post>> callback;
	
	public FeedPageLoader() {
		// TODO Auto-generated constructor stub
		callback=new AsyncTaskListener<List<Post>>() {
			public void onTaskComplete(List<Post> loadedPosts) {}
			public void onTaskFailed() {}
		};
	}
	
	public FeedPageLoader(AsyncTaskListener<List<Post>> onComplete) {
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
		str.append(Blog.getInstance().getActiveFilter().getFeedURl()).append("?paged=").append(page.toString());
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
		super.onPostExecute(result);
	}
}
