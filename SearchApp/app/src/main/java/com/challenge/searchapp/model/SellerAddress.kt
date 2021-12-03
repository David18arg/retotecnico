package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class SellerAddress (
    @SerializedName("state") val state: State,
    @SerializedName("search_location") val search_location: SearchLocation
)