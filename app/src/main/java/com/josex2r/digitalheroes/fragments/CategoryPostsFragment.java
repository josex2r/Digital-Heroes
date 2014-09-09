package com.josex2r.digitalheroes.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.BlogFilter;
import com.josex2r.digitalheroes.model.Typefaces;


public class CategoryPostsFragment extends Fragment implements OnClickListener{
	
	public CategoryPostsFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category_posts,
				container, false);
		
		Button btnAdvertising = (Button) rootView.findViewById(R.id.btnAdversiting);
		Button btnCreatividad = (Button) rootView.findViewById(R.id.btnCreatividad);
		Button btnInside = (Button) rootView.findViewById(R.id.btnInside);
		Button btnMarketing = (Button) rootView.findViewById(R.id.btnMarketing);
		Button btnNegocios = (Button) rootView.findViewById(R.id.btnNegocios);
		Button btnSeo = (Button) rootView.findViewById(R.id.btnSeo);
		Button btnWeb = (Button) rootView.findViewById(R.id.btnWeb);
		
		//A�adir las fuentes de los iconos
		Button btnAdvertisingIcon = (Button) rootView.findViewById(R.id.btnAdversitingIcon);
		Button btnCreatividadIcon = (Button) rootView.findViewById(R.id.btnCreatividadIcon);
		Button btnInsideIcon = (Button) rootView.findViewById(R.id.btnInsideIcon);
		Button btnMarketingIcon = (Button) rootView.findViewById(R.id.btnMarketingIcon);
		Button btnNegociosIcon = (Button) rootView.findViewById(R.id.btnNegociosIcon);
		Button btnSeoIcon = (Button) rootView.findViewById(R.id.btnSeoIcon);
		Button btnWebIcon = (Button) rootView.findViewById(R.id.btnWebIcon);
		
		Typeface font = Typefaces.get( getActivity().getApplicationContext(), "font/fontawesome-webfont.ttf" );
		
		btnAdvertisingIcon.setTypeface(font);
		btnCreatividadIcon.setTypeface(font);
		btnInsideIcon.setTypeface(font);
		btnMarketingIcon.setTypeface(font);
		btnNegociosIcon.setTypeface(font);
		btnSeoIcon.setTypeface(font);
		btnWebIcon.setTypeface(font);
		
		btnAdvertising.setOnClickListener(this);
		btnCreatividad.setOnClickListener(this);
		btnInside.setOnClickListener(this);
		btnMarketing.setOnClickListener(this);
		btnNegocios.setOnClickListener(this);
		btnSeo.setOnClickListener(this);
		btnWeb.setOnClickListener(this);
		btnAdvertisingIcon.setOnClickListener(this);
		btnCreatividadIcon.setOnClickListener(this);
		btnInsideIcon.setOnClickListener(this);
		btnMarketingIcon.setOnClickListener(this);
		btnNegociosIcon.setOnClickListener(this);
		btnSeoIcon.setOnClickListener(this);
		btnWebIcon.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MainActivity mainActivity=(MainActivity)getActivity();
		Blog blog=Blog.getInstance();
		
		BlogFilter selectedFilter = blog.getActiveFilter();
		
		int id = v.getId();
		if (id == R.id.btnAdversiting || id == R.id.btnAdversitingIcon) {
			selectedFilter = Blog.FILTER_ADVERSITING;
		} else if (id == R.id.btnCreatividad || id == R.id.btnCreatividadIcon) {
			selectedFilter = Blog.FILTER_CREATIVIDAD;
		} else if (id == R.id.btnInside || id == R.id.btnInsideIcon) {
			selectedFilter = Blog.FILTER_INSIDE;
		} else if (id == R.id.btnMarketing || id == R.id.btnMarketingIcon) {
			selectedFilter = Blog.FILTER_MARKETING;
		} else if (id == R.id.btnNegocios || id == R.id.btnNegociosIcon) {
			selectedFilter = Blog.FILTER_NEGOCIOS;
		} else if (id == R.id.btnSeo || id == R.id.btnSeoIcon) {
			selectedFilter = Blog.FILTER_SEO;
		} else if (id == R.id.btnWeb || id == R.id.btnWebIcon) {
			selectedFilter = Blog.FILTER_WEB;
		}
		
		if( !blog.getActiveFilter().equals(selectedFilter) ){
		
			blog.setActiveFilter(selectedFilter);
			blog.setCurrentPage(1);
			
			/*
			Fragment newPostsFragment = new AllPostsFragment();
			newPostsFragment.setArguments(data);
	
			MainActivity main=((MainActivity) getActivity());*/
			//mainActivity.getSectionsPageAdapter().changeTitle(1, selectedFilter.getName());
			//mainActivity.getViewPager().setCurrentItem(1);
			//mainActivity.getDrawerList().setItemChecked(2, true);
			mainActivity.getActionBar().setTitle(selectedFilter.getName());
		}
		
		//blog.loadCurrentPage(true);
	}
}
