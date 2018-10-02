package com.apc.pincode;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by akshay on 30/07/18
 */
public class Utils {

    public static Gson gson = new Gson();
    public static String locationName = "qwerty";


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isInteger(String searchText) {
        boolean flag = false;
        try {
            Integer.parseInt(searchText);
            flag = true;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }


    public static void trackGoogleAnalyticsEvent(String category, String action, String label, int value) {
//        setCustomDimensions();
        Tracker tracker = Pincode.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .setCustomDimension(1, locationName)
                .build());
    }

    public static void trackGoogleAnalyticsScreenView(String screenName) {
//        setCustomDimensions();
        Tracker tracker = Pincode.getDefaultTracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder()
                .setCustomDimension(1, locationName)
                .build());
    }


    private static void setCustomDimensions() {
        SharedPreferences preferences = Pincode.getAppContext().getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        locationName = preferences.getString(AppConstants.RECENT_SEARCHES, null);
    }


}
