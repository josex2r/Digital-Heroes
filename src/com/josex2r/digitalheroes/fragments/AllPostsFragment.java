package com.josex2r.digitalheroes.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;


public class AllPostsFragment extends Fragment implements OnItemClickListener, OnScrollListener{
	
	private Blog blog;
	private PostsAdapter adapter;
	private ListView lvPosts;
	
	private int visibleThreshold = 5;
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
		
		MainActivity mainActivity=(MainActivity)getActivity();
		
		this.blog=mainActivity.getBlog();
		
		if(blog.getActivity()==null){
			blog.setActivity(getActivity());
			blog.setPostLoader( (ProgressBar)rootView.findViewById(R.id.pbPostLoader) );
		}
		
		lvPosts=(ListView)rootView.findViewById(R.id.lvPosts);
		
		adapter=blog.getAdapter();
		if(adapter==null){
			adapter=new PostsAdapter(getActivity(), R.layout.blog_post, new ArrayList<Post>(), lvPosts);
			blog.setAdapter(adapter);
		}
		
		lvPosts.setAdapter(adapter);
		lvPosts.setOnItemClickListener(this);
		lvPosts.setOnScrollListener(this);
		
		
		blog.loadCurrentPage(false);

		return rootView;
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		int filter=blog.getFilter();
		int page=blog.getPage();
		StringBuilder str=new StringBuilder();
		String url=adapter.getItem(position).getLink();
		str.append(url).append("&android=true");
		Toast.makeText(getActivity(), "Link: "+str.toString(), Toast.LENGTH_LONG).show();
		
		Intent i = new Intent("com.josex2r.digitalheroes.BrowserActivity");
		i.setData(Uri.parse(str.toString()));
		startActivity(i);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	
        if (!blog.isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            Log.d("MyApp","-------------------------------------------");
            Log.d("MyApp","--------------- LOADING PAGE --------------");
            Log.d("MyApp","-------------------------------------------");
            
            blog.nextPage();
        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
}