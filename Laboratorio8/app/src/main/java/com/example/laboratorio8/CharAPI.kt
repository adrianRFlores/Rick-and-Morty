package com.example.laboratorio8

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CharAPI {

    @GET("/api/character")
    fun getChars(): Call<APIResponse>

    @GET("/api/character/{id}")
    fun getCharacter(
        @Path("id") id: Int
    ): Call<CharacterDetailsAPI>
}