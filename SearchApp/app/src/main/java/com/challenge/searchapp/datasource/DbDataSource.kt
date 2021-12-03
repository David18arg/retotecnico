package com.challenge.searchapp.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.challenge.searchapp.model.ItemDao
import com.challenge.searchapp.model.ItemDetails

@Database(entities = [ItemDetails::class],version = 1,exportSchema = false)
abstract class DbDataSource : RoomDatabase() {
    abstract fun getArticleDao(): ItemDao
}