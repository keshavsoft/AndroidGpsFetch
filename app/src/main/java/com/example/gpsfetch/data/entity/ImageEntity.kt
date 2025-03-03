package com.example.gpsfetch.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val imagePath: String,
    val latitude: Double,
    val longitude: Double
)
