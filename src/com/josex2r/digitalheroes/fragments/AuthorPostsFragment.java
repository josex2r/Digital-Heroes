package com.josex2r.digitalheroes.fragments;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.model.Blog;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AuthorPostsFragment extends Fragment implements OnClickListener{
	public AuthorPostsFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_author_posts,
				container, false);
		
		Button btnBinary = (Button) rootView.findViewById(R.id.btnBinary);
		Button btnCode = (Button) rootView.findViewById(R.id.btnCode);
		Button btnCraft = (Button) rootView.findViewById(R.id.btnCraft);
		Button btnCrea = (Button) rootView.findViewById(R.id.btnCrea);
		Button btnIdea = (Button) rootView.findViewById(R.id.btnIdea);
		Button btnNumbers = (Button) rootView.findViewById(R.id.btnNumbers);
		Button btnPencil = (Button) rootView.findViewById(R.id.btnPencil);
		Button btnPixel = (Button) rootView.findViewById(R.id.btnPixel);
		Button btnSem = (Button) rootView.findViewById(R.id.btnSem);
		Button btnSocial = (Button) rootView.findViewById(R.id.btnSocial);
		Button btnSpeed = (Button) rootView.findViewById(R.id.btnSpeed);
		Button btnTrix = (Button) rootView.findViewById(R.id.btnTrix);
		
		btnBinary.setOnClickListener(this);
		btnCode.setOnClickListener(this);
		btnCraft.setOnClickListener(this);
		btnCrea.setOnClickListener(this);
		btnIdea.setOnClickListener(this);
		btnNumbers.setOnClickListener(this);
		btnPencil.setOnClickListener(this);
		btnPixel.setOnClickListener(this);
		btnSem.setOnClickListener(this);
		btnSocial.setOnClickListener(this);
		btnSpeed.setOnClickListener(this);
		btnTrix.setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle data=new Bundle();
		int id = v.getId();
		if (id == R.id.btnBinary) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/a-vara/feed/");
			data.putString("name", "Super 01101");
			data.putInt("filter", Blog.FILTER_BINARY);
		} else if (id == R.id.btnCode) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/jl-represa/feed/");
			data.putString("name", "Super Code");
			data.putInt("filter", Blog.FILTER_CODE);
		} else if (id == R.id.btnCraft) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/n-pastor/feed/");
			data.putString("name", "Super Craft");
			data.putInt("filter", Blog.FILTER_CRAFT);
		} else if (id == R.id.btnCrea) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/g-gomez/feed/");
			data.putString("name", "Super Crea");
			data.putInt("filter", Blog.FILTER_CREA);
		} else if (id == R.id.btnIdea) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/j-azpeitia/feed/");
			data.putString("name", "Super Idea");
			data.putInt("filter", Blog.FILTER_IDEA);
		} else if (id == R.id.btnNumbers) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/m-becerra/feed/");
			data.putString("name", "Super Numbers");
			data.putInt("filter", Blog.FILTER_NUMBERS);
		} else if (id == R.id.btnPencil) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/a-fassi/feed/");
			data.putString("name", "Super Pencil");
			data.putInt("filter", Blog.FILTER_PENCIL);
		} else if (id == R.id.btnPixel) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/f-bril/feed/");
			data.putString("name", "Super Pixel");
			data.putInt("filter", Blog.FILTER_PIXEL);
		} else if (id == R.id.btnSem) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/l-casado/feed/");
			data.putString("name", "Super SEM");
			data.putInt("filter", Blog.FILTER_SEM);
		} else if (id == R.id.btnSocial) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/bloggobalo-es/feed/");
			data.putString("name", "Super Social");
			data.putInt("filter", Blog.FILTER_SOCIAL);
		} else if (id == R.id.btnSpeed) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/a-gonzalez/feed/");
			data.putString("name", "Super Speed");
			data.putInt("filter", Blog.FILTER_SPEED);
		} else if (id == R.id.btnTrix) {
			data.putString("feedUrl", "http://blog.gobalo.es/author/cristina/feed/");
			data.putString("name", "Super Trix");
			data.putInt("filter", Blog.FILTER_TRIX);
		}
		
		MainActivity mainActivity=(MainActivity)getActivity();
		Blog blog=Blog.getInstance();
		
		if( blog.getActiveFilter() != data.getInt("filter") ){
		
			blog.setActiveFilter(data.getInt("filter"));
			blog.setFeedUrl(data.getString("feedUrl"));
			blog.setCurrentPage(1);
			
			/*
			Fragment newPostsFragment = new AllPostsFragment();
			newPostsFragment.setArguments(data);
	
			MainActivity main=((MainActivity) getActivity());*/
			mainActivity.getSectionsPageAdapter().changeTitle(1, data.getString("name"));
			mainActivity.getViewPager().setCurrentItem(1);
			mainActivity.getDrawerList().setItemChecked(3, true);
		}

		
		//blog.loadCurrentPage(true);
	}
}
