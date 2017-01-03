package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Dhankher on 1/2/2017.
 */
public class AdapterClass extends RecyclerView.Adapter {
    Context ctx;
    List<Item> list;


    public AdapterClass(Context context, List<Item> lst) {
        this.ctx = context;
        this.list = lst;

    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Item.createViewHolderFor(ctx, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((BindableItem) holder).bindViewHolder(ctx, list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

