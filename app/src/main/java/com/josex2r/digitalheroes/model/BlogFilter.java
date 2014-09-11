package com.josex2r.digitalheroes.model;

public class BlogFilter {
    private static int ID_COUNTER = 0;

	private int id;
	private String name;
	private String feedURl;
    private BlogFilterType type;
	
	public BlogFilter(String name, String feedURl, BlogFilterType type) {
		super();
		this.id = ID_COUNTER++;
		this.name = name;
		this.feedURl = feedURl;
        this.type = type;
	}
	
	public int getId() {
		return id;
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
		//System.out.println("BlogFilter.equals()");
		if( !(obj instanceof BlogFilter) ){
			//System.out.println("!(obj instanceof BlogFilter)");
            return false;
		}if( obj == this ){
			//System.out.println("obj == this");
            return true;
		}
		//System.out.println(this.id==((BlogFilter) obj).getId());
		//System.out.println(this.id + "==" + ((BlogFilter) obj).getId());
		return this.id==((BlogFilter) obj).getId();
	}

    public enum BlogFilterType {
        AUTHOR,
        CATEGORY,
        ALL,
        FAVOURITES
    }

    protected void setId(int id){
        this.id = id;
    }

    public BlogFilter clone() {
        BlogFilter clonedFilter = new BlogFilter(this.name, this.feedURl, this.type);
        clonedFilter.setId(this.id);
        return clonedFilter;
    }
}
