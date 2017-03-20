package com.rja.snapchat;

import com.rja.snapchat.util.Print;

/**
 * Created by rjaylward on 3/3/17
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Print.init("Snap", true, null);
        VideoCache.init(getApplicationContext(), null);
    }
}
