package com.josex2r.digitalheroes.model;

import java.util.ArrayList;

public class Post {
	private String title;
	private String link;
	private String comments;
	private String date;
	private String creator;
	private ArrayList<Integer> categories;
	private String guid;
	private String description;
	private String imageLink;
	//private Bitmap image;
	//private boolean isFavourite=false;
	//private boolean loaded=false;
	/*
	public void setLoaded(boolean is){
		this.loaded=is;
	}
	
	public boolean getLoaded(){
		return this.loaded;
	}
	
	public void setFavourite(boolean is){
		this.isFavourite=is;
	}
	
	public boolean isFavourite(){
		return this.isFavourite;
	}
	*/
	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	/*public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}*/

	public Post(){
		this.categories=new ArrayList<Integer>();
	}
	
	public Post(String a, String b){
		this.title=a;
		this.description=b;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public ArrayList<Integer> getCategories() {
		return categories;
	}
	public void setCategory(Integer categories) {
		this.categories.add(categories);
	}
	public boolean hasCategory(int category) {
		return this.categories.indexOf(category)>=0;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
