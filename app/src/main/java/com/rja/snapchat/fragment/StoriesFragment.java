package com.rja.snapchat.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rja.snapchat.MainActivity;
import com.rja.snapchat.Constants;
import com.rja.snapchat.R;
import com.rja.snapchat.adapter.StoriesAdapter;
import com.rja.snapchat.data.DataProvider;
import com.rja.snapchat.data.TestDataProvider;
import com.rja.snapchat.listeners.DataCallback;
import com.rja.snapchat.models.Story;
import com.rja.snapchat.view.StoryViewer;

import java.util.List;

/**
 * Created by rjaylward on 3/6/17
 */

public class StoriesFragment extends BaseFragment {

    private StoriesAdapter mAdapter;

    public static StoriesFragment create() {
        return new StoriesFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RecyclerView list = (RecyclerView) findViewById(R.id.fc_recycler_view);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(mAdapter = new StoriesAdapter());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        list.addItemDecoration(itemDecoration);
        DataProvider dataProvider = new TestDataProvider();

        dataProvider.getStories(Constants.USER_ID, new DataCallback<List<Story>>() {

            @Override
            public void onDataFetched(List<Story> data, Exception error) {
                if(error == null) {
                    mAdapter.setItems(data);
                }
            }

        });

        mAdapter.setOnStoryClickedListener(new StoriesAdapter.OnStoryClickedListener() {

            @Override
            public void onStoryClicked(final int index, Story conversation, Point point) {
                if(getActivity() instanceof MainActivity) {
                    if(conversation.hasUnwatchedItems()) {
                        //Highly coupled, fix later
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