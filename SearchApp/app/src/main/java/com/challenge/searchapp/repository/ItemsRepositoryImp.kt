package com.challenge.searchapp.repository

import androidx.lifecycle.LiveData
import com.challenge.searchapp.datasource.RestDataSource
import com.challenge.searchapp.model.ItemDao
import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import retrofit2.Response
import javax.inject.Inject

class ItemsRepositoryImp @Inject constructor(
    private val dataSource: RestDataSource,
    private val articleDao: ItemDao
) : ItemsRepository {
    override suspend fun getItems(query: String, page: Int): Response<ItemResponse> {
        return dataSource.getItems(query, page)
    }

    override suspend fun getArticle(id: String): Response<ItemDetails> {
        return dataSource.getArticle(id)
    }

    override suspend fun insertArticle(item: ItemDetails?) {
        return articleDao.insertArticle(item)
    }

    override suspend fun deleteArticle(item: ItemDetails) = articleDao.deleteArticle(item)

    override fun getSavedArticles(): LiveData<List<ItemDetails>> {
        return articleDao.getAllArticles()
    }
}