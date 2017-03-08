package com.rja.snapchat.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rjaylward on 3/3/17
 */

public class ChatItem implements IStoryItem {

    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VIDEO = 3;

    private int mFromId;
    private int mToId;
    private long mSentAt;
    private int mDuration;

    @SerializedName("message")
    private String mMessage;
    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("video_url")
    private String mVideoUrl;
    @SerializedName("viewed")
    private boolean mIsViewed;

    public int getFromId() {
        return mFromId;
    }

    public int getToId() {
        return mToId;
    }

    public long getSentAt() {
        return mSentAt;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    public String getMessage() {
        return mMessage;
    }

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

    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }

    public int getType() {
        if(mMessage != null)
            return TYPE_TEXT;
        if(mImageUrl != null)
            return TYPE_IMAGE;
        if(mVideoUrl != null)
            return TYPE_VIDEO;

        return TYPE_UNKNOWN;
    }

    public void setFromId(int fromId) {
        mFromId = fromId;
    }

    public void setToId(int toId) {
        mToId = toId;
    }

    public void setSentAt(long sentAt) {
        mSentAt = sentAt;
    }

    public void setDuration(int lifetime) {
        mDuration = lifetime;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }
}
