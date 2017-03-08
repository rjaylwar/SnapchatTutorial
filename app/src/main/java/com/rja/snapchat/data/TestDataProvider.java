package com.rja.snapchat.data;

import com.rja.snapchat.listeners.DataCallback;
import com.rja.snapchat.models.Chat;
import com.rja.snapchat.models.ChatItem;
import com.rja.snapchat.models.Story;
import com.rja.snapchat.models.StoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjaylward on 3/6/17
 */

public class TestDataProvider implements DataProvider {

    private Chat createConversation(int mode) {
        Chat conversation = new Chat();
        List<ChatItem> messages = new ArrayList<>();

        ChatItem other1 = new ChatItem();
        other1.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-74085658.jpg?alt=media&token=3c9a3c32-b2c9-4e3c-9c47-f580c165d3d5");
        other1.setDuration(5000);
        messages.add(other1);

        ChatItem chatMessage = new ChatItem();
//        switch(mode%3) {
//            case 0:
//                chatMessage.setDuration(5000);
//                chatMessage.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-456981531.jpg?alt=media&token=f261ab50-38e4-4d82-b41a-667ac01f0bed");
//                break;
//            case 1:
//                chatMessage.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-2010441394.mp4?alt=media&token=1c644779-767f-44ba-97c0-100c3dcde372");
//                break;
//            case 2:
//                chatMessage.setMessage("Hello!");
//                break;
//        }

        chatMessage.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-575156866.mp4?alt=media&token=251308b4-51b5-4255-a1bb-c50d9e73c0ea");
        messages.add(chatMessage);

        ChatItem third = new ChatItem();
        third.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-456981531.jpg?alt=media&token=f261ab50-38e4-4d82-b41a-667ac01f0bed");
        third.setDuration(3000);
        messages.add(third);

        ChatItem fourth = new ChatItem();
        fourth.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-2010441394.mp4?alt=media&token=1c644779-767f-44ba-97c0-100c3dcde372");
        messages.add(fourth);

        conversation.setChatMessages(messages);
        return conversation;
    }

    private Story createStory(int mode) {
        Story conversation = new Story();
        List<StoryItem> messages = new ArrayList<>();

        StoryItem chatMessage = new StoryItem();
        switch(mode%3) {
            case 0:
                chatMessage.setDuration(5000);
                chatMessage.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-74085658.jpg?alt=media&token=3c9a3c32-b2c9-4e3c-9c47-f580c165d3d5");
                break;
            case 1:
                chatMessage.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-2010441394.mp4?alt=media&token=1c644779-767f-44ba-97c0-100c3dcde372");
                StoryItem other1 = new StoryItem();
                other1.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-575156866.mp4?alt=media&token=251308b4-51b5-4255-a1bb-c50d9e73c0ea");
                other1.setDuration(5000);
                messages.add(other1);
                break;
            case 2:
                chatMessage.setDuration(4000);
                chatMessage.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-74085658.jpg?alt=media&token=3c9a3c32-b2c9-4e3c-9c47-f580c165d3d5");
                StoryItem other = new StoryItem();
                other.setDuration(3000);
                other.setImageUrl("https://firebasestorage.googleapis.com/v0/b/snap-21bf6.appspot.com/o/test_images%2FSnapchat-74085658.jpg?alt=media&token=3c9a3c32-b2c9-4e3c-9c47-f580c165d3d5");
                messages.add(other);
                break;
        }

        messages.add(chatMessage);
        conversation.setStoryItems(messages);
        return conversation;
    }

    @Override
    public void getConversations(String userId, DataCallback<List<Chat>> dataCallback) {
        List<Chat> list = new ArrayList<>();
        int number = 24;
        for(int i = 0; i < number; i++) {
            Chat conversation = createConversation(i);
            list.add(conversation);
        }

        dataCallback.onDataFetched(list, null);
    }

    @Override
    public void getStories(String userId, DataCallback<List<Story>> dataCallback) {
        List<Story> list = new ArrayList<>();
        int number = 24;
        for(int i = 0; i < number; i++) {
            Story conversation = createStory(i);
            list.add(conversation);
        }

        dataCallback.onDataFetched(list, null);
    }
}
