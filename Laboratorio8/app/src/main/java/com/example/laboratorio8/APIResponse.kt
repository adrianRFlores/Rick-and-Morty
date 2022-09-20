package com.example.laboratorio8

import com.google.gson.annotations.SerializedName

data class APIResponse(
    val info: CallData,
    @SerializedName("results") val chars: List<Character>
)