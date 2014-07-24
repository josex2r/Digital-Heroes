package com.josex2r.digitalheroes.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;

import com.josex2r.digitalheroes.MainActivity;
import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.controllers.AsyncTaskListener;
import com.josex2r.digitalheroes.controllers.FavouritesSQLiteHelper;
import com.josex2r.digitalheroes.controllers.FeedPageLoader;
import com.josex2r.digitalheroes.utils.DiskLruImageCache;

public class Blog {
	//Singleton pattern
	private static Blog INSTANCE=new Blog();
	//-------------	Posts collection -------------
	private SparseArray<SparseArray<List<Post>>> posts;
	//-------------	Current filter showing -------------
	private BlogFilter activeFilter;
	//-------------	Current page showing -------------
	private int currentPage;
	//-------------	¿Loading posts? -------------
	private boolean loading;
	//-------------	Bitmap Cache -------------
    private DiskLruImageCache images;
    
    private Context context;
    private final static int DB_VERSION=1;
	
	//-------------	Amount of post per RSS feed page -------------
	public static final int POSTS_PER_FEED=10;
	
	//-------------	Loading -------------
	private AsyncTaskListener<Boolean> onLoadCallback;
	public static final int REQUEST_LOAD = 999;
	public static final int REQUEST_LOADED = 200;
	public static final int REQUEST_FAILED = 500;
	
	//-------------	Preferences -------------
	public static final String PREFS_NAMESPACE = "DigitalHeroes";
		public static final String PREFS_NOTIFICATIONS = "notifications";
		public static final String PREFS_LAST_UPDATE = "lastUpdate";
	
	//-------------	Post Filters -------------
	public static final String DEFAULT_FEED_URL="http://blog.gobalo.es/feed/";
		//-------------	All -------------
		public static final BlogFilter FILTER_ALL = new BlogFilter(0, "Todos", "http://blog.gobalo.es/feed/");
		//-------------	Categories -------------
		public static final BlogFilter FILTER_ADVERSITING = new BlogFilter(1, "Adversiting", "http://blog.gobalo.es/category/advertising-2/feed/");
		public static final BlogFilter FILTER_CREATIVIDAD = new BlogFilter(2, "Creatividad", "http://blog.gobalo.es/category/creatividad/feed/");
		public static final BlogFilter FILTER_INSIDE = new BlogFilter(3, "Inside Góbalo", "http://blog.gobalo.es/category/inside-gobalo/feed/");
		public static final BlogFilter FILTER_MARKETING = new BlogFilter(4, "Marketing", "http://blog.gobalo.es/category/marketing-digital-y-social-media/feed/");
		public static final BlogFilter FILTER_NEGOCIOS = new BlogFilter(5, "Negocios", "http://blog.gobalo.es/category/negocios/feed/");
		public static final BlogFilter FILTER_SEO = new BlogFilter(6, "SEO", "http://blog.gobalo.es/category/seo-y-sem/feed/");
		public static final BlogFilter FILTER_WEB = new BlogFilter(7, "WEB", "http://blog.gobalo.es/category/web-y-programacion/feed/");
		//-------------	Authors -------------
		public static final BlogFilter FILTER_BINARY = new BlogFilter(20, "Super 01101", "http://blog.gobalo.es/author/a-vara/feed/");
		public static final BlogFilter FILTER_CODE = new BlogFilter(21, "Super Code", "http://blog.gobalo.es/author/jl-represa/feed/");
		public static final BlogFilter FILTER_CRAFT = new BlogFilter(22, "Super Craft", "http://blog.gobalo.es/author/n-pastor/feed/");
		public static final BlogFilter FILTER_CREA = new BlogFilter(23, "Super Crea", "http://blog.gobalo.es/author/g-gomez/feed/");
		public static final BlogFilter FILTER_IDEA = new BlogFilter(24, "Super Idea", "http://blog.gobalo.es/author/j-azpeitia/feed/");
		public static final BlogFilter FILTER_NUMBERS = new BlogFilter(25, "Super Numbers", "http://blog.gobalo.es/author/m-becerra/feed/");
		public static final BlogFilter FILTER_PENCIL = new BlogFilter(26, "Super Pencil", "http://blog.gobalo.es/author/a-fassi/feed/");
		public static final BlogFilter FILTER_PIXEL = new BlogFilter(27, "Super Pixel", "http://blog.gobalo.es/author/f-bril/feed/");
		public static final BlogFilter FILTER_SEM = new BlogFilter(28, "Super SEM", "http://blog.gobalo.es/author/l-casado/feed/");
		public static final BlogFilter FILTER_SOCIAL = new BlogFilter(29, "Super Social", "http://blog.gobalo.es/author/bloggobalo-es/feed/");
		public static final BlogFilter FILTER_SPEED = new BlogFilter(30, "Super Speed", "http://blog.gobalo.es/author/a-gonzalez/feed/");
		public static final BlogFilter FILTER_TRIX = new BlogFilter(31, "Super Trix", "http://blog.gobalo.es/author/cristina/feed/");
		//-------------	Favourites -------------
		public static final BlogFilter FILTER_FAVOURITES = new BlogFilter(99, "Favoritos", null);
	
	//-------------	Constructor -------------
	public Blog(){
		this.currentPage = 1;
		this.activeFilter = new BlogFilter(0, "Todos", "http://blog.gobalo.es/feed/"); //FILTER_ALL
		this.posts = new SparseArray<SparseArray<List<Post>>>();
		this.loading = false;
	}
	
	//------------- Singleton Pattern -------------
	private static void createInstance(){
        if( INSTANCE==null ){
            synchronized(Blog.class){
                if( INSTANCE==null ){ 
                    INSTANCE = new Blog();
                }
            }
        }
    }
	
	public static Blog getInstance(){
        createInstance();
        return INSTANCE;
    }
	
	//-------------	Getters -------------
	public int getCurrentPage(){
		return this.currentPage;
	}
	public boolean isLoading(){
		return loading;
	}
	public SparseArray<SparseArray<List<Post>>> getPosts(){
		return this.posts;
	}
	public BlogFilter getActiveFilter(){
		return activeFilter;
	}
	public int getPage(){
		return currentPage;
	}
	public DiskLruImageCache getImages(){
		return images;
	}
	
	//-------------	Setters -------------
	public void setCurrentPage(int page){
		Log.d("MyApp", "Setting context");
		this.currentPage=page;
	}
	public void setLoading(boolean l){
		this.loading=l;
	}
	public void setActiveFilter(BlogFilter filter){
		this.activeFilter = filter;
	}
	public void setContext(Context context){
		this.context=context;
		this.images=new DiskLruImageCache(this.context, "postCache", 700000, CompressFormat.JPEG, 80); //1Mb memory cache
	}
	
	public void setOnLoadListener(AsyncTaskListener<Boolean> callback){
		Log.d("MyApp", "Setting onLoadListener");
		int postCount = (getFilteredPagedPosts()).size();
		if( postCount>0 ){
			//Post have been loaded, dispatch callback
			callback.onTaskComplete(true);
			onLoadCallback = null;
		}else{
			//Wait load to dispatch callback
			onLoadCallback = callback;
		}
	}
	public void dispatchLoadListener(Boolean result){
		Log.d("MyApp", "Dispatching onLoadListener");
		if( onLoadCallback!=null ){
			if( result ){
				onLoadCallback.onTaskComplete(result);
			}else{
				onLoadCallback.onTaskFailed();
			}
			onLoadCallback = null;
		}
	}
	
	public void loadFavouritesFromDB(){
		List<Post> loadedPosts=new ArrayList<Post>();
		
		if(context!=null){
			SQLiteDatabase db=null;
			try {
				
				FavouritesSQLiteHelper conexionDB=new FavouritesSQLiteHelper(context, "DBFavourites", null, DB_VERSION);
				db=conexionDB.getReadableDatabase();
				Cursor i=db.rawQuery("SELECT title, link, comments, date, creator, guid, description, imageLink FROM favourites WHERE 1", null);
				
				if(i.getCount()>0){
					i.moveToFirst();
					do{
						Log.d("MyApp", "LOADED FROM BBDD: "+i.getString(0));
						Post post=new Post();
						post.setTitle( i.getString(0) );
						post.setLink( i.getString(1) );
						post.setComments( i.getString(2) );
						post.setDate( i.getString(3) );
						post.setCreator( i.getString(4) );
						post.setGuid( i.getString(5) );
						post.setDescription( i.getString(6) );
						post.setImageLink( i.getString(7) );
						
						loadedPosts.add( post );
					}while(i.moveToNext());
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			finally{
				db.close();	
			}
		}
		
		this.addPosts(Blog.FILTER_FAVOURITES, 1, loadedPosts);
	}
	
	//-------------	Add posts to posts collection -------------
	public void addPosts(BlogFilter filter, int page, List<Post> postsToAdd){
		if( posts.get(filter.getId())==null ){
			posts.put(filter.getId(), new SparseArray<List<Post>>());
		}
		//Never override pages
		if( posts.get(filter.getId()).get(page)==null ){
			posts.get(filter.getId()).put(page, postsToAdd);
		}
	}
	
	//-------------	Get all post from page 1 to current page -------------
	public List<Post> getFilteredAllPagedPosts(){
		List<Post> filteredPagedPosts = new ArrayList<Post>();
		if( this.posts.get(this.activeFilter.getId())!=null ){
			for(int i=1; i<this.currentPage+1; i++){
				if( this.posts.get(this.activeFilter.getId()).get(i)!=null ){
					filteredPagedPosts.addAll( this.posts.get(this.activeFilter.getId()).get(i) );
				}
			}
		}
		return filteredPagedPosts;
	}
	
	//-------------	Get all post from current page -------------
	public List<Post> getFilteredPagedPosts(){
		
		if( this.activeFilter.equals(Blog.FILTER_FAVOURITES) ){
			
			return this.posts.get(Blog.FILTER_FAVOURITES.getId()).get(1);
			
		}else{
			
			List<Post> filteredPagedPosts=new ArrayList<Post>();
			if( this.posts.get(this.activeFilter.getId())!=null ){
				if( this.posts.get(this.activeFilter.getId()).get(this.currentPage)!=null ){
					filteredPagedPosts.addAll( this.posts.get(this.activeFilter.getId()).get(this.currentPage) );
				}
			}
			return filteredPagedPosts;
		
		}
		
	}
	
	//-------------	Toggle post (click on star icon) -------------
	public void addRemoveFromFavourites(int position){
		if(context!=null){
			Log.d("MyApp", "addRemoveFromFavourites:"+Integer.toString(position));
			
			Post selectedPost=getFilteredAllPagedPosts().get(position);
			String link=selectedPost.getLink();
			
			//Always delete
			
			
			if(!isFavourite(link)){
				
				//Insert
				removeFavourite(link);
				addFavourite(selectedPost);
				
			}else
				removeFavourite(link);
			
		}
	}
	
	//-------------	Remove post from favourites (collection + DB) -------------
	private boolean removeFavourite(String link){
		
		if(context!=null){
			SQLiteDatabase db=null;
			try {
				FavouritesSQLiteHelper conexionDB=new FavouritesSQLiteHelper(context, "DBFavourites", null, DB_VERSION);
				db=conexionDB.getWritableDatabase();
				
				db.delete("favourites", "link=?", new String[]{link});
				
				List<Post> favourites=this.posts.get(Blog.FILTER_FAVOURITES.getId()).get(1);
				for(int i=0;i<favourites.size();i++){
					if( favourites.get(i).getLink().equals(link) )
						favourites.remove(i);
				}
				
				return true;
				
			}catch(Exception e){
				return false;
			}finally{
				db.close();	
			}
		}else
			return false;
	}
	
	//-------------	Add post to favourites (collection + DB) -------------
	private boolean addFavourite(Post post){
		
		if(context!=null){
			SQLiteDatabase db=null;
			try {
				FavouritesSQLiteHelper conexionDB=new FavouritesSQLiteHelper(context, "DBFavourites", null, DB_VERSION);
				db=conexionDB.getWritableDatabase();
				
				ContentValues insertSQL = new ContentValues();
				insertSQL.put("title", post.getTitle());
				insertSQL.put("link", post.getLink());
				insertSQL.put("comments", post.getComments());
				insertSQL.put("date", post.getDate());
				insertSQL.put("creator", post.getCreator());
				insertSQL.put("guid", post.getGuid());
				insertSQL.put("description", post.getDescription());
				insertSQL.put("imageLink", post.getImageLink());
				db.insert("favourites", null, insertSQL);
				
				this.posts.get(Blog.FILTER_FAVOURITES.getId()).get(1).add( post );
				
				return true;
				
			}catch(Exception e){
				return false;
			}finally{
				db.close();	
			}
		}else
			return false;
	}
	
	//-------------	check if post is favourite -------------
	public boolean isFavourite(String link){
		boolean found=false;
		List<Post> favourites=this.posts.get(Blog.FILTER_FAVOURITES.getId()).get(1);
		for(int i=0;i<favourites.size();i++){
			if(favourites.get(i).getLink().equals(link))
				found=true;
		}
		return found;
	}
	
	
	public void checkNewPost(final Context ctx){
		//final Context ctx = context;
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		
		if( prefs.getBoolean(Blog.PREFS_NOTIFICATIONS, true) ){
			
			String lastUpdate = prefs.getString(Blog.PREFS_LAST_UPDATE, "Wed, 18 Jun 2014 08:38:57 +0000");
			
			final DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
			try {
				final Date lastDate = formatter.parse(lastUpdate);
				
				FeedPageLoader lastPosts=new FeedPageLoader(new AsyncTaskListener<List<Post>>() {
					@Override
					public void onTaskComplete(List<Post> loadedPosts) {
						//Display notification
						if( loadedPosts!=null && loadedPosts.size()>0 ){
							Post lastPost = loadedPosts.get(0);
							try {
								Date newLastDate = formatter.parse(lastPost.getDate());
								//Check dates
								if( newLastDate.after(lastDate) ){
									
									SharedPreferences.Editor editor = prefs.edit();
									editor.putString(Blog.PREFS_LAST_UPDATE, lastPost.getDate());
									editor.commit();
									
									NotificationCompat.Builder mBuilder =
										    new NotificationCompat.Builder(ctx)
										    .setContentTitle("Digital Heroes")
										    .setContentText(lastPost.getTitle())
										    .setSmallIcon(R.drawable.ic_notification);
									
									Intent resultIntent = new Intent(ctx, MainActivity.class);
									PendingIntent resultPendingIntent =	PendingIntent.getActivity(ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
									mBuilder.setContentIntent(resultPendingIntent);
									
									NotificationManager mNotifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
									mNotifyMgr.notify(0, mBuilder.build());
									
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					public void onTaskFailed() {
						//Do nothing
					}
				});
				//Get firsts posts and execute callback if not updated
				lastPosts.execute(1);
				
			}catch(ParseException e){
				e.printStackTrace();
				return;
			}
						
		}
	}
	
}
