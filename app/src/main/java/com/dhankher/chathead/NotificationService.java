package com.dhankher.chathead;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Dhankher on 1/2/2017.
 */


public class NotificationService extends NotificationListenerService {


    Context context;
    private String TAG="notification";

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {

        Log.d("notification", "onNotificationPosted: ");

        String pack = statusBarNotification.getPackageName();
      //  String ticker = statusBarNotification.getNotification().tickerText.toString();
        Bundle extras = statusBarNotification.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Log.i("Package",pack);
      //  Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        Intent intent = new Intent("Msg");
        intent.putExtra("package", pack);
    //    intent.putExtra("ticker", ticker);
        intent.putExtra("title", title);
        intent.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
