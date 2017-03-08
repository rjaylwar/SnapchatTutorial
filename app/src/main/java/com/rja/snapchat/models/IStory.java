package com.rja.snapchat.models;

import java.util.List;

/**
 * Created by rjaylward on 3/6/17
 */

public interface IStory {
    public boolean hasUnwatchedItems();

    public User getSource();

    public List<? extends IStoryItem> getStoryItems();

    public IStoryItem getCurrentStoryItem();
}
