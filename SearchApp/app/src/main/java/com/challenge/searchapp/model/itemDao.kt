package com.challenge.searchapp.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(item: ItemDetails?)

    @Query("SELECT * FROM items")
    fun getAllArticles(): LiveData<List<ItemDetails>>

    @Delete
    fun deleteArticle(item: ItemDetails)
}