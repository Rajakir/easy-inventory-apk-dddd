package com.bukerabrothers.easyinventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<Product>>

    @Query("""
        SELECT * FROM products
        WHERE (:query = '' OR name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%')
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        ORDER BY name ASC
    """)
    fun search(query: String, categoryId: Long?): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: Long): Flow<Product?>

    @Query("SELECT * FROM products WHERE quantity <= lowStockThreshold ORDER BY quantity ASC")
    fun getLowStock(): Flow<List<Product>>

    @Query("SELECT COALESCE(SUM(quantity * unitPrice), 0.0) FROM products")
    fun getTotalStockValue(): Flow<Double>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM products")
    fun getTotalUnits(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product): Long

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("UPDATE products SET quantity = quantity + :delta, updatedAt = :now WHERE id = :productId")
    suspend fun adjustQuantity(productId: Long, delta: Int, now: Long = System.currentTimeMillis())
}
