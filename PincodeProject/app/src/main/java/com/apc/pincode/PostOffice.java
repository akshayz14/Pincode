package com.apc.pincode;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshay on 30/07/18
 */
public class PostOffice {

    /*
           "Name": "New Delhi ",
            "Description": "",
            "PINCode": "110001",
            "BranchType": "Head Post Office",
            "DeliveryStatus": "Delivery",
            "Taluk": "New Delhi",
            "Circle": "New Delhi",
            "District": "New Delhi",
            "Division": "New Delhi GPO",
            "Region": "Delhi",
            "State": "Delhi",
            "Country": "India"

            */

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String description;

    @SerializedName("PINCode")
    private String pincode;

    @SerializedName("BranchType")
    private String branchType;

    @SerializedName("DeliveryStatus")
    private String deliveryStatus;

    @SerializedName("Taluk")
    private String taluk;

    @SerializedName("Circle")
    private String circle;

    @SerializedName("District")
    private String district;

    @SerializedName("Division")
    private String division;

    @SerializedName("Region")
    private String region;

    @SerializedName("State")
    private String state;

    @SerializedName("Country")
    private String country;


    public PostOffice(String name, String description, String pincode, String branchType, String deliveryStatus, String taluk,
                      String circle, String district, String division, String region, String state, String country) {
        this.name = name;
        this.description = description;
        this.pincode = pincode;
        this.branchType = branchType;
        this.deliveryStatus = deliveryStatus;
        this.taluk = taluk;
        this.circle = circle;
        this.district = district;
        this.division = division;
        this.region = region;
        this.state = state;
        this.country = country;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
