package com.apc.pincode;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class Pincode extends MultiDexApplication {

    private static Context mContext;
    private static Application sInstance;
    public static Tracker tracker;
    public static GoogleAnalytics analytics;

    public static Context getAppContext() {
        return mContext;
    }

    public static Application getsInstance() {
        return sInstance;
    }

    synchronized public static Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (tracker == null) {
            analytics = GoogleAnalytics.getInstance(mContext);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Pincode.mContext = getApplicationContext();
        getDefaultTracker();
        sInstance = this;
    }
}
