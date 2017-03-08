package com.rja.snapchat.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjaylward on 3/3/17
 */

public class Chat implements IStory {

    private int mFirstParicipantId;
    private int mSecondParicipantId;

    private User mFirstParticipant;
    private User mSecondParticipant;

    @SerializedName("updated_at")
    private long mUpdatedAt;

    @SerializedName("messages")
    private List<ChatItem> mChatMessages = new ArrayList<>();

    public int getLatestMessageType() {
        if(mChatMessages != null && !mChatMessages.isEmpty()) {
            return mChatMessages.get(mChatMessages.size() - 1).getType();
        }
        return ChatItem.TYPE_UNKNOWN;
    }

    public List<ChatItem> getChatMessages() {
        return mChatMessages;
    }

    @Override
    public boolean hasUnwatchedItems() {
        for(ChatItem chatMessage : mChatMessages) {
            if(chatMessage.getMessage() != null)
                continue;

            if(!chatMessage.isViewed())
                return true;
        }

        return false;
    }

    @Override
    public User getSource() {
        return null;
    }

    @Override
    public List<? extends IStoryItem> getStoryItems() {
        return mChatMessages;
    }

    @Override
    public IStoryItem getCurrentStoryItem() {
        for(ChatItem chatMessage : mChatMessages) {
            if(chatMessage.getMessage() != null)
                continue;

            if(!chatMessage.isViewed())
                return chatMessage;
        }

        return null;
    }

    public void setFirstParicipantId(int firstParicipantId) {
        mFirstParicipantId = firstParicipantId;
    }

    public void setSecondParicipantId(int secondParicipantId) {
        mSecondParicipantId = secondParicipantId;
    }

    public void setFirstParticipant(User firstParticipant) {
        mFirstParticipant = firstParticipant;
    }

    public void setSecondParticipant(User secondParticipant) {
        mSecondParticipant = secondParticipant;
    }

    public void setChatMessages(List<ChatItem> chatMessages) {
        mChatMessages = chatMessages;
    }
}
