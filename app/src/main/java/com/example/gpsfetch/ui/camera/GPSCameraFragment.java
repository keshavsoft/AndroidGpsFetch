package com.example.gpsfetch.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpsfetch.R;
import com.example.gpsfetch.data.ImageDatabase;
import com.example.gpsfetch.data.entity.ImageEntity;
import com.example.gpsfetch.data.repository.ImageRepository;
import com.example.gpsfetch.ui.adapter.ImageAdapter;
import com.example.gpsfetch.ui.viewmodel.ImageViewModel;
import com.example.gpsfetch.ui.viewmodel.ImageViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GPSCameraFragment extends Fragment {

    private ImageView imageViewPhoto;
    private TextView txtLocation;
    private Button btnTakePhoto;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private ImageViewModel imageViewModel;
    private FusedLocationProviderClient fusedLocationClient;

    private Bitmap capturedImage;
    private double currentLatitude;
    private double currentLongitude;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        if (bitmap != null) {
                            capturedImage = bitmap; // Store Bitmap globally
                            imageViewPhoto.setImageBitmap(bitmap);
                            getCurrentLocation();
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
        txtLocation = view.findViewById(R.id.txtLocation);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        recyclerView = view.findViewById(R.id.recyclerView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize ViewModel
        ImageDatabase db = ImageDatabase.Companion.getDatabase(requireContext());
        ImageRepository repository = new ImageRepository(db.imageDao());
        ImageViewModelFactory factory = new ImageViewModelFactory(repository);
        imageViewModel = new ViewModelProvider(this, factory).get(ImageViewModel.class);

        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
            }
        });

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        imageAdapter = new ImageAdapter(requireContext());
        recyclerView.setAdapter(imageAdapter);

        imageViewModel.getAllImages().observe(getViewLifecycleOwner(), new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(List<ImageEntity> images) {
                imageAdapter.setImageList(images);
            }
        });

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    txtLocation.setText("Latitude: " + currentLatitude + "\nLongitude: " + currentLongitude);
                    String imagePath = saveImageToInternalStorage(capturedImage);
                    saveImageToDatabase(imagePath, currentLatitude, currentLongitude);
                } else {
                    txtLocation.setText("Location Not Found");
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 102);
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = requireContext().getFilesDir();
        File imageFile = new File(directory, "IMG_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }

    private void saveImageToDatabase(String imagePath, double latitude, double longitude) {
        ImageEntity imageEntity = new ImageEntity(0, imagePath, latitude, longitude);
        imageViewModel.insertImage(imageEntity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
        if (requestCode == 102 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }
}
