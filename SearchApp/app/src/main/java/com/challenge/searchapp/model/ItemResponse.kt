package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class ItemResponse (
    @SerializedName("query")
    val query: String,
    @SerializedName("results")
    val results: MutableList<ItemDetails>,
    @SerializedName("paging")
    val paging: Paging
)