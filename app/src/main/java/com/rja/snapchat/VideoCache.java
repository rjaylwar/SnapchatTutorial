package com.rja.snapchat;

import android.content.Context;
import android.support.annotation.Nullable;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by rjaylward on 3/7/17
 */

public class VideoCache {

    private static HttpProxyCacheServer mProxy;

    public static HttpProxyCacheServer getProxy() {
        return mProxy;
    }

    public static void init(Context context, @Nullable HttpProxyCacheServer.Builder builder) {
        if(builder != null)
            mProxy = builder.build();
        else
            mProxy = new HttpProxyCacheServer(context.getApplicationContext());
    }

}
