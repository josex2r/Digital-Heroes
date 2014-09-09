package com.josex2r.digitalheroes.model;

public class DrawerLayoutItem {
	 
	//Menu text name
    String text;
    //Menu text icon (font-awesome)
    String icon;

    public DrawerLayoutItem(String text, String icon) {
    	super();
    	this.text = text;
    	this.icon = icon;
    }

    public String getText(){
    	return text;
    }
    public void setText(String text){
    	this.text = text;
    }
    public String getIcon(){
    	return icon;
    }
    public void setIcon(String icon){
    	this.icon = icon;
    }

}
