package com.rja.snapchat.listeners;

/**
 * Created by rjaylward on 3/7/17
 */

public interface DataCallback<T> {
    void onDataFetched(T data, Exception error);
}
