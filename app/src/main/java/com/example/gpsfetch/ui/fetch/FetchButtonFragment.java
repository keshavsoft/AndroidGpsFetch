package com.example.gpsfetch.ui.fetch;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gpsfetch.R;
import com.example.gpsfetch.network.ApiService;
import com.example.gpsfetch.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchButtonFragment extends Fragment {

    private Button btnFetchData;
    private RecyclerView recyclerViewFetch;

    public FetchButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fetch_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFetchData = view.findViewById(R.id.btnFetchData);
        recyclerViewFetch = view.findViewById(R.id.recyclerViewFetch);
        TextView tvApiResponse = view.findViewById(R.id.tvApiResponse);

        btnFetchData.setOnClickListener(v -> {
            tvApiResponse.setText("Fetching Data...");
            fetchData(tvApiResponse);
        });
    }

    private void fetchData(TextView textView) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<String> call = apiService.getAboutUs();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textView.setText(response.body());
                } else {
                    textView.setText("Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                textView.setText("Error: " + t.getMessage());
            }
        });
    }
}
