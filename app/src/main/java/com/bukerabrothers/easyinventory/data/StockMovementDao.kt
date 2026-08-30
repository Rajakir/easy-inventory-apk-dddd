package com.bukerabrothers.easyinventory.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDao {
    @Query("SELECT * FROM stock_movements WHERE productId = :productId ORDER BY timestamp DESC")
    fun getForProduct(productId: Long): Flow<List<StockMovement>>

    @Query("SELECT * FROM stock_movements ORDER BY timestamp DESC LIMIT 100")
    fun getRecent(): Flow<List<StockMovement>>

    @Insert
    suspend fun insert(movement: StockMovement): Long
}
