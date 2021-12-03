package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class Attribute (
    @SerializedName("name") val name: String,
    @SerializedName("value_name") val valueName: String,
)