package com.josex2r.digitalheroes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.BlogFilter;

public class AuthorPostsFragment extends Fragment implements OnClickListener{

    private Blog blog;
    private MainActivity mainActivity;

	public AuthorPostsFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_author_posts,
				container, false);

        blog = Blog.getInstance();
        mainActivity = (MainActivity)getActivity();

        ((LinearLayout) rootView.findViewById(R.id.lyBinary)).setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		BlogFilter selectedFilter = blog.getActiveFilter();
		
		int id = v.getId();
		if (id == R.id.lyBinary) {
			selectedFilter = Blog.FILTER_BINARY;
		} else if (id == R.id.btnCode) {
			selectedFilter = Blog.FILTER_CODE;
		} else if (id == R.id.btnCraft) {
			selectedFilter = Blog.FILTER_CRAFT;
		} else if (id == R.id.btnCrea) {
			selectedFilter = Blog.FILTER_CREA;
		} else if (id == R.id.btnIdea) {
			selectedFilter = Blog.FILTER_IDEA;
		} else if (id == R.id.btnNumbers) {
			selectedFilter = Blog.FILTER_NUMBERS;
		} else if (id == R.id.btnPencil) {
			selectedFilter = Blog.FILTER_PENCIL;
		} else if (id == R.id.btnPixel) {
			selectedFilter = Blog.FILTER_PIXEL;
		} else if (id == R.id.btnSem) {
			selectedFilter = Blog.FILTER_SEM;
		} else if (id == R.id.btnSocial) {
			selectedFilter = Blog.FILTER_SOCIAL;
		} else if (id == R.id.btnSpeed) {
			selectedFilter = Blog.FILTER_SPEED;
		} else if (id == R.id.btnTrix) {
			selectedFilter = Blog.FILTER_TRIX;
		}
		
		if( !blog.getActiveFilter().equals(selectedFilter) ){
            //Change blog data
            blog.setActiveFilter(selectedFilter);
            blog.setCurrentPage(1);
            //Perform Main Activity stuff
            mainActivity.getViewPager().setCurrentItem(1);
            mainActivity.getActionBar().setTitle(selectedFilter.getName());
            //Force refresh
            mainActivity.getSectionsPageAdapter().notifyDataSetChanged();
		}
	}
}
