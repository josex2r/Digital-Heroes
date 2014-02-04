package com.josex2r.digitalheroes.controllers;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.model.Post;
import com.josex2r.digitalheroes.model.PostViewHolder;

public class PostsAdapter extends ArrayAdapter<Post>{

	private Context context;
	private List<Post> news;
	private int resource;
	
	public PostsAdapter(Context context, int resource, List<Post> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.resource=resource;
		this.context=context;
		this.news=objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=convertView;
		PostViewHolder viewHolder;
		
		if(row==null){
			LayoutInflater inflater=((Activity) context).getLayoutInflater();
			row=inflater.inflate(resource, null);
			viewHolder=new PostViewHolder();
			viewHolder.lblTitle=(TextView) row.findViewById(R.id.lblTitle);
			viewHolder.lblDescription=(TextView) row.findViewById(R.id.lblDescription);
			viewHolder.ivImage=(ImageView) row.findViewById(R.id.ivImage);
			viewHolder.pbImage=(ProgressBar) row.findViewById(R.id.pbImage);
			row.setTag(viewHolder);
		}else{
			viewHolder=(PostViewHolder) row.getTag();
		}
		
		Post currPost=news.get(position);

		viewHolder.lblTitle.setText( currPost.getTitle() );
		viewHolder.lblDescription.setText( currPost.getDescription() );
		viewHolder.pbImage.setIndeterminate(true);
		
		if(!currPost.getImageLink().equals("NO-IMAGE")){
			if(currPost.getImage()!=null){
				viewHolder.ivImage.setImageBitmap(currPost.getImage());
				showImage(viewHolder);
			}else{
				//Async task
				hideImage(viewHolder);
				ImageLoader downloader=new ImageLoader();
				downloader.postHolder=viewHolder;
				downloader.execute(currPost);
			}
		}else{
			viewHolder.ivImage.setImageDrawable( context.getResources().getDrawable(R.drawable.no_image) );
			showImage(viewHolder);
		}
		return row;
	}
	
	private void hideImage(PostViewHolder viewHolder){
		viewHolder.pbImage.setVisibility(View.VISIBLE);
		viewHolder.ivImage.setVisibility(View.GONE);
	}
	
	private void showImage(PostViewHolder viewHolder){
		viewHolder.pbImage.setVisibility(View.GONE);
		viewHolder.ivImage.setVisibility(View.VISIBLE);
	}
	
	public class ImageLoader extends AsyncTask<Post, Integer, Bitmap>{
		
		public PostViewHolder postHolder;

		@Override
		protected Bitmap doInBackground(Post... params) {
			// TODO Auto-generated method stub
			Log.d("MyApp","Trying to download image");
			Bitmap bitmap=null;
			URL postUrl;
			try {
				postUrl=new URL(params[0].getImageLink());
		        URLConnection conn=postUrl.openConnection();
		        conn.connect();
		        InputStream stream=postUrl.openStream();
		        bitmap = BitmapFactory.decodeStream(stream);

		        params[0].setImage( bitmap );

		    }catch(Exception e){
		    	return null;
		    }
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			if(result!=null)
				postHolder.ivImage.setImageBitmap( result );
			else
				postHolder.ivImage.setImageDrawable( context.getResources().getDrawable(R.drawable.no_image) );
			showImage(postHolder);
			super.onPostExecute(result);
		}
	}

}
