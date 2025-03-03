package com.example.gpsfetch.data.repository

import com.example.gpsfetch.data.dao.ImageDao
import com.example.gpsfetch.data.entity.ImageEntity

class ImageRepository(private val imageDao: ImageDao) {

    suspend fun insertImage(image: ImageEntity) {
        imageDao.insertImage(image)
    }

    suspend fun getAllImages(): List<ImageEntity> {
        return imageDao.getAllImages()
    }
}
