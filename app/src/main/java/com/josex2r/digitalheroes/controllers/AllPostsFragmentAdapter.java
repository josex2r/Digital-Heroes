package com.josex2r.digitalheroes.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.Blog;
import com.josex2r.digitalheroes.model.CustomDiskLruImageCache;
import com.josex2r.digitalheroes.model.Post;
import com.josex2r.digitalheroes.model.Typefaces;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class AllPostsFragmentAdapter extends ArrayAdapter<Post>{

	private Context context;
	private List<Post> news;
	private int resource;
	private ListView lvPosts;
	private CustomDiskLruImageCache imagesCache;
	private OnClickListener listener;
    private Blog blog;
	
	public AllPostsFragmentAdapter(Context context, int resource, List<Post> objects, ListView lv, OnClickListener li) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.resource=resource;
		this.context=context;
		this.news=objects;
		this.lvPosts=lv;
        this.blog = Blog.getInstance();
		this.imagesCache = blog.getImagesCache();
		this.listener=li;
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
            //Inflate view
			LayoutInflater inflater =((Activity) context).getLayoutInflater();
			row = inflater.inflate(resource, null);
            //Set view holder
			viewHolder = new PostViewHolder();
			viewHolder.lblTitle = (TextView) row.findViewById(R.id.lblTitle);
			viewHolder.lblDescription = (TextView) row.findViewById(R.id.lblDescription);
			viewHolder.ivImage = (ImageView) row.findViewById(R.id.ivImage);
			viewHolder.pbImage = (ProgressBar) row.findViewById(R.id.pbImage);
			//viewHolder.ivFavourites = (ImageView)row.findViewById(R.id.ivFavourites);
            viewHolder.btnFavourites = (Button)row.findViewById(R.id.btnFavourites);
			row.setTag(viewHolder);
		}else{
			viewHolder = (PostViewHolder) row.getTag();
		}
        //Set data to the view holder
		viewHolder.position = position;
		viewHolder.lblTitle.setText( currPost.getTitle() );
		viewHolder.lblDescription.setText( currPost.getDescription() );
		viewHolder.pbImage.setIndeterminate(true);
        Typeface font = Typefaces.get(context, "font/fontawesome-webfont.ttf");
        viewHolder.btnFavourites.setTypeface(font);
        //Handle image view
		hideImage(viewHolder);

		if(currPost.getImageLink().toUpperCase().equals("NO-IMAGE")){
			//No post image, must set "no_image.jpg"
			viewHolder.ivImage.setImageBitmap( imagesCache.getNoImage() );
			showImage(viewHolder);
		}else{
			//Check if image exist
			if( imagesCache.getImage(currPost.getImageLink())==null ){
				//Image is not stored on disk cache, must download it
				ImageLoader downloader = new ImageLoader(position);
				downloader.postHolder = viewHolder;
				downloader.execute( currPost.getImageLink() );
			}else{
				//Image is stored on disk cache
				viewHolder.ivImage.setImageBitmap( imagesCache.getImage(currPost.getImageLink()) );
				showImage(viewHolder);
			}
		}
		//Check if this post is marked as favourite
		if( blog.isFavourite(currPost) ){
			//viewHolder.ivFavourites.setImageDrawable(context.getResources().getDrawable(android.R.drawable.star_on));
            viewHolder.btnFavourites.setText(context.getString(R.string.icon_star));
		}else{
			//viewHolder.ivFavourites.setImageDrawable(context.getResources().getDrawable(android.R.drawable.star_off));
            viewHolder.btnFavourites.setText(context.getString(R.string.icon_star_half_empty));
		}
		//viewHolder.ivFavourites.setTag(position);
		//viewHolder.ivFavourites.setOnClickListener(this.listener);
        viewHolder.btnFavourites.setTag(position);
        viewHolder.btnFavourites.setOnClickListener(this.listener);
		
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
		
		public PostViewHolder postHolder;
		private int position;
		
		public ImageLoader(int position){
			this.position=position;
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			// TODO Auto-generated method stub
			URL postUrl;
			Bitmap bitmap;
			try {
				postUrl=new URL(url[0]);
		        URLConnection conn=postUrl.openConnection();
		        conn.connect();
		        InputStream stream = postUrl.openStream();
                try {
                    bitmap = BitmapFactory.decodeStream(stream);
                }catch(OutOfMemoryError e){
                    bitmap = imagesCache.getNoImage();
                }
		        //Try to save into cache
                if( imagesCache != null ) {
                    imagesCache.put(url[0], bitmap);
                }

		    }catch(Exception e){
                bitmap = imagesCache.getNoImage();
		    	return null;
		    }
			return bitmap;
			
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			//Prevents lazy image load
			if(this.position==postHolder.position){
				postHolder.ivImage.setImageBitmap( result );
				showImage(postHolder);
			}

			super.onPostExecute(result);
		}
	}

    public static class PostViewHolder {
        public TextView lblTitle, lblDescription;
        public ImageView ivImage;
        public ProgressBar pbImage;
        //public ImageView ivFavourites;
        public Button btnFavourites;
        public int position;
    }

}
