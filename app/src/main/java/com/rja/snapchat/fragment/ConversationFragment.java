package com.rja.snapchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.rja.snapchat.R;

/**
 * Created by rjaylward on 3/4/17
 */

public class ConversationFragment extends BaseFragment {

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    private ViewGroup mMainLayout;
    private float mSidebarWidth;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_conversation;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mMainLayout = (ViewGroup) findViewById(R.id.fc_main_layout);
        mSidebarWidth = getResources().getDimension(R.dimen.conversations_side_bar_width);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fc_toolbar);
        toolbar.setTitle("Conversation");
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_info_details);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showOrHideSidePanel();
            }

        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fc_recycler_view);

    }

    private void showOrHideSidePanel() {
        float translationX = mMainLayout.getTranslationX();
        if(translationX == 0 || translationX == mSidebarWidth) {
            mMainLayout.animate()
                    .translationX(translationX == 0 ? mSidebarWidth : 0)
                    .setDuration(300);
        }
    }

}
