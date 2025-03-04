package com.example.gpsfetch.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Note: If your base URL ends with a slash, no leading slash is needed here.
    @GET("utility/who/AboutUs")
    Call<String> getAboutUs();
}
