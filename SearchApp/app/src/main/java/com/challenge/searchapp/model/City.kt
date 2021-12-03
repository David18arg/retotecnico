package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class City (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)