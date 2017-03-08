package com.rja.snapchat.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rja.snapchat.MainActivity;
import com.rja.snapchat.Constants;
import com.rja.snapchat.R;
import com.rja.snapchat.adapter.ConversationsAdapter;
import com.rja.snapchat.data.DataProvider;
import com.rja.snapchat.data.TestDataProvider;
import com.rja.snapchat.listeners.DataCallback;
import com.rja.snapchat.models.Chat;
import com.rja.snapchat.view.StoryViewer;

import java.util.List;

/**
 * Created by rjaylward on 3/3/17
 */

public class ChatFragment extends BaseFragment {

    private ConversationsAdapter mAdapter;

    public static ChatFragment create() {
        return new ChatFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RecyclerView list = (RecyclerView) findViewById(R.id.fc_recycler_view);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(mAdapter = new ConversationsAdapter());
        DataProvider dataProvider = new TestDataProvider();

        dataProvider.getConversations(Constants.USER_ID, new DataCallback<List<Chat>>() {

            @Override
            public void onDataFetched(List<Chat> data, Exception error) {
                mAdapter.setItems(data);
            }

        });

        mAdapter.setOnConversationClickedListener(new ConversationsAdapter.OnConversationClickedListener() {

            @Override
            public void onConversationClicked(final int index, Chat conversation, Point point) {
                if(getActivity() instanceof MainActivity) {
                    if(conversation.hasUnwatchedItems()) {
                        ((MainActivity) getActivity()).showStory(conversation, point, new StoryViewer.StoryViewListener() {
                            @Override
                            public void onStoryHidden() {
                                mAdapter.notifyItemChanged(index);
                            }
                        });
                    }
                }
            }

        });
    }

}
