package com.rja.snapchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rjaylward on 3/3/17
 */

public class User {

    @SerializedName("chat_ids")
    private List<Integer> mChatIds;
    private Story mStory;

    private String mName;
    private int mId;
    private String mImageUrl;
    private String mEmail;

}