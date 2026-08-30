package com.bukerabrothers.easyinventory.repository

import com.bukerabrothers.easyinventory.data.AppDatabase
import com.bukerabrothers.easyinventory.data.Category
import com.bukerabrothers.easyinventory.data.MovementType
import com.bukerabrothers.easyinventory.data.Product
import com.bukerabrothers.easyinventory.data.StockMovement
import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val db: AppDatabase) {

    // Products
    fun getProducts(): Flow<List<Product>> = db.productDao().getAll()
    fun searchProducts(query: String, categoryId: Long?): Flow<List<Product>> =
        db.productDao().search(query, categoryId)
    fun getProduct(id: Long): Flow<Product?> = db.productDao().getById(id)
    fun getLowStock(): Flow<List<Product>> = db.productDao().getLowStock()
    fun getTotalStockValue(): Flow<Double> = db.productDao().getTotalStockValue()
    fun getTotalUnits(): Flow<Int> = db.productDao().getTotalUnits()

    suspend fun saveProduct(product: Product): Long = db.productDao().insert(product)
    suspend fun updateProduct(product: Product) = db.productDao().update(product)
    suspend fun deleteProduct(product: Product) = db.productDao().delete(product)

    // Categories
    fun getCategories(): Flow<List<Category>> = db.categoryDao().getAll()
    suspend fun saveCategory(category: Category): Long = db.categoryDao().insert(category)
    suspend fun deleteCategory(category: Category) = db.categoryDao().delete(category)

    // Stock movements
    fun getMovementsForProduct(productId: Long): Flow<List<StockMovement>> =
        db.stockMovementDao().getForProduct(productId)
    fun getRecentMovements(): Flow<List<StockMovement>> = db.stockMovementDao().getRecent()

    suspend fun recordMovement(productId: Long, type: MovementType, quantity: Int, note: String?) {
        val delta = if (type == MovementType.STOCK_IN) quantity else -quantity
        db.productDao().adjustQuantity(productId, delta)
        db.stockMovementDao().insert(
            StockMovement(productId = productId, type = type, quantity = quantity, note = note)
        )
    }
}
