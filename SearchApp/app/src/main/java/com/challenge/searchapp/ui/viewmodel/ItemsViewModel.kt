package com.challenge.searchapp.ui.viewmodel

import androidx.lifecycle.*
import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import com.challenge.searchapp.util.Constants
import com.challenge.searchapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import com.challenge.searchapp.repository.ItemsRepositoryImp

@HiltViewModel
class ItemsViewModel @Inject constructor (
    private val itemsRepository: ItemsRepositoryImp
) : ViewModel() {

    val searchItems: MutableLiveData<Resource<ItemResponse>> = MutableLiveData()
    val findArticle: MutableLiveData<Resource<ItemDetails>> = MutableLiveData()
    var searchItemsPage = Constants.QUERY_PAGE_SIZE
    var limitItemsPage = Constants.LIMIT_PAGE_SIZE
    var searchItemsResponse: ItemResponse? = null
    var findArticleResponse: ItemDetails? = null

    fun searchItems(searchQuery: String, pageLimit: Int) = viewModelScope.launch {
        searchItems.postValue(Resource.Loading())
        val response = itemsRepository.getItems(searchQuery, pageLimit)
        searchItems.postValue(handleSearchItemsResponse(response))
    }

    fun findArticle(id: String) = viewModelScope.launch {
        findArticle.postValue(Resource.Loading())
        val response = itemsRepository.getArticle(id)
        findArticle.postValue(handleFindArticleResponse(response))
    }

    private fun handleSearchItemsResponse(response: Response<ItemResponse>) : Resource<ItemResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(searchItemsResponse == null) {
                    searchItemsResponse = resultResponse
                } else {
                    val oldArticles = searchItemsResponse?.results
                    val newArticles = resultResponse.results
                    if(searchItemsResponse?.query.equals(resultResponse.query)) {
                        oldArticles?.addAll(newArticles.subList(oldArticles.size, newArticles.size))
                    } else {
                        oldArticles?.clear()
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(searchItemsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleFindArticleResponse(response: Response<ItemDetails>) : Resource<ItemDetails> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                findArticleResponse = resultResponse
                return Resource.Success(findArticleResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(item: ItemDetails?) = viewModelScope.launch {
        itemsRepository.insertArticle(item)
    }

    fun getSavedItems() = itemsRepository.getSavedArticles()

    fun deleteArticle(item: ItemDetails) = viewModelScope.launch {
        itemsRepository.deleteArticle(item)
    }
}












