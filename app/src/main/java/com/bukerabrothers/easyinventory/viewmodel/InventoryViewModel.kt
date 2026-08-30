package com.bukerabrothers.easyinventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bukerabrothers.easyinventory.data.Category
import com.bukerabrothers.easyinventory.data.MovementType
import com.bukerabrothers.easyinventory.data.Product
import com.bukerabrothers.easyinventory.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    val categories: StateFlow<List<Category>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val searchQuery = MutableStateFlow("")
    private val selectedCategoryId = MutableStateFlow<Long?>(null)

    val products: StateFlow<List<Product>> = combine(searchQuery, selectedCategoryId) { q, cat ->
        q to cat
    }.flatMapLatest { (q, cat) -> repository.searchProducts(q, cat) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStock: StateFlow<List<Product>> = repository.getLowStock()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalStockValue: StateFlow<Double> = repository.getTotalStockValue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalUnits: StateFlow<Int> = repository.getTotalUnits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setSearchQuery(q: String) { searchQuery.value = q }
    fun setCategoryFilter(id: Long?) { selectedCategoryId.value = id }

    fun saveProduct(product: Product) = viewModelScope.launch { repository.saveProduct(product) }
    fun updateProduct(product: Product) = viewModelScope.launch { repository.updateProduct(product) }
    fun deleteProduct(product: Product) = viewModelScope.launch { repository.deleteProduct(product) }

    fun saveCategory(name: String) = viewModelScope.launch { repository.saveCategory(Category(name = name)) }

    fun recordStockIn(productId: Long, qty: Int, note: String?) = viewModelScope.launch {
        repository.recordMovement(productId, MovementType.STOCK_IN, qty, note)
    }

    fun recordStockOut(productId: Long, qty: Int, note: String?) = viewModelScope.launch {
        repository.recordMovement(productId, MovementType.STOCK_OUT, qty, note)
    }

    fun getProduct(id: Long) = repository.getProduct(id)
    fun getMovements(productId: Long) = repository.getMovementsForProduct(productId)

    class Factory(private val repository: InventoryRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InventoryViewModel(repository) as T
        }
    }
}
