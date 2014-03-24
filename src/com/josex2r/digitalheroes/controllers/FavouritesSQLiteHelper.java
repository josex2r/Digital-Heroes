package com.josex2r.digitalheroes.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesSQLiteHelper extends SQLiteOpenHelper{
	
	String sqlCreate = "CREATE TABLE favourites (title TEXT, url TEXT)";
	
	public FavouritesSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS favourites");
		
		db.execSQL(sqlCreate);
		
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS favourites");
		
		db.execSQL(sqlCreate);
	}

}
