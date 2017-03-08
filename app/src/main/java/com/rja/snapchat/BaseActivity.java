package com.rja.snapchat;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

/**
 * Created by rjaylward on 3/5/17
 */

public class BaseActivity extends AppCompatActivity {

    private Point mScreenSize = new Point();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(mScreenSize);
    }

    public Point getScreenSize() {
        return mScreenSize;
    }
}
