package com.example.gpsfetch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gpsfetch.network.ApiService;
import com.example.gpsfetch.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiViewModel extends ViewModel {

    private MutableLiveData<String> aboutUsData;

    public ApiViewModel() {
        aboutUsData = new MutableLiveData<>();
        fetchAboutUsData();
    }

    public LiveData<String> getAboutUsData() {
        return aboutUsData;
    }

    private void fetchAboutUsData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<String> call = apiService.getAboutUs();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    aboutUsData.setValue(response.body());
                } else {
                    aboutUsData.setValue("Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                aboutUsData.setValue("Network Error: " + t.getMessage());
            }
        });
    }
}
