package com.josex2r.digitalheroes.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.controllers.AsyncTaskListener;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.controllers.RssXmlPullParser;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;


public class AllPostsFragment extends Fragment implements OnItemClickListener, OnScrollListener, OnClickListener{
	//-------------	Blog model -------------
	private Blog blog;
	//-------------	ListView adapter -------------
	private PostsAdapter adapter;
	//-------------	ListView -------------
	private ListView lvPosts;
	//-------------	Progress bar circle -------------
	private LinearLayout lyLoader;
	//-------------	Load post on scrolling -------------
	private int visibleThreshold = 5;
	
	//-------------	Constructor -------------
    public AllPostsFragment(){
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all_posts,
				container, false);
		//-------------	Get Activity -------------
		//MainActivity mainActivity=(MainActivity)getActivity();
		//-------------	Get static Blog -------------
		this.blog=Blog.getInstance();
		//-------------	Get loader from View -------------
		lyLoader = (LinearLayout)rootView.findViewById(R.id.lyLoader);
		//-------------	Set Loading state -------------
		loading(true);
		//-------------	Manage ListView -------------
		lvPosts=(ListView)rootView.findViewById(R.id.lvPosts);
		adapter=new PostsAdapter(getActivity(), R.layout.blog_post, new ArrayList<Post>(), lvPosts, this);
		lvPosts.setAdapter(adapter);
		lvPosts.setOnItemClickListener(this);
		lvPosts.setOnScrollListener(this);
		//-------------	If post loaded from internet == 0, else show list -------------
		List<Post> currentPosts=blog.getFilteredAllPagedPosts();
		Log.d("MyApp", "-------- Page: "+Integer.toString(blog.getCurrentPage()) );
		if( currentPosts.size()==0 ){
			adapter.clear();
			loadCurrentPage();
		}else{
			blog.setCurrentPage(1);
			displayPosts();
		}
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		lvPosts.setSelection(0);
		super.onResume();
	}
	
	//-------------	Add posts to ListView -------------
	public void displayPosts(){
		loading(true);
		List<Post> currentPosts=blog.getFilteredPagedPosts();
		for(int i=0;i<currentPosts.size();i++){
			//Log.d("MyApp","Insertando al adaptador: "+currentPosts.get(i).getTitle());
			adapter.add(currentPosts.get(i));
		}
		adapter.getListView().setSelection(0);
		adapter.notifyDataSetChanged();
		loading(false);
	}
	
	//-------------	Loader actions -------------
	public void loading(boolean action){
		if(action){
			if(lyLoader!=null)
				lyLoader.setVisibility(View.VISIBLE);
			blog.setLoading(true);
		}else{
			if(lyLoader!=null)
				lyLoader.setVisibility(View.GONE);
			blog.setLoading(false);
		}
	}
	
	//-------------	Load posts from Internet -------------
	public void loadCurrentPage(){
		FeedPageLoader page=new FeedPageLoader(new AsyncTaskListener<List<Post>>() {
			@Override
			public void onTaskComplete(List<Post> loadedPosts) {
				blog.addPosts(blog.getActiveFilter(), blog.getCurrentPage(), loadedPosts);
				displayPosts();
				loading(false);
			}
			public void onTaskFailed() {
				//No more posts
				loading(false);
				//Force loading state to prevent more loadings
				blog.setLoading(true);
			}
		});
		page.execute(blog.getCurrentPage());
	}
	
	
	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		Log.d("MyApp",Integer.toString(view.getId()));
		Post selectedPost=adapter.getItem(position);
		StringBuilder str=new StringBuilder();
		String url=selectedPost.getLink();
		str.append(url).append("&android=true");
		
		Bundle data=new Bundle();
		data.putString("uri", str.toString());
		data.putString("title", selectedPost.getTitle());
		
		Intent i = new Intent("com.josex2r.digitalheroes.BrowserActivity");
		i.putExtras(data);
		//i.setData(Uri.parse(str.toString()));
		startActivity(i);
		
		// TODO Auto-generated method stub
		
		//Toast.makeText(getActivity(), "Link: "+str.toString(), Toast.LENGTH_LONG).show();
		/*
		Bundle data=new Bundle();
		data.putString("uri", str.toString());
		data.putString("title", selectedPost.getTitle());
		
		Intent i = new Intent("com.josex2r.digitalheroes.BrowserActivity");
		i.putExtras(data);
		startActivity(i);*/
	}

	
	
	
	
	
	
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	
        if (!blog.isLoading() && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

            loading(true);
            
            blog.setCurrentPage(blog.getCurrentPage()+1);
            
        	List<Post> posts=blog.getFilteredPagedPosts();
        	
        	if( posts.size()>0 ){
        		//Log.d("MyApp", "Se han encontrado posts, page: "+Integer.toString(blog.getCurrentPage()));
        		displayPosts();
        		loading(false);
        	}else{
        		//Log.d("MyApp", "No se han encontrado posts");
        		loadCurrentPage();
        	}
            
            //blog.nextPage();
        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	
	
	
	
	
	
	
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
			str.append(blog.getFeedUrl()).append("?paged=").append(page.toString());
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







	@Override
	public void onClick(View v) {
		Integer position=(Integer) v.getTag();
		List<Post> currentPosts=blog.getFilteredAllPagedPosts();
		
		Log.d("MyApp","Has clickado en la estrella nº:"+Integer.toString(position));
		Log.d("MyApp","Título: "+currentPosts.get(position).getTitle());
		
		Blog.getInstance().addRemoveFromFavourites( position );
		adapter.notifyDataSetChanged();
	}


}