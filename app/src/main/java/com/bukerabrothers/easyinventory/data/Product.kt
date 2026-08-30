package com.bukerabrothers.easyinventory.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("sku")]
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sku: String? = null,
    val categoryId: Long? = null,
    val quantity: Int = 0,
    val unitPrice: Double = 0.0,
    val lowStockThreshold: Int = 5,
    val photoUri: String? = null,
    val notes: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
