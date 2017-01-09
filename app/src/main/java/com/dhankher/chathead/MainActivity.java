package com.dhankher.chathead;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
    TableLayout tab;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

//        if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
//            if (!Settings.canDrawOverlays(this)) {
////                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
////                startActivity(intent);
////            } else {
//            }
//        }
        startService(new Intent(MainActivity.this, ChatHeadService.class));
        startService(new Intent(MainActivity.this, NotificationService.class));
        finish();

    }

}
//        mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Title")
//                .setContentText("Dummy Notification");


//        tab = (TableLayout) findViewById(R.id.tab);
//        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


//    private BroadcastReceiver onNotice = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String pack = intent.getStringExtra("package");
//            String title = intent.getStringExtra("title");
//            String text = intent.getStringExtra("text");
//
//
//            TableRow tr = new TableRow(getApplicationContext());
//            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            TextView textview = new TextView(getApplicationContext());
//            textview.setLayoutParams(new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//            textview.setTextSize(20);
//            textview.setTextColor(Color.parseColor("#0B0719"));
//            textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
//            tr.addView(textview);
//            tab.addView(tr);
//        }
//    };


//    public void onStart(View view) {
//        int mNotificationId = 001;
//        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
//    }


