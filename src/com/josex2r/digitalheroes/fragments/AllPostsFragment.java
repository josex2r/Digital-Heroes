package com.josex2r.digitalheroes.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.PostsAdapter;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.Post;


public class AllPostsFragment extends Fragment implements OnItemClickListener{
	
	private Blog blog;
	private PostsAdapter adapter;
	private int page=1;
	
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
		
		/*Bundle data=this.getArguments();
		if(data==null){
			data=new Bundle();
			data.putString("feedUrl", "http://www.gobalo.es/blog/feed/");
		}*/
		
		
		//blog=new Blog(getActivity(), data.getString("feedUrl"), adapter, "");
		
		MainActivity mainActivity=(MainActivity)getActivity();
		
		this.blog=mainActivity.getBlog();
		
		if(blog.getActivity()==null){
			blog.setActivity(getActivity());
		}
		
		adapter=blog.getAdapter();
		if(adapter==null){
			adapter=new PostsAdapter(getActivity(), R.layout.blog_post, new ArrayList<Post>());
			blog.setAdapter(adapter);
		}
		ListView lvPosts=(ListView)rootView.findViewById(R.id.lvPosts);
		lvPosts.setAdapter(adapter);
		lvPosts.setOnItemClickListener(this);
		
		
		blog.loadCurrentPage();

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		int filter=blog.getFilter();
		int page=blog.getPage();
		StringBuilder str=new StringBuilder();
		String url=blog.getFilteredPagedPosts().get(filter).get(page).get(position).getLink();
		str.append(url).append("&android=true");
		Toast.makeText(getActivity(), "Link: "+str.toString(), Toast.LENGTH_LONG).show();
		
		Intent i = new Intent("com.josex2r.digitalheroes");
		i.setData(Uri.parse(str.toString()));
		startActivity(i);
	}
}