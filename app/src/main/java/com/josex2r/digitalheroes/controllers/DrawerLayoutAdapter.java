package com.josex2r.digitalheroes.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.model.DrawerLayoutItem;
import com.josex2r.digitalheroes.model.Typefaces;

import java.util.List;
 
public class DrawerLayoutAdapter extends ArrayAdapter<DrawerLayoutItem> {
 
      Context context;
      List<DrawerLayoutItem> drawerLayoutItemItemList;
      int layoutResID;
 
      public DrawerLayoutAdapter(Context context, int layoutResourceID, List<DrawerLayoutItem> listItems){
    	  super(context, layoutResourceID, listItems);
    	  this.context=context;
    	  this.drawerLayoutItemItemList =listItems;
    	  this.layoutResID=layoutResourceID;
      }
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        DrawerLayoutViewHolder menuHolder;
		View view = convertView;
		
		if( view==null ){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			menuHolder = new DrawerLayoutViewHolder();
			
			view = inflater.inflate(layoutResID, parent, false);
			menuHolder.lblScrollableMenuText = (TextView) view.findViewById(R.id.lblItemDrawerText);
			menuHolder.lblScrollableMenuIcon = (TextView) view.findViewById(R.id.lblItemDrawerIcon);
			view.setTag(menuHolder);
		}else{
			menuHolder=(DrawerLayoutViewHolder) view.getTag();
		}
		
		DrawerLayoutItem dItem = (DrawerLayoutItem) this.drawerLayoutItemItemList.get(position);
		
		Typeface font = Typefaces.get( context.getApplicationContext(), "font/fontawesome-webfont.ttf" );
		menuHolder.lblScrollableMenuIcon.setTypeface(font);
		menuHolder.lblScrollableMenuIcon.setText(dItem.getIcon());
		menuHolder.lblScrollableMenuText.setText(dItem.getText());
		
		return view;
	}

    public class DrawerLayoutViewHolder {
        public TextView lblScrollableMenuIcon;
        public TextView lblScrollableMenuText;
    }
}