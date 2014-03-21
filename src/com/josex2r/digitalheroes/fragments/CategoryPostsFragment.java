package com.josex2r.digitalheroes.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.controllers.Typefaces;
import com.josex2r.digitalheroes.model.Blog;


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
		
		//Añadir las fuentes de los iconos
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
		Bundle data=new Bundle();
		switch(v.getId()){
			case R.id.btnAdversiting:
			case R.id.btnAdversitingIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/advertising-2/feed/");
				data.putString("name", "Advertising");
				data.putInt("filter", Blog.FILTER_ADVERSITING);
				break;
			case R.id.btnCreatividad:
			case R.id.btnCreatividadIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/creatividad/feed/");
				data.putString("name", "Creatividad");
				data.putInt("filter", Blog.FILTER_CREATIVIDAD);
				break;
			case R.id.btnInside:
			case R.id.btnInsideIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/inside-gobalo/feed/");
				data.putString("name", "Inside Góbalo");
				data.putInt("filter", Blog.FILTER_INSIDE);
				break;
			case R.id.btnMarketing:
			case R.id.btnMarketingIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/marketing-digital-y-social-media/feed/");
				data.putString("name", "Marketing Digital");
				data.putInt("filter", Blog.FILTER_MARKETING);
				break;
			case R.id.btnNegocios:
			case R.id.btnNegociosIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/negocios/feed/");
				data.putString("name", "Negocios");
				data.putInt("filter", Blog.FILTER_NEGOCIOS);
				break;
			case R.id.btnSeo:
			case R.id.btnSeoIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/seo-y-sem/feed/");
				data.putString("name", "SEO y SEM");
				data.putInt("filter", Blog.FILTER_SEO);
				break;
			case R.id.btnWeb:
			case R.id.btnWebIcon:
				data.putString("feedUrl", "http://blog.gobalo.es/category/web-y-programacion/feed/");
				data.putString("name", "Web y Programación");
				data.putInt("filter", Blog.FILTER_WEB);
				break;
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
			mainActivity.getDrawerList().setItemChecked(2, true);
		}
		
		//blog.loadCurrentPage(true);
	}
}
