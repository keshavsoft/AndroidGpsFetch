package com.example.gpsfetch.ui.camera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gpsfetch.R;
import com.example.gpsfetch.databinding.FragmentGpsBinding;
import com.example.gpsfetch.databinding.FragmentBlankBinding;
import com.example.gpsfetch.ui.slideshow.SlideshowViewModel;

public class BlankFragment extends Fragment {
    private FragmentBlankBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBlankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}