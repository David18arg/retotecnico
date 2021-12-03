package com.challenge.searchapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.challenge.searchapp.di.RepositoryModule
import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import com.challenge.searchapp.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Response
import javax.inject.Singleton
import com.challenge.searchapp.model.Paging


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepositoryModule {

    @Provides
    @Singleton
    fun userRepository(): ItemsRepository =
        object : ItemsRepository {

            private val items = MutableLiveData<List<ItemDetails>>(listOf());

            override suspend fun getItems(query: String, page: Int): Response<ItemResponse> {
                val itemList = items.value!!
                val newItem = ItemDetails(
                    "MLA1234 ${itemList.size}",
                    "Notebook Asus ${itemList.size}",
                    "86000",
                    "ARG",
                    "${itemList.size}",
                    "100",
                    "new",
                    "https://articulo.mercadolibre.com.ar/MLA-932471563-notebook-asus-n4020-128gb-ssd-4gb-156-full-hd-windows-10-_JM",
                    "http://http2.mlstatic.com/D_893029-MLA48010907279_102021-O.jpg",
                    "",
                    "true"
                )
                val itemDetails: MutableList<ItemDetails> = mutableListOf(newItem)
                val paging: Paging = Paging(100,50)
                val itemResponse = ItemResponse("asus", itemDetails, paging)

                return Response.success(itemResponse)
            }

            override suspend fun getArticle(id: String): Response<ItemDetails> {
                val itemList = items.value!!
                val newItem = ItemDetails(
                    "MLA1234 ${itemList.size}",
                    "Notebook Asus ${itemList.size}",
                    "86000",
                    "ARG",
                    "${itemList.size}",
                    "100",
                    "new",
                    "https://articulo.mercadolibre.com.ar/MLA-932471563-notebook-asus-n4020-128gb-ssd-4gb-156-full-hd-windows-10-_JM",
                    "http://http2.mlstatic.com/D_893029-MLA48010907279_102021-O.jpg",
                    "",
                    "true"
                )

                return Response.success(newItem)
            }

            override suspend fun insertArticle(item: ItemDetails?) {
                items.postValue(items.value?.toMutableList()?.apply { add(item!!) })
            }

            override suspend fun deleteArticle(item: ItemDetails) {
                items.postValue(items.value?.toMutableList()?.apply { remove(item) })
            }

            override fun getSavedArticles(): LiveData<List<ItemDetails>> = items
        }
}