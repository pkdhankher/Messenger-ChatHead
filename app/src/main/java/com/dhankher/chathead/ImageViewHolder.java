package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Dhankher on 1/2/2017.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder implements BindableItem{
    ImageView iv;
    private String TAG = getClass().getCanonicalName();
    public ImageViewHolder(View view){
        super(view);
        iv = (ImageView) view.findViewById(R.id.iv);
        Log.d(TAG, "ImageViewHolder: mkn");
    }

    @Override
    public void bindViewHolder(Context context, Item item) {
        Log.d(TAG, "bindViewHolder: ");

        iv.setImageResource(R.drawable.remove);
//        Glide.with(context)
//                .load(((ImageItem) item).getThumbnail())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(iv);

        Log.d("msg",((ImageItem) item).getThumbnail());
    }
}