package com.gaelanbolger.woltile;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private int orientation;
    private int space;

    public SpaceItemDecoration(int orientation, int space) {
        this.orientation = orientation;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL) {
            outRect.set(0, 0, 0, space);
        } else {
            outRect.set(0, 0, space, 0);
        }
    }
}
