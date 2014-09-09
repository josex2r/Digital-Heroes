package com.josex2r.digitalheroes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.BlogFilter;

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
		MainActivity mainActivity=(MainActivity)getActivity();
		Blog blog=Blog.getInstance();
		
		BlogFilter selectedFilter = blog.getActiveFilter();
		
		int id = v.getId();
		if (id == R.id.btnBinary) {
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
			
			blog.setActiveFilter(selectedFilter);
			
			blog.setCurrentPage(1);
			
			//mainActivity.getSectionsPageAdapter().changeTitle(1, selectedFilter.getName());
			//mainActivity.getViewPager().setCurrentItem(1);
			//mainActivity.getDrawerList().setItemChecked(3, true);
			mainActivity.getActionBar().setTitle(selectedFilter.getName());
		}
	}
}
