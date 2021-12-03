package com.challenge.searchapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.challenge.searchapp.datasource.RestDataSource
import com.challenge.searchapp.model.ItemDao
import com.challenge.searchapp.model.ItemDetails
import com.challenge.searchapp.model.ItemResponse
import com.challenge.searchapp.model.Paging
import com.challenge.searchapp.repository.ItemsRepositoryImp
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets


private val item1 = ItemDetails("MLA932471563", "Notebook Asus N4020 128gb Ssd 4gb 15.6 Full Hd Windows 10", "50999", "ARS", "1", "500","new","https://articulo.mercadolibre.com.ar/MLA-932471563-notebook-asus-n4020-128gb-ssd-4gb-156-full-hd-windows-10-_JM", "http://http2.mlstatic.com/D_893029-MLA48010907279_102021-O.jpg", "", "true")
private val item2 = ItemDetails("MLA1110351326", "Notebook Asus Vivobook X543ua Gris Oscura 15.6 , Intel Core I5 8250u  8gb De Ram 256gb Ssd, Intel Uhd Graphics 620 1920x1080px Windows 10 Home", "93999.06", "ARS", "32", "30","new","https://www.mercadolibre.com.ar/notebook-asus-vivobook-x543ua-gris-oscura-156-intel-core-i5-8250u-8gb-de-ram-256gb-ssd-intel-uhd-graphics-620-1920x1080px-windows-10-home/p/MLA18522650", "http://http2.mlstatic.com/D_691014-MLA47861577646_102021-I.jpg", "", "true")


class ItemsRepositoryTest {
    private val mockWebServer = MockWebServer().apply {
        url("/")
        dispatcher = myDispatcher
    }

    private val restDataSource = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RestDataSource::class.java)

    private val itemsRepository = ItemsRepositoryImp(restDataSource, MockItemDao())

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Items on the DB are retrieved correctly`() {
        val articles = itemsRepository.getSavedArticles()
        assertEquals(2, articles.value?.size)
    }

    @Test
    fun `Items is deleted correctly`() {
        runBlocking {
            itemsRepository.deleteArticle(item1)

            val articles = itemsRepository.getSavedArticles()
            assertEquals(1, articles.value?.size)
        }
    }

    @Test
    fun `Items is fetched correctly`() {
        runBlocking {
            itemsRepository.insertArticle(item1)

            val items = itemsRepository.getSavedArticles()
            assertEquals(3, items.value?.size)
            assertEquals(item1.id, "MLA932471563")
            assertEquals(item1.title, "Notebook Asus N4020 128gb Ssd 4gb 15.6 Full Hd Windows 10")
            assertEquals(item1.accepts_mercadopago, "true")
            assertEquals(item1.price, "50999")
            assert(item1.thumbnail!!.contains("http://http2.mlstatic.com/D_893029-MLA48010907279_102021-O.jpg"))
        }
    }
}

class MockItemDao : ItemDao {

    private val items = MutableLiveData(listOf(item1, item2));

    override fun insertArticle(item: ItemDetails?) {
        items.value = items.value?.toMutableList()?.apply { add(item!!) }
    }

    override fun getAllArticles(): LiveData<List<ItemDetails>> = items

    override fun deleteArticle(item: ItemDetails) {
        items.value = items.value?.toMutableList()?.apply { remove(item) }
    }
}

val myDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/sites/MLA/search?q=asus" -> MockResponse().apply { addResponse("api_item_details.json") }
            "/items/MLA932471563" -> MockResponse().apply { addResponse("api_item_response.json") }
            else -> MockResponse().setResponseCode(404)
        }
    }
}

fun MockResponse.addResponse(filePath: String): MockResponse {
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        setResponseCode(200)
        setBody(it.readString(StandardCharsets.UTF_8))
    }
    return this
}