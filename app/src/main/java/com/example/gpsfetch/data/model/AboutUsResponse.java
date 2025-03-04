package com.example.gpsfetch.data.model;

import com.google.gson.annotations.SerializedName;

public class AboutUsResponse {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
