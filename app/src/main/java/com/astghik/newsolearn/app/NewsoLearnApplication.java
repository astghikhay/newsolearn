package com.astghik.newsolearn.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class NewsoLearnApplication extends Application {
    private static Context context;
    private static Activity activity;

    /**
     * setCurrentActivity(null) in onPause() on each activity
     * setCurrentActivity(this) in onResume() on each activity
     */

    public static void setCurrentActivity(Activity currentActivity) {
        activity = currentActivity;
    }

    public synchronized static Context getAppContext() {
        return NewsoLearnApplication.context;
    }

    public static Activity currentActivity() {
        return activity;
    }

    public void onCreate() {
        super.onCreate();
        NewsoLearnApplication.context = getApplicationContext();

    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

}