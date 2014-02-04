package com.josex2r.digitalheroes.fragments;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.MainActivity;

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
		switch(v.getId()){
			case R.id.btnBinary:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/a-vara/feed/");
				data.putString("name", "Super 01101");
				break;
			case R.id.btnCode:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/jl-represa/feed/");
				data.putString("name", "Super Code");
				break;
			case R.id.btnCraft:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/n-pastor/feed/");
				data.putString("name", "Super Craft");
				break;
			case R.id.btnCrea:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/g-gomez/feed/");
				data.putString("name", "Super Crea");
				break;
			case R.id.btnIdea:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/j-azpeitia/feed/");
				data.putString("name", "Super Idea");
				break;
			case R.id.btnPencil:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/a-fassi/feed/");
				data.putString("name", "Super Pencil");
				break;
			case R.id.btnPixel:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/f-bril/feed/");
				data.putString("name", "Super Pixel");
				break;
			case R.id.btnSem:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/l-casado/feed/");
				data.putString("name", "Super SEM");
				break;
			case R.id.btnSocial:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/bloggobalo-es/feed/");
				data.putString("name", "Super Social");
				break;
			case R.id.btnSpeed:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/a-gonzalez/feed/");
				data.putString("name", "Super Speed");
				break;
			case R.id.btnTrix:
				data.putString("feedUrl", "http://www.gobalo.es/blog/author/cristina/feed/");
				data.putString("name", "Super Trix");
				break;
			
		}
		
		Fragment newPostsFragment = new AllPostsFragment();
		newPostsFragment.setArguments(data);

		MainActivity main=((MainActivity) getActivity());
		main.getSectionsPageAdapter().swapFragment(0, newPostsFragment, data.getString("name"));
		main.getViewPager().setCurrentItem(0);
	}
}
