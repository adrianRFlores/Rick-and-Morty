package com.example.laboratorio8

data class CharacterDetailsAPI(
    val url: String,
    val gender: String,
    val id: Int,
    val image: String,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val location: Location,
    val type: String,
    val episode: List<String>,
    val created: String
)