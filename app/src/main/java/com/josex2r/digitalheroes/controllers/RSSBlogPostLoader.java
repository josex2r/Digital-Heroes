package com.josex2r.digitalheroes.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;
import com.josex2r.digitalheroes.util.RssXmlPullParser;
import com.josex2r.digitalheroes.util.SuperCodeStolenPosts;

import java.util.ArrayList;
import java.util.List;

public class RSSBlogPostLoader extends AsyncTask<Integer, Integer, List<Post>>{
	
	private Integer page;
	private AsyncTaskListener<List<Post>> callback;
	
	public RSSBlogPostLoader() {
		// TODO Auto-generated constructor stub
		callback=new AsyncTaskListener<List<Post>>() {
			public void onTaskComplete(List<Post> loadedPosts) {}
			public void onTaskFailed() {}
		};
	}
	
	public RSSBlogPostLoader(AsyncTaskListener<List<Post>> onComplete) {
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

        String feedUrl;
        if(Blog.getInstance().getActiveFilter() == null){
            Blog.getInstance().setActiveFilter(Blog.FILTER_ALL);
            feedUrl = "http://blog.gobalo.es/feed/";
        }else{
            feedUrl = Blog.getInstance().getActiveFilter().getFeedURl();
        }

        str.append(feedUrl).append("?paged=").append(page.toString());

        if(Blog.getInstance().getActiveFilter().equals(Blog.FILTER_CODE)){
            str.delete(0, str.length());
            str.append(Blog.FILTER_BINARY.getFeedURl());
        }

		RssXmlPullParser parser=new RssXmlPullParser(str.toString());
		List<Post> posts=parser.getNews();
		return posts;
	}
	@Override
	protected void onPostExecute(List<Post> result) {
		// TODO Auto-generated method stub
		if(result!=null) {

            if(Blog.getInstance().getActiveFilter().equals(Blog.FILTER_CODE)){
                List<Post> filteredList = new ArrayList<Post>();
                for(Post filteredPost : result){
                    if(SuperCodeStolenPosts.stolenPosts.contains(filteredPost.getTitle())){
                        filteredList.add(filteredPost);
                    }
                }
            }else{
                callback.onTaskComplete(result);
            }
        }else
			callback.onTaskFailed();
		super.onPostExecute(result);
	}
}
