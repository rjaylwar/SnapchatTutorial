package com.rja.snapchat.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rjaylward on 3/4/17
 */

public class StoryItem implements IStoryItem {

    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("video_url")
    private String mVideoUrl;
    @SerializedName("viewed")
    private boolean mIsViewed;

    private long mDuration;

    @Override
    public boolean isViewed() {
        return mIsViewed;
    }

    @Override
    public void setViewed(boolean viewed) {
        mIsViewed = viewed;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }
}
