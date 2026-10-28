package com.example.a5_2_melekhova

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var year: String,
    var genre: String,
    var rating: String,
    var poster: String = ""
)