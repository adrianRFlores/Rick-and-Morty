package com.example.laboratorio8

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    val gender: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val name: String,
    val origin: String,
    val species: String,
    val status: String,
    val epCount: Int,
)
