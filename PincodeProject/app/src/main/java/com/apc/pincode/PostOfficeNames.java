package com.apc.pincode;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 30/07/18
 */
public class PostOfficeNames {


    @SerializedName("Message")
    private String message;

    @SerializedName("Status")
    private String status;

    @SerializedName("PostOffice")
    public List<PostOffice> postOffice = new ArrayList();


    public PostOfficeNames(String message, String status, List<PostOffice> postOffice) {
        this.message = message;
        this.status = status;
        this.postOffice = postOffice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostOffice> getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(List<PostOffice> postOffice) {
        this.postOffice = postOffice;
    }
}
