package com.example.gpsfetch.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gpsfetch.R

class CameraFragment : Fragment() {

    private lateinit var imageViewPhoto: ImageView
    private lateinit var btnTakePhoto: Button

    // Permission Launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    // Camera Launcher
    private val cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                imageViewPhoto.setImageBitmap(bitmap)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera_kt, container, false) // Use New Layout
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto)
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)

        btnTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        return view
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }
}
