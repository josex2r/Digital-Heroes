package com.josex2r.digitalheroes.model;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

public class Blog {

	//-------------	Posts collection -------------
	private SparseArray<SparseArray<List<Post>>> posts;
	//-------------	Current filter showing -------------
	private int activeFilter;
	//-------------	Current page showing -------------
	private int currentPage;
	//-------------	¿Loading posts? -------------
	private boolean loading;
	//-------------	RSS feed URL -------------
	private String feedUrl;
	
	//-------------	Amount of post per RSS feed page -------------
	public static final int POSTS_PER_FEED=10;
	
	//-------------	Post Filters -------------
		//-------------	All -------------
		public static final int FILTER_ALL=0;
		//-------------	Categories -------------
		public static final int FILTER_ADVERSITING=1;
		public static final int FILTER_CREATIVIDAD=2;
		public static final int FILTER_INSIDE=3;
		public static final int FILTER_MARKETING=4;
		public static final int FILTER_NEGOCIOS=5;
		public static final int FILTER_SEO=6;
		public static final int FILTER_WEB=7;
		//-------------	Authors -------------
		public static final int FILTER_BINARY=20;
		public static final int FILTER_CODE=21;
		public static final int FILTER_CRAFT=22;
		public static final int FILTER_CREA=23;
		public static final int FILTER_IDEA=24;
		public static final int FILTER_NUMBERS=25;
		public static final int FILTER_PENCIL=26;
		public static final int FILTER_PIXEL=27;
		public static final int FILTER_SEM=28;
		public static final int FILTER_SOCIAL=29;
		public static final int FILTER_SPEED=30;
		public static final int FILTER_TRIX=31;
	
	//-------------	Constructor -------------
	public Blog(String url){
		this.feedUrl=url;
		this.currentPage=1;
		this.activeFilter=Blog.FILTER_ALL;
		this.posts=new SparseArray<SparseArray<List<Post>>>();
		this.loading=false;
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
	public int getActiveFilter(){
		return activeFilter;
	}
	public String getFeedUrl(){
		return feedUrl;
	}
	public int getPage(){
		return currentPage;
	}
	
	//-------------	Setters -------------
	public void setCurrentPage(int page){
		this.currentPage=page;
	}
	public void setLoading(boolean l){
		this.loading=l;
	}
	public void setFeedUrl(String url){
		this.feedUrl=url;
	}
	public void setActiveFilter(int filter){
		this.activeFilter=filter;
	}
	
	//-------------	Add posts to posts collection -------------
	public void addPosts(int filter, int page, List<Post> postsToAdd){
		if( posts.get(filter)==null ){
			posts.put(filter, new SparseArray<List<Post>>());
		}
		posts.get(filter).put(page, postsToAdd);
	}
	
	//-------------	Get all post from page 1 to current page -------------
	public List<Post> getFilteredAllPagedPosts(){
		List<Post> filteredPagedPosts=new ArrayList<Post>();
		if( this.posts.get(this.activeFilter)!=null ){
			for(int i=1; i<this.currentPage+1; i++){
				if( this.posts.get(this.activeFilter).get(i)!=null ){
					filteredPagedPosts.addAll( this.posts.get(this.activeFilter).get(i) );
				}
			}
		}
		return filteredPagedPosts;
	}
	
	//-------------	Get all post from current page -------------
	public List<Post> getFilteredPagedPosts(){
		List<Post> filteredPagedPosts=new ArrayList<Post>();
		if( this.posts.get(this.activeFilter)!=null ){
			if( this.posts.get(this.activeFilter).get(this.currentPage)!=null ){
				filteredPagedPosts.addAll( this.posts.get(this.activeFilter).get(this.currentPage) );
			}
		}
		return filteredPagedPosts;
	}
	
}
