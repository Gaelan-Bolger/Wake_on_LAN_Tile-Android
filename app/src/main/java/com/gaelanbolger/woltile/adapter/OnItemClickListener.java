package com.gaelanbolger.woltile.adapter;

import android.view.View;

public interface OnItemClickListener {

    void onItemClick(View view, int position);

    default boolean onItemLongClick(View view, int position) {
        return false;
    }
}
