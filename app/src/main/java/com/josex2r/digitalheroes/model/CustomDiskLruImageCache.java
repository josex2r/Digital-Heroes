package com.josex2r.digitalheroes.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.josex2r.digitalheroes.R;
import com.josex2r.digitalheroes.util.DiskLruImageCache;

import java.io.File;

/**
 * Created by Jose on 09/09/2014.
 */
public class CustomDiskLruImageCache {

    public static final String NO_IMAGE_NAME = "no-image";

    private Bitmap noImage;

    private DiskLruImageCache cache;

    private boolean existsCache;

    public CustomDiskLruImageCache(Context context,String uniqueName, int diskCacheSize,
                                   Bitmap.CompressFormat compressFormat, int quality){

        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), uniqueName);
        if( !cacheDir.exists() ){
            cacheDir.mkdirs();
        }
        noImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
        if( cacheDir.exists() ){
            this.cache = new DiskLruImageCache(context, uniqueName, diskCacheSize, compressFormat, quality);
            this.cache.put(NO_IMAGE_NAME, noImage);
        }

    }

    public Bitmap getNoImage(){
        if(existsCache()){
            return this.cache.getBitmap(NO_IMAGE_NAME);
        }
        return noImage;
    }

    private DiskLruImageCache getCache(){
        return cache;
    }

    public boolean existsCache(){
        return this.cache != null;
    }

    public void put(String key, Bitmap data){
        if(existsCache()){
            this.cache.put(key, resizeBitmap(data, 800));
        }
    }

    public Bitmap getImage(String key){
        if(!existsCache()){
            return null;
        }
        Bitmap image = this.cache.getBitmap(key);
        return image;
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
}
