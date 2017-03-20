package com.rja.snapchat;

import android.os.Handler;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.danikula.videocache.CacheListener;
import com.rja.snapchat.models.IStory;
import com.rja.snapchat.models.IStoryItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjaylward on 3/8/17
 */

public class Bufferer implements CacheListener {

    private List<String> mUrls = new ArrayList<>();
    private List<String> mFailedUrls = new ArrayList<>();
    private List<String> mBufferedUrls = new ArrayList<>();
    private int mRequiredPercent = 50;
    private int mTargetHeight;
    private int mTargetWidth;

    private BufferCallback mBufferCallback;
    private Handler mHandler = new Handler();

    public Bufferer(Request request, RequestManager glide, IStory story, BufferCallback callback) {
        this(
            glide, story, request.mRequiredVideoBufferedPercent, request.mImageWidth,
            request.mImageHeight, request.mReplay, request.mMaxFiles, request.mTimeoutMillis,
            callback
        );
    }

    private Bufferer(RequestManager glide, IStory story, int requiredVideoBufferedPercent, int imageWidth, int imageHeight, boolean replay, int maxFiles, int timeoutMillis, BufferCallback callback) {
        mRequiredPercent = requiredVideoBufferedPercent;
        mTargetHeight = imageHeight;
        mTargetWidth = imageWidth;

        mBufferCallback = callback;

        mHandler.postDelayed(mTimeoutRunnable, timeoutMillis);

        for(IStoryItem iStoryItem : story.getStoryItems()) {
            if(!replay && iStoryItem.isViewed())
                continue;

            if(iStoryItem.getVideoUrl() != null)
                bufferVideoUrl(iStoryItem.getVideoUrl());
            if(iStoryItem.getImageUrl() != null)
                bufferImageUrl(glide, iStoryItem.getImageUrl(), mTargetWidth, mTargetHeight);

            if(mUrls.size() >= maxFiles)
                break;
        }

    }

    private Runnable mTimeoutRunnable = new Runnable() {

        @Override
        public void run() {
            stopListening();
            if(mBufferCallback != null)
                mBufferCallback.onBufferingTimedOut(mBufferedUrls, mFailedUrls);
        }

    };

    private void bufferVideoUrl(String url) {
        mUrls.add(url);
        VideoCache.getProxy().registerCacheListener(this, url);
    }

    private void bufferImageUrl(RequestManager glide, String url, int targetWidth, int targetHeight) {
        if(targetHeight > 0 && targetWidth > 0) {
            glide.load(url)
                    .listener(mGlideListener)
                    .preload(targetWidth, targetHeight);
        }
        else {
            glide.load(url)
                    .listener(mGlideListener)
                    .preload();
        }
    }

    private RequestListener<String, GlideDrawable> mGlideListener = new RequestListener<String, GlideDrawable>() {

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            mBufferedUrls.add(model);
            mUrls.remove(model);
            notifyIfFinished();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            mFailedUrls.add(model);
            mUrls.remove(model);
            notifyIfFinished();
            return false;
        }

    };

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        if(percentsAvailable > mRequiredPercent) {
            mUrls.remove(url);
            mBufferedUrls.add(url);
            notifyIfFinished();
        }
    }

    private void notifyIfFinished() {
        if(mUrls.isEmpty()) {
            if(mBufferCallback != null)
                mBufferCallback.onBufferedToRequiredPercent(mRequiredPercent, mBufferedUrls, mFailedUrls);

            mHandler.removeCallbacks(mTimeoutRunnable);
        }
    }

    public void stopListening() {
        VideoCache.getProxy().unregisterCacheListener(this);
        mHandler.removeCallbacks(mTimeoutRunnable);
        mUrls.clear();
        mBufferedUrls.clear();
        mFailedUrls.clear();


    }

    interface BufferCallback {
        void onBufferedToRequiredPercent(int percent, List<String> loadedUrls, List<String> failedUrls);
        void onBufferingTimedOut(List<String> loadedUrls, List<String> failedUrls);
    }

    public static class Request {

        int mRequiredVideoBufferedPercent;
        int mImageWidth;
        int mImageHeight;
        boolean mReplay;
        int mMaxFiles;
        int mTimeoutMillis;

        public Request(int requiredVideoBufferedPercent, int imageWidth, int imageHeight, boolean replay, int maxFiles, int timeoutMillis) {
            mRequiredVideoBufferedPercent = requiredVideoBufferedPercent;
            mImageWidth = imageWidth;
            mImageHeight = imageHeight;
            mReplay = replay;
            mMaxFiles = maxFiles;
            mTimeoutMillis = timeoutMillis;
        }

        public static class Builder {

            int mRequiredVideoBufferedPercent;
            int mImageWidth;
            int mImageHeight;
            boolean mReplay;
            int mMaxFiles;
            int mTimeoutMillis;

            public Builder setRequiredVideoBufferedPercent(int requiredVideoBufferedPercent) {
                mRequiredVideoBufferedPercent = requiredVideoBufferedPercent;
                return this;
            }

            public Builder setImageSize(int width, int height) {
                mImageHeight = height;
                mImageWidth = width;
                return this;
            }

            public Builder setReplay(boolean replay) {
                mReplay = replay;
                return this;
            }

            public Builder setMaxFiles(int maxFiles) {
                mMaxFiles = maxFiles;
                return this;
            }

            public Builder setTimeoutMillis(int timeoutMillis) {
                mTimeoutMillis = timeoutMillis;
                return this;
            }

            public Request build() {
                return new Request( mRequiredVideoBufferedPercent, mImageWidth, mImageHeight,
                        mReplay, mMaxFiles, mTimeoutMillis);
            }
        }

    }
}
