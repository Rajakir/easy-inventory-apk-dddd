package com.bukerabrothers.easyinventory.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class MovementType { STOCK_IN, STOCK_OUT }

@Entity(
    tableName = "stock_movements",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class StockMovement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val type: MovementType,
    val quantity: Int,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
