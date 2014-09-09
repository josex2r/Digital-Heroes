package com.josex2r.digitalheroes.controllers;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.ScrollableMenu;
import com.josex2r.digitalheroes.model.ScrollableMenuViewHolder;
import com.josex2r.digitalheroes.model.Typefaces;
 
public class ScrollableMenuAdapter extends ArrayAdapter<ScrollableMenu> {
 
      Context context;
      List<ScrollableMenu> scrollableMenuItemList;
      int layoutResID;
 
      public ScrollableMenuAdapter(Context context, int layoutResourceID, List<ScrollableMenu> listItems){
    	  super(context, layoutResourceID, listItems);
    	  this.context=context;
    	  this.scrollableMenuItemList=listItems;
    	  this.layoutResID=layoutResourceID;
      }
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ScrollableMenuViewHolder menuHolder;
		View view = convertView;
		
		if( view==null ){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			menuHolder = new ScrollableMenuViewHolder();
			
			view = inflater.inflate(layoutResID, parent, false);
			menuHolder.lblScrollableMenuText = (TextView) view.findViewById(R.id.lblItemDrawerText);
			menuHolder.lblScrollableMenuIcon = (TextView) view.findViewById(R.id.lblItemDrawerIcon);
			view.setTag(menuHolder);
		}else{
			menuHolder=(ScrollableMenuViewHolder) view.getTag();
		}
		
		ScrollableMenu dItem = (ScrollableMenu) this.scrollableMenuItemList.get(position);
		
		Typeface font = Typefaces.get( context.getApplicationContext(), "font/fontawesome-webfont.ttf" );
		menuHolder.lblScrollableMenuIcon.setTypeface(font);
		menuHolder.lblScrollableMenuIcon.setText(dItem.getIcon());
		menuHolder.lblScrollableMenuText.setText(dItem.getText());
		
		return view;
	}
}