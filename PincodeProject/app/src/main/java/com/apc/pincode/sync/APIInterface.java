package com.apc.pincode.sync;

import com.apc.pincode.PostOfficeNames;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by akshay on 30/07/18
 */
public interface APIInterface {


    @GET("/api/postoffice/{po_name}")
    Call<PostOfficeNames> getPOByName(@Path("po_name") String po_name);

    @GET("/api/pincode/{pincode}")
    Call<PostOfficeNames> getPOByPincode(@Path("pincode") String pincode);

}
