package com.example.gpsfetch.ui.fetch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gpsfetch.R;
import com.example.gpsfetch.viewmodel.ApiViewModel;

public class FetchFragment extends Fragment {

    private ApiViewModel apiViewModel;
    private TextView apiTextView;

    public FetchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fetch, container, false);

        apiTextView = view.findViewById(R.id.apiTextView);

        apiViewModel = new ViewModelProvider(this).get(ApiViewModel.class);

        apiViewModel.getAboutUsData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                apiTextView.setText(data);
            }
        });

        return view;
    }
}
