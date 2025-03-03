package com.example.gpsfetch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gpsfetch.data.entity.ImageEntity
import com.example.gpsfetch.data.repository.ImageRepository
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    private val _allImages = MutableLiveData<List<ImageEntity>>()
    val allImages: LiveData<List<ImageEntity>> get() = _allImages

    init {
        viewModelScope.launch {
            _allImages.postValue(repository.getAllImages())
        }
    }

    fun insertImage(image: ImageEntity) {
        viewModelScope.launch {
            repository.insertImage(image)
            _allImages.postValue(repository.getAllImages()) // Update LiveData after insertion
        }
    }
}

class ImageViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}