package com.challenge.searchapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Picture (
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String?,
    @SerializedName("secure_url") val secure_url: String?
)