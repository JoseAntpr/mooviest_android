package com.mooviest.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jesus on 16/11/16.
 */

public class PaddingItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;
    private Context context;

    public PaddingItemDecoration(int size, Context context) {
        this.size = size;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Apply offset only to first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left += pxFromDp();
        }
    }

    public float pxFromDp() {
        return size * context.getResources().getDisplayMetrics().density;
    }
}
