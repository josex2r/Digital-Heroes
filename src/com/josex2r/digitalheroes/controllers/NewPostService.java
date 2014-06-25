package com.josex2r.digitalheroes.controllers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class NewPostService extends Service{
	private NewPostAlarm alarm = new NewPostAlarm();
	
    public void onCreate(){
        super.onCreate();       
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         alarm.setAlarm(NewPostService.this);
         return START_STICKY;
	}
    
    public void onStart(Context context,Intent intent, int startId){
        alarm.setAlarm(context);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
