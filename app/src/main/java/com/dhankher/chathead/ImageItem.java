package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Created by Dhankher on 1/2/2017.
 */
public class ImageItem extends Item{
    public String thumbnail;
    public String getThumbnail(){
        return thumbnail;
    }

    public void setThumbnail(String thumbnail){
        this.thumbnail=thumbnail;
    }

    @Override
    public int getType() {
        return IMAGE_ITEM;
    }

    public static RecyclerView.ViewHolder createViewHolder(Context context) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_layouts,null));
    }

}