package com.josex2r.digitalheroes.model;

public class BlogFilter {
	private int id;
	private String name;
	private String feedURl;
	
	public BlogFilter(int id, String name, String feedURl) {
		super();
		this.id = id;
		this.name = name;
		this.feedURl = feedURl;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFeedURl() {
		return feedURl;
	}
	public void setFeedURl(String feedURl) {
		this.feedURl = feedURl;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		System.out.println("BlogFilter.equals()");
		if( !(obj instanceof BlogFilter) ){
			System.out.println("!(obj instanceof BlogFilter)");
            return false;
		}if( obj == this ){
			System.out.println("obj == this");
            return true;
		}
		System.out.println(this.id==((BlogFilter) obj).getId());
		System.out.println(this.id + "==" + ((BlogFilter) obj).getId());
		return this.id==((BlogFilter) obj).getId();
	}
}
