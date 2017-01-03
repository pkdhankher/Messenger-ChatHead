package com.dhankher.chathead;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhankher on 1/2/2017.
 */

public class ChatView extends AppCompatActivity {

    RecyclerView rv;
    List<Item> feedlist;
    AdapterClass adapterClass;
    ImageItem imageItem;
    TextItem textItem;
    String title, text, pack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatview);

        rv = (RecyclerView) findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));
        feedlist = new ArrayList<>();
        adapterClass=new AdapterClass(this,feedlist);
        rv.setAdapter(adapterClass);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

//        rv = (RecyclerView) findViewById(R.id.recyclerView);


            textItem = new TextItem();

            textItem.setTitle(title);
            feedlist.add(textItem);


            imageItem = new ImageItem();
            imageItem.setThumbnail("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
            feedlist.add(imageItem);



//       feedlist.add(textItem);
//       feedlist.add(imageItem);

        adapterClass = new AdapterClass(this, feedlist);

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pack = intent.getStringExtra("package");
            title = intent.getStringExtra("title");
            text = intent.getStringExtra("text");
        }
    };

}


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