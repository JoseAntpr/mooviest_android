package com.mooviest.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by jesus on 19/7/16.
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages

        if (SingletonSwipe.getInstance().enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (SingletonSwipe.getInstance().enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }
}

