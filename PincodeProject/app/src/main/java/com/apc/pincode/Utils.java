package com.apc.pincode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by akshay on 30/07/18
 */
public class Utils {


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



}
