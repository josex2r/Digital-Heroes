package com.josex2r.digitalheroes.controllers;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.josex2r.digitalheoroes.R;
import com.josex2r.digitalheroes.model.DrawerItem;
import com.josex2r.digitalheroes.model.DrawerViewHolder;
 
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
 
      Context context;
      List<DrawerItem> drawerItemList;
      int layoutResID;
 
      public DrawerAdapter(Context context, int layoutResourceID,
                  List<DrawerItem> listItems) {
            super(context, layoutResourceID, listItems);
            this.context = context;
            this.drawerItemList = listItems;
            this.layoutResID = layoutResourceID;
 
      }
 
      @SuppressLint("NewApi")
	@Override
      public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
 
    	  DrawerViewHolder drawerHolder;
            View view = convertView;
 
            if( view==null ){
                  LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                  drawerHolder = new DrawerViewHolder();
 
                  view = inflater.inflate(layoutResID, parent, false);
                  drawerHolder.lblDrawerText = (TextView) view.findViewById(R.id.lblItemDrawerText);
                  drawerHolder.lblDrawerIcon = (TextView) view.findViewById(R.id.lblItemDrawerIcon);
 
                  view.setTag(drawerHolder);
            }else{
                  drawerHolder = (DrawerViewHolder) view.getTag();
            }
 
            DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);
            
            Typeface font = Typefaces.get( context.getApplicationContext(), "font/fontawesome-webfont.ttf" );
            drawerHolder.lblDrawerIcon.setTypeface(font);
            drawerHolder.lblDrawerIcon.setText(dItem.getIcon());
            drawerHolder.lblDrawerText.setText(dItem.getText());
 
            return view;
      }
}