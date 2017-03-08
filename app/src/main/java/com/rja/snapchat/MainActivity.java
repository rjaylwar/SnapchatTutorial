package com.rja.snapchat;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rja.snapchat.adapter.MainViewAdapter;
import com.rja.snapchat.fragment.Camera2BasicFragment;
import com.rja.snapchat.fragment.ConversationFragment;
import com.rja.snapchat.models.IStory;
import com.rja.snapchat.util.Print;
import com.rja.snapchat.view.StoryViewer;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Camera2BasicFragment mCameraFragment;
    private ConversationFragment mConversationFragment;
    private View mBackground;
    private ImageView mCameraButton;

    private ViewPager mViewPager;
    private RelativeLayout mRoot;
    private StoryViewer mStoryViewer;
    private View mDivider;

    @ColorInt private int LIGHT_BLUE;
    @ColorInt private int LIGHT_PURPLE;

    private float mCameraButtonTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LIGHT_BLUE = ContextCompat.getColor(this, R.color.light_blue);
        LIGHT_PURPLE = ContextCompat.getColor(this, R.color.light_purple);
        mRoot = (RelativeLayout) findViewById(R.id.am_root);
        mStoryViewer = (StoryViewer) findViewById(R.id.am_story_viewer);
        mStoryViewer.setVisibility(View.INVISIBLE);

        View left = findViewById(R.id.am_left_layout);
        left.setTranslationX(-getScreenSize().x);
        View right = findViewById(R.id.am_right_layout);
        right.setTranslationX(getScreenSize().x);
        setUpCameraToolbar();

        if(null == savedInstanceState) {
//            mCameraFragment = Camera2BasicFragment.newInstance();
            mConversationFragment = ConversationFragment.newInstance();
            getFragmentManager().beginTransaction()
//                    .replace(R.id.am_background_layout, mCameraFragment)
                    .replace(R.id.am_left_layout, mConversationFragment)
                    .commit();
        }

        mCameraButtonTranslationY = getResources().getDimension(R.dimen.camera_button_translation_y);

        mBackground = findViewById(R.id.am_background_view);
        mCameraButton = (ImageView) findViewById(R.id.am_camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem() != 1) {
                    mViewPager.setCurrentItem(1, true);
                }
                else if(mCameraFragment != null)
                    mCameraFragment.takePicture();
            }

        });

        mViewPager = (ViewPager) findViewById(R.id.am_view_pager);
        mViewPager.setAdapter(new MainViewAdapter(getFragmentManager()));
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float scale;
                if(position == 0) {
                    mBackground.setBackgroundColor(LIGHT_BLUE);
                    mBackground.setAlpha(1 - positionOffset);
                    mDivider.setAlpha(positionOffset);
                    scale = .7f + (positionOffset * .3f);
                    mCameraButton.setScaleX(scale);
                    mCameraButton.setScaleY(scale);

                    mCameraButton.setTranslationY((1 - positionOffset) * mCameraButtonTranslationY);
                }
                else if(position == 1) {
                    mBackground.setBackgroundColor(LIGHT_PURPLE);
                    mBackground.setAlpha(positionOffset);
                    mDivider.setAlpha(1 - positionOffset);

                    scale = .7f + ((1 - positionOffset) * .3f);
                    mCameraButton.setScaleX(scale);
                    mCameraButton.setScaleY(scale);

                    mCameraButton.setTranslationY(positionOffset * mCameraButtonTranslationY);
                }

                if((position == 0 && positionOffset > .5) || position == 1 && positionOffset < .5)
                    mCameraButton.setBackgroundResource(R.drawable.circle_white);
                else
                    mCameraButton.setBackgroundResource(R.drawable.circle_gray);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void showStory(IStory story, Point point, final StoryViewer.StoryViewListener listener) {
        mStoryViewer.loadStory(story);
        if(mCameraFragment != null && !mCameraFragment.isPaused())
            mCameraFragment.onPause();

        mStoryViewer.setListener(new StoryViewer.StoryViewListener() {

            @Override
            public void onStoryHidden() {
                mStoryViewer.setVisibility(View.GONE);

                if(mCameraFragment != null && mCameraFragment.isPaused())
                    mCameraFragment.onResume();

                listener.onStoryHidden();
            }

        });

        mStoryViewer.show(point.x, point.y);
    }

    private void setUpCameraToolbar() {
        ViewGroup layout = (ViewGroup) findViewById(R.id.toolbar_layout);
        mDivider = findViewById(R.id.toolbar_bottom_divider);
        ImageView cameraFacingView = (ImageView) findViewById(R.id.toolbar_end_icon);
        cameraFacingView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Print.i("camera button click");
                if(mCameraFragment != null) {
                    mCameraFragment.setFrontFacing(!mCameraFragment.getFrontFacing());
                }
            }

        });
    }

}
