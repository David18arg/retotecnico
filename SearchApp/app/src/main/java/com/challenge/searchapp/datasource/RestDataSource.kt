package com.challenge.searchapp.datasource

import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestDataSource {
    @GET("sites/MLA/search?")
    suspend fun getItems(
        @Query("q")
        searchQuery: String,
        @Query("limit")
        pageNumber: Int
    ): Response<ItemResponse>

    @GET("items/{id}")
    suspend fun getArticle(
        @Path("id")
        id: String
    ): Response<ItemDetails>
}