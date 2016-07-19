package com.mooviest.ui;

/**
 * Created by jesus on 19/7/16.
 */

public class SingletonSwipe {
    public boolean enabled;
    private static SingletonSwipe ourInstance = new SingletonSwipe();

    public static SingletonSwipe getInstance() {
        return ourInstance;
    }

    private SingletonSwipe() {
    }
}
