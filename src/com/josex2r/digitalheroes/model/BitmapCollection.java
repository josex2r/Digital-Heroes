package com.josex2r.digitalheroes.model;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCollection {
	//Singleton pattern
	private static BitmapCollection INSTANCE=new BitmapCollection();
	//Bitmap cache
	private LruCache<String, Bitmap> mMemoryCache;
	
	
	public BitmapCollection(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		this.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            return bitmap.getByteCount() / 1024;
	        }
	    };
	}
	
	private static void createInstance(){
        if( INSTANCE==null ){
            synchronized(BitmapCollection.class){
                if( INSTANCE==null ){ 
                    INSTANCE=new BitmapCollection();
                }
            }
        }
    }
	
	public static BitmapCollection getInstance(){
        createInstance();
        return INSTANCE;
    }
	
	public Object clone() throws CloneNotSupportedException {
    	throw new CloneNotSupportedException(); 
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	    	//Resize bitmap
	    	Bitmap resizedBitmap = resizeBitmap(bitmap, 400);
	    	
	    	INSTANCE.mMemoryCache.put(key, resizedBitmap);
	    }
	}
	
	private Bitmap resizeBitmap(Bitmap bitmap, int maxW){
		double ratio=1;
		double newW=bitmap.getWidth();
		double newH=bitmap.getHeight();
		
		if( bitmap.getWidth()>maxW ){
			ratio=maxW/newW;
			newW=maxW;
			newH=ratio*bitmap.getHeight();
		}
		
		return Bitmap.createScaledBitmap(bitmap, ((int)newW), ((int)newH), false);
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return INSTANCE.mMemoryCache.get(key);
	}

	
}
