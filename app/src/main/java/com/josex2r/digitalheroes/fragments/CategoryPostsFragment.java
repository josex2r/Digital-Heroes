package com.josex2r.digitalheroes.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.BlogFilter;
import com.josex2r.digitalheroes.model.Typefaces;


public class CategoryPostsFragment extends Fragment implements OnClickListener{

    private Blog blog;
    private MainActivity mainActivity;
	
	public CategoryPostsFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category_posts,
				container, false);

        blog = Blog.getInstance();
        mainActivity = (MainActivity)getActivity();

        ((LinearLayout) rootView.findViewById(R.id.lyIdeas)).setOnClickListener(this);
        ((LinearLayout) rootView.findViewById(R.id.lyContent)).setOnClickListener(this);
        ((LinearLayout) rootView.findViewById(R.id.lyGoogle)).setOnClickListener(this);
        ((LinearLayout) rootView.findViewById(R.id.lyInside)).setOnClickListener(this);

        Typeface font = Typefaces.get( getActivity().getApplicationContext(), "font/fontawesome-webfont.ttf" );

        ((TextView) rootView.findViewById(R.id.btnIdeasIcon)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.btnContentIcon)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.btnGoogleIcon)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.btnInsideIcon)).setTypeface(font);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		BlogFilter selectedFilter = blog.getActiveFilter();
		
		int id = v.getId();

        if (id == R.id.lyIdeas || id == R.id.btnIdeasIcon) {
            selectedFilter = Blog.FILTER_IDEAS;
        } else if (id == R.id.lyContent || id == R.id.btnContentIcon) {
            selectedFilter = Blog.FILTER_CONTENT;
        } else if (id == R.id.lyGoogle || id == R.id.btnGoogleIcon) {
            selectedFilter = Blog.FILTER_GOOGLE;
        } else if (id == R.id.lyInside || id == R.id.btnInsideIcon) {
            selectedFilter = Blog.FILTER_INSIDE;
        }
		//Check if selected filter is not the current filter
		if( !blog.getActiveFilter().equals(selectedFilter) ){
		    //Change blog data
			blog.setActiveFilter(selectedFilter);
            //Perform Main Activity stuff
			mainActivity.getViewPager().setCurrentItem(1);
			mainActivity.getActionBar().setTitle(selectedFilter.getName());
            //Force refresh
            mainActivity.getSectionsPageAdapter().notifyDataSetChanged();
		}
	}
}
