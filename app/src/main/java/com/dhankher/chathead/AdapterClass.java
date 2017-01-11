package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dhankher on 1/9/2017.
 */
public class AdapterClass extends RecyclerView.Adapter<AdapterClass.viewHolder> {
    Context context;
    List<String> list;

    public AdapterClass(Context context1, List<String> lst){
        this.context=context1;
        this.list=lst;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);
        return new viewHolder(myView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public viewHolder(final View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.tv);

        }
        }

    }

