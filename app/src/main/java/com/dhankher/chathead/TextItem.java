package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Created by Dhankher on 1/2/2017.
 */
public class TextItem extends Item {


    String title;


    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }


    @Override
    public int getType() {
        return TEXT_ITEM;
    }


    public static RecyclerView.ViewHolder createViewHolder(Context context) {
        return new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.text_layout,null));
    }
}
