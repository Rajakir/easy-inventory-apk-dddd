package com.bukerabrothers.easyinventory.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bukerabrothers.easyinventory.data.Product
import com.bukerabrothers.easyinventory.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: InventoryViewModel,
    onAddProduct: () -> Unit,
    onOpenProduct: (Long) -> Unit
) {
    val products by viewModel.products.collectAsState()
    val totalValue by viewModel.totalStockValue.collectAsState()
    val totalUnits by viewModel.totalUnits.collectAsState()
    val lowStock by viewModel.lowStock.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Easy Inventory Manager") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Default.Add, contentDescription = "Add product")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Stock value: ₹%.2f".format(totalValue))
                Text("Units: $totalUnits")
            }
            if (lowStock.isNotEmpty()) {
                Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Row(Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Text(" ${lowStock.size} item(s) low on stock", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
            var query by remember { mutableStateOf("") }
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.setSearchQuery(it)
                },
                label = { Text("Search products") },
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            )
            LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                items(products, key = { it.id }) { product ->
                    ProductRow(product) { onOpenProduct(product.id) }
                }
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(product.name, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Text("Qty: ${product.quantity}  •  ₹${product.unitPrice}")
        }
    }
}
