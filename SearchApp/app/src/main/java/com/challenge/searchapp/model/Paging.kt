package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class Paging (
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int
)