package com.rja.snapchat.data;

import com.rja.snapchat.listeners.DataCallback;
import com.rja.snapchat.models.Chat;
import com.rja.snapchat.models.Story;

import java.util.List;

/**
 * Created by rjaylward on 3/4/17
 */

public interface DataProvider {

    void getConversations(String userId, DataCallback<List<Chat>> dataCallback);
    void getStories(String userId, DataCallback<List<Story>> dataCallback);

}
