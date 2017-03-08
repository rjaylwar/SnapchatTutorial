package com.rja.snapchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * A {@link TextureView} that can be adjusted to a specified aspect ratio.
 */
public class AutoFitTextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    private int mScreenHeight = 0;
    private int mScreenWidth = 0;

    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height, int screenWidth, int screenHeight) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int width = mScreenWidth;
//        int height = mScreenHeight;

        int aspectHeight = 0; int aspectWidth = 0;
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
//            if (width < height * mRatioWidth / mRatioHeight) {
//                aspectHeight = width * mRatioHeight / mRatioWidth;
//                aspectWidth = width;
//                setMeasuredDimension(aspectWidth, aspectHeight);
//            } else {
                aspectHeight = height;
                aspectWidth = height * mRatioWidth / mRatioHeight;
                setMeasuredDimension(aspectWidth, aspectHeight);
//            }

        }
    }

}