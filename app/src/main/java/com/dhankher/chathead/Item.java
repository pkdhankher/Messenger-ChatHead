package com.dhankher.chathead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Dhankher on 1/2/2017.
 */

public abstract class Item {
    public static final int IMAGE_ITEM = 0;
    public static final int TEXT_ITEM = 1;

    public abstract int getType();

    public static RecyclerView.ViewHolder createViewHolderFor(Context context, int itemType){
        switch (itemType)  {
            case IMAGE_ITEM:
                return ImageItem.createViewHolder(context);
            case TEXT_ITEM:
                return TextItem.createViewHolder(context);
        }
        return null;
    }
}
