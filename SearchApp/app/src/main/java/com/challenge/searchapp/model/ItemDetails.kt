package com.challenge.searchapp.model

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*
import java.io.Serializable

@Entity(tableName = "items")
data class ItemDetails (
    @PrimaryKey()
    val id: String,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "price")
    val price: String?,
    @ColumnInfo(name = "currency_id")
    val currency_id: String?,
    @ColumnInfo(name = "available_quantity")
    val available_quantity: String?,
    @ColumnInfo(name = "sold_quantity")
    val sold_quantity: String?,
    @ColumnInfo(name = "condition")
    val condition: String?,
    @ColumnInfo(name = "permalink")
    val permalink: String?,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String?,
    @ColumnInfo(name = "secure_thumbnail")
    val secure_thumbnail: String?,
    @ColumnInfo(name = "accepts_mercadopago")
    val accepts_mercadopago: String?,
    @Ignore
    @Nullable
    val pictures: List<Picture>?,
    @Ignore
    @Nullable
    val attributes: List<Attribute>?
):Serializable {
    constructor(id: String, title: String?, price: String?, currency_id: String?, available_quantity: String?, sold_quantity: String?, condition: String?, permalink: String?, thumbnail: String?, secure_thumbnail: String?,accepts_mercadopago: String?) : this(id, title, price, currency_id, available_quantity, sold_quantity, condition, permalink, thumbnail, secure_thumbnail, accepts_mercadopago, null, null)
}
