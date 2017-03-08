package com.rja.snapchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rjaylward on 3/4/17
 */

public class Story implements IStory {

    public User mSource;

    @SerializedName("story_items")
    public List<StoryItem> mStoryItems;

    public boolean hasUnwatchedItems() {
        if(mStoryItems != null) {
            for(StoryItem storyItem : mStoryItems) {
                if(!storyItem.isViewed())
                    return true;
            }
        }
        return false;
    }

    public User getSource() {
        return mSource;
    }

    public List<? extends IStoryItem> getStoryItems() {
        return mStoryItems;
    }

    public StoryItem getCurrentStoryItem() {
        if(mStoryItems != null) {
            for(StoryItem storyItem : mStoryItems) {
                if(!storyItem.isViewed())
                    return storyItem;
            }
        }

        return null;
    }

    public void setSource(User source) {
        mSource = source;
    }

    public void setStoryItems(List<StoryItem> storyItems) {
        mStoryItems = storyItems;
    }
}
