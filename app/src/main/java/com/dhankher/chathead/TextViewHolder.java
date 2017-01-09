package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Dhankher on 1/2/2017.
 */

public class TextViewHolder extends RecyclerView.ViewHolder implements BindableItem{
    TextView tv;
    public TextViewHolder(View view){
        super(view);
        tv = (TextView) view.findViewById(R.id.tvRecycle);

    }

    @Override
    public void bindViewHolder(Context context, Item item) {
        tv.setText(((TextItem) item).getTitle());
        Log.d("getTitle", "bindViewHolder: "+((TextItem) item).getTitle());

    }
}