package com.challenge.searchapp.model

import com.google.gson.annotations.SerializedName

data class SearchResponseModel(
    //@SerializedName("installments")val installments: Installments,
    @SerializedName("available_filters")val available_filters: List<Any>,
    @SerializedName("available_sorts")val available_sorts: List<Any>,
    @SerializedName("country_default_time_zone")val country_default_time_zone: String,
    @SerializedName("filters")val filters: List<Any>,
    @SerializedName("paging")val paging: Paging,
    @SerializedName("query")val query: String,
    //@SerializedName("results")val results: List<ResultModel>,
    @SerializedName("site_id")val site_id: String,
    //@SerializedName("sort")val sort: Sort
)