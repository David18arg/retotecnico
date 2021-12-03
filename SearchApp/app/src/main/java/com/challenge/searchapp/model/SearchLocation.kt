package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class SearchLocation (
    @SerializedName("city") val city: City
)