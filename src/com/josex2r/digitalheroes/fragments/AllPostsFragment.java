package com.josex2r.digitalheroes.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.model.Blog__;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;


public class AllPostsFragment extends Fragment{
	
	private PostsAdapter adapter;
	private int page=1;
	
	public AllPostsFragment(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all_posts,
				container, false);
		
		/*Bundle data=this.getArguments();
		if(data==null){
			data=new Bundle();
			data.putString("feedUrl", "http://www.gobalo.es/blog/feed/");
		}*/
		
		
		//blog=new Blog(getActivity(), data.getString("feedUrl"), adapter, "");
		
		MainActivity mainActivity=(MainActivity)getActivity();
		
		Blog blog=mainActivity.getBlog();
		
		if(blog.getActivity()==null){
			blog.setActivity(getActivity());
		}
		
		adapter=blog.getAdapter();
		if(adapter==null){
			adapter=new PostsAdapter(getActivity(), R.layout.blog_post_portrait, new ArrayList<Post>());
			blog.setAdapter(adapter);
		}
		ListView lvPosts=(ListView)rootView.findViewById(R.id.lvPosts);
		lvPosts.setAdapter(adapter);

		blog.loadCurrentPage();

		return rootView;
	}
}