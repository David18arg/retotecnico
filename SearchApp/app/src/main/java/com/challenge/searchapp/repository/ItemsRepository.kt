package com.challenge.searchapp.repository

import androidx.lifecycle.LiveData
import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import retrofit2.Response

interface ItemsRepository {
    suspend fun getItems(query: String, page: Int): Response<ItemResponse>
    suspend fun getArticle(id: String): Response<ItemDetails>
    suspend fun insertArticle(item: ItemDetails?)
    suspend fun deleteArticle(item: ItemDetails)
    fun getSavedArticles(): LiveData<List<ItemDetails>>
}