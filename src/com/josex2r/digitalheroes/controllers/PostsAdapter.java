package com.josex2r.digitalheroes.controllers;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.model.BitmapCollection;
import com.josex2r.digitalheroes.model.Post;
import com.josex2r.digitalheroes.model.PostViewHolder;

public class PostsAdapter extends ArrayAdapter<Post>{

	private Context context;
	private List<Post> news;
	private int resource;
	private ListView lvPosts;
	private BitmapCollection images;
	
	public PostsAdapter(Context context, int resource, List<Post> objects, ListView lv) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.resource=resource;
		this.context=context;
		this.news=objects;
		this.lvPosts=lv;
		this.images=BitmapCollection.getInstance();
	}
	
	public ListView getListView(){
		return lvPosts;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=convertView;
		PostViewHolder viewHolder;
		Post currPost=news.get(position);
		
		if(row==null){
			LayoutInflater inflater=((Activity) context).getLayoutInflater();
			row=inflater.inflate(resource, null);
			viewHolder=new PostViewHolder();
			viewHolder.lblTitle=(TextView) row.findViewById(R.id.lblTitle);
			viewHolder.lblDescription=(TextView) row.findViewById(R.id.lblDescription);
			viewHolder.ivImage=(ImageView) row.findViewById(R.id.ivImage);
			viewHolder.pbImage=(ProgressBar) row.findViewById(R.id.pbImage);
			viewHolder.ivBookmark=(ImageView)row.findViewById(R.id.ivBookmark);
			row.setTag(viewHolder);
		}else{
			viewHolder=(PostViewHolder) row.getTag();
		}

		viewHolder.lblTitle.setText( currPost.getTitle() );
		viewHolder.lblDescription.setText( currPost.getDescription() );
		viewHolder.pbImage.setIndeterminate(true);
		hideImage(viewHolder);
		

		if(currPost.getImageLink().equals("NO-IMAGE")){

			//Log.d("MyApp","NO-IMAGE");
			viewHolder.ivImage.setImageBitmap( images.getBitmapFromMemCache("NO-IMAGE") );
			showImage(viewHolder);
			
		}else{
			Log.d("MyApp","Check if image exist");
			
			if(images.getBitmapFromMemCache( currPost.getImageLink() )==null){
				
				Log.d("MyApp","currPost.getImage()==null");
				ImageLoader downloader=new ImageLoader(viewHolder);
		
				downloader.execute( currPost.getImageLink() );
				
			}else if(!currPost.getLoaded()){
			
				viewHolder.ivImage.setImageBitmap( images.getBitmapFromMemCache(currPost.getImageLink()) );
				showImage(viewHolder);
			
			}
			
		}
		
			
		
		
		//Log.d("MyApp",currPost.getImageLink().toString());
		/*
		if(currPost.getImageLink().equals("NO-IMAGE")){
			currPost.setImage(noImageBitmap);
			viewHolder.ivImage.setImageBitmap( currPost.getImage() );
			showImage(viewHolder);
		}else{ //Exist Image link
			//Exist Bitmap
			if(currPost.getImage()!=null){
				viewHolder.ivImage.setImageBitmap(currPost.getImage());
				showImage(viewHolder);
			}else{
				//Check if image exist
				MainActivity mainActivity=(MainActivity) context;
				Blog blog=mainActivity.getBlog();
				SparseArray<SparseArray<List<Post>>> filteredPagedPosts=blog.getPosts();
				
				for(int j=0;j<filteredPagedPosts.size();j++)
					if(filteredPagedPosts.valueAt(j)!=null)
						for(int k=0;k<filteredPagedPosts.valueAt(j).size();k++)
							if(filteredPagedPosts.valueAt(j).valueAt(k)!=null)
								for(int l=0;l<filteredPagedPosts.valueAt(j).valueAt(k).size();l++)
									if(filteredPagedPosts.valueAt(j).valueAt(k).get(l)!=null)
										if(filteredPagedPosts.valueAt(j).valueAt(k).get(l).getImageLink().equals(currPost.getImageLink()))
											if(filteredPagedPosts.valueAt(j).valueAt(k).get(l).getImage()!=null)
												currPost.setImage( filteredPagedPosts.valueAt(j).valueAt(k).get(l).getImage() );
										
					
				if(currPost.getImage()!=null){
					//Log.d("MyApp", "-----------> "+currPost.getImage());
					viewHolder.ivImage.setImageBitmap(currPost.getImage());
					showImage(viewHolder);
				}else if(!currPost.isLoading()){
					//Async task
					Log.d("MyApp", "----------------------------->");
					currPost.setLoading(true);
					ImageLoader downloader=new ImageLoader();
					downloader.postHolder=viewHolder;
					downloader.execute(position);
				}
					
			}
		}*/
		
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
	
	public class ImageLoader extends AsyncTask<String, Integer, Bitmap>{
		
		private final WeakReference<PostViewHolder> postHolder;
		Bitmap bitmap=null;
		
		public ImageLoader(PostViewHolder holder){
			postHolder=new WeakReference<PostViewHolder>(holder);
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			// TODO Auto-generated method stub
			//Log.d("MyApp","Trying to download image");
			URL postUrl;
			try {
				postUrl=new URL(url[0]);
		        URLConnection conn=postUrl.openConnection();
		        conn.connect();
		        InputStream stream=postUrl.openStream();
		        bitmap = BitmapFactory.decodeStream(stream);

		        //post.setImage( bitmap );
		        

		    }catch(Exception e){
		    	bitmap=images.getBitmapFromMemCache("NO-IMAGE");
		    	//post.setImage( bitmap );
		    	return null;
		    }
			images.addBitmapToMemoryCache(url[0], bitmap);
			
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			//Log.d("MyApp",post.getTitle());
			//Log.d("MyApp",postHolder.get().lblTitle.getText().toString());
			postHolder.get().ivImage.setImageBitmap( result );
			showImage(postHolder.get());

			//postHolder.ivImage.setImageBitmap( result );
			super.onPostExecute(result);
		}
	}

}
